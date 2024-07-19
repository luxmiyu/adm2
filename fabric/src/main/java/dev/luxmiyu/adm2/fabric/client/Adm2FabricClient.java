package dev.luxmiyu.adm2.fabric.client;

import dev.luxmiyu.adm2.Adm2;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public final class Adm2FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(Adm2.ANY_DIMENSIONAL_PORTAL_BLOCK.get(), RenderLayer.getTranslucent());
    }
}
