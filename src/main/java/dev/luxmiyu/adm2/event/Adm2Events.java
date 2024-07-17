package dev.luxmiyu.adm2.event;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.luxmiyu.adm2.Adm2;
import dev.luxmiyu.adm2.command.Adm2Command;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static dev.luxmiyu.adm2.Adm2.*;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Adm2Events {
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        Adm2.LOGGER.info("Server started!");
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<ServerCommandSource> dispatcher = event.getDispatcher();

        dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("adm2")
            .requires(commandSourceStack -> commandSourceStack.hasPermissionLevel(2)).executes(Adm2Command::adm2Execute));
    }
}
