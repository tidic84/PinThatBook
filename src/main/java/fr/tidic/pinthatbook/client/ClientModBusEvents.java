package fr.tidic.pinthatbook.client;

import fr.tidic.pinthatbook.PinThatBook;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(modid = PinThatBook.MODID, value = Dist.CLIENT)
public class ClientModBusEvents {

    @SubscribeEvent
    static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(KeyBindings.PIN);
        event.register(KeyBindings.NEXT_PAGE);
        event.register(KeyBindings.PREV_PAGE);
        event.register(KeyBindings.TOGGLE_VISIBILITY);
    }

    @SubscribeEvent
    static void registerLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(
                ResourceLocation.fromNamespaceAndPath(PinThatBook.MODID, "pinned_book"),
                new HudOverlay()
        );
    }
}
