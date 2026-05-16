package fr.tidic.pinthatbook;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = PinThatBook.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = PinThatBook.MODID, value = Dist.CLIENT)
public class PinThatBookClient {
    public PinThatBookClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        PinThatBook.LOGGER.info("PinThatBook client setup");
    }
}
