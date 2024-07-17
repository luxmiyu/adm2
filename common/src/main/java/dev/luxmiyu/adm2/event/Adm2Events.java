package dev.luxmiyu.adm2.event;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.luxmiyu.adm2.Adm2;
import dev.luxmiyu.adm2.command.Adm2Command;
import net.minecraft.server.command.ServerCommandSource;

public class Adm2Events {
    public static void init() {
        Adm2.LOGGER.info("Registering events");

        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
            dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("adm2").executes(Adm2Command::adm2Execute));
        });
    }
}
