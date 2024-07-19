package dev.luxmiyu.adm2.neoforge;

import dev.luxmiyu.adm2.Adm2;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Adm2.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class Adm2NeoForgeEvents {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        // This will run the operation on the main thread, ensuring thread safety.
        event.enqueueWork(() -> {
            RenderLayers.setRenderLayer(Adm2.ANY_DIMENSIONAL_PORTAL_BLOCK.get(), RenderLayer.getTranslucent());
        });
    }
}
