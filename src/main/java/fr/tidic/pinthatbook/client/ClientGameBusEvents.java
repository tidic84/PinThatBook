package fr.tidic.pinthatbook.client;

import fr.tidic.pinthatbook.PinThatBook;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = PinThatBook.MODID, value = Dist.CLIENT)
public class ClientGameBusEvents {

    @SubscribeEvent
    static void onJoin(ClientPlayerNetworkEvent.LoggingIn event) {
        PinPersistence.load();
    }

    @SubscribeEvent
    static void onTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;

        if (KeyBindings.PIN.consumeClick()) {
            Player p = mc.player;
            ItemStack stack = p.getMainHandItem();
            if (stack.isEmpty()) stack = p.getOffhandItem();
            PinnedBookState.pinFrom(stack);
        }
        if (KeyBindings.TOGGLE_VISIBILITY.consumeClick()) {
            PinnedBookState.toggleVisible();
        }
        while (KeyBindings.NEXT_PAGE.consumeClick()) PinnedBookState.nextPage();
        while (KeyBindings.PREV_PAGE.consumeClick()) PinnedBookState.prevPage();
    }
}
