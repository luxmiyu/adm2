package dev.luxmiyu.adm2.fabric;

import net.fabricmc.api.ModInitializer;

import dev.luxmiyu.adm2.Adm2;

public final class Adm2Fabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Adm2.init();
    }
}
