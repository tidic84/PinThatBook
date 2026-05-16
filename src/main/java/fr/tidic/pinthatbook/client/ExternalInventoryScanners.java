package fr.tidic.pinthatbook.client;

import fr.tidic.pinthatbook.PinThatBook;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ExternalInventoryScanners {

    public interface Scanner {
        void scan(Player player, Map<Item, Integer> counts, Map<ItemStack, Boolean> visited);
    }

    private static final List<Scanner> SCANNERS = resolve();

    private ExternalInventoryScanners() {}

    public static void scanAll(Player player, Map<Item, Integer> counts, Map<ItemStack, Boolean> visited) {
        for (Scanner s : SCANNERS) {
            try {
                s.scan(player, counts, visited);
            } catch (Throwable t) {
                PinThatBook.LOGGER.warn("External inventory scanner failed", t);
            }
        }
    }

    private static List<Scanner> resolve() {
        List<Scanner> list = new ArrayList<>();
        tryLoad(list, "curios", "fr.tidic.pinthatbook.client.compat.CuriosInventoryScanner");
        return list;
    }

    private static void tryLoad(List<Scanner> list, String modId, String className) {
        if (!ModList.get().isLoaded(modId)) return;
        try {
            Class<?> cls = Class.forName(className);
            list.add((Scanner) cls.getDeclaredConstructor().newInstance());
            PinThatBook.LOGGER.info("Loaded inventory scanner compat: {}", modId);
        } catch (Throwable t) {
            PinThatBook.LOGGER.warn("Failed to load compat scanner: {}", className, t);
        }
    }
}
