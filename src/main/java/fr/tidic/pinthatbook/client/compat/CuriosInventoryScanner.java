package fr.tidic.pinthatbook.client.compat;

import fr.tidic.pinthatbook.client.ExternalInventoryScanners;
import fr.tidic.pinthatbook.client.InventoryScanner;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Map;

public class CuriosInventoryScanner implements ExternalInventoryScanners.Scanner {
    @Override
    public void scan(Player player, Map<Item, Integer> counts, Map<ItemStack, Boolean> visited) {
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> scanHandler(handler, counts, visited));
    }

    private static void scanHandler(ICuriosItemHandler handler, Map<Item, Integer> counts, Map<ItemStack, Boolean> visited) {
        for (ICurioStacksHandler slot : handler.getCurios().values()) {
            IDynamicStackHandler stacks = slot.getStacks();
            for (int i = 0; i < stacks.getSlots(); i++) {
                InventoryScanner.addStack(counts, stacks.getStackInSlot(i), visited);
            }
        }
    }
}
