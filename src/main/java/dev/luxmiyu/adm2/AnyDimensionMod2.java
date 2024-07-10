package dev.luxmiyu.adm2;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnyDimensionMod2 implements ModInitializer {
	public static final String MOD_ID = "adm2";
    public static final Logger LOGGER = LoggerFactory.getLogger("adm2");

	public static void log(String message) {
		LOGGER.info(message);
	}

	@Override
	public void onInitialize() {

	}
}