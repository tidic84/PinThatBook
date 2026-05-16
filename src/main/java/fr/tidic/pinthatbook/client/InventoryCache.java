package fr.tidic.pinthatbook.client;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.Map;

public final class InventoryCache {
    private static Map<Item, Integer> cached = Map.of();

    private InventoryCache() {}

    public static Map<Item, Integer> get() {
        return cached;
    }

    public static void refresh(Player player) {
        cached = InventoryScanner.scan(player);
    }

    public static void clear() {
        cached = Map.of();
    }
}
