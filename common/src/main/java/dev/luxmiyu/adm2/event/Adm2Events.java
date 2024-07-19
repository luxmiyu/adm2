package dev.luxmiyu.adm2.event;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.luxmiyu.adm2.Adm2;
import dev.luxmiyu.adm2.command.Adm2Command;
import dev.luxmiyu.adm2.portal.Portal;
import net.minecraft.block.Block;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Adm2Events {
    public static void init() {
        Adm2.LOGGER.info("Registering events");

        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
            dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("adm2").executes(Adm2Command::adm2Execute));
        });

        TickEvent.PLAYER_POST.register(player -> {
            World world = player.getWorld();
            if (world.isClient) return;

            BlockPos pos = player.getBlockPos();
            Block block = world.getBlockState(pos).getBlock();
            Identifier blockId = Registries.BLOCK.getId(block);
            Identifier portalId = Registries.BLOCK.getId(Adm2.ANY_DIMENSIONAL_PORTAL_BLOCK.get());

            if (blockId.equals(portalId)) {
                if (Adm2.TELEPORT_MANAGER.hasTeleported(player.getUuid())) return;

                player.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 30, 0, true, true));

                Adm2.TELEPORT_MANAGER.tick(player.getUuid());

                if (Adm2.TELEPORT_MANAGER.shouldTeleport(player.getUuid())) {
                    Portal.attemptTeleport(player);
                    Adm2.TELEPORT_MANAGER.onPlayerTeleport(player.getUuid());
                }
            } else {
                Adm2.TELEPORT_MANAGER.resetCooldown(player.getUuid());
                Adm2.TELEPORT_MANAGER.resetTeleported(player.getUuid());
            }
        });
    }
}
