package fr.tidic.pinthatbook.client;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public final class InventoryScanner {
    private InventoryScanner() {}

    public static Map<Item, Integer> scan(Player player) {
        Map<Item, Integer> counts = new HashMap<>();
        Map<ItemStack, Boolean> visited = new IdentityHashMap<>();
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            addStack(counts, player.getInventory().getItem(i), visited);
        }
        ExternalInventoryScanners.scanAll(player, counts, visited);
        return counts;
    }

    public static void addStack(Map<Item, Integer> counts, ItemStack stack, Map<ItemStack, Boolean> visited) {
        if (stack.isEmpty() || visited.put(stack, Boolean.TRUE) != null) return;
        counts.merge(stack.getItem(), stack.getCount(), Integer::sum);
        IItemHandler handler = stack.getCapability(Capabilities.ItemHandler.ITEM);
        if (handler == null) return;
        for (int i = 0; i < handler.getSlots(); i++) {
            addStack(counts, handler.getStackInSlot(i), visited);
        }
    }
}
