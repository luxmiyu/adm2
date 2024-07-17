package dev.luxmiyu.adm2.command;

import com.mojang.brigadier.context.CommandContext;
import dev.luxmiyu.adm2.Adm2;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class Adm2Command {
    public static int adm2Execute(CommandContext<ServerCommandSource> objectCommandContext) {
        try {
            ServerCommandSource source = objectCommandContext.getSource();

            PlayerEntity player = source.getPlayer();

            if (player == null) {
                source.sendFeedback(() -> Text.translatable("message.adm2.command_not_player"), false);
                return 0;
            }

            if (!player.hasPermissionLevel(2)) {
                source.sendFeedback(() -> Text.translatable("message.adm2.command_no_permission"), false);
                return 0;
            }

            Item[] wandItems = {
                Adm2.ANY_DIMENSIONAL_PORTAL_WAND.get(),
                Adm2.ANY_DIMENSIONAL_PORTAL_WAND_CLASSIC.get(),
                Adm2.ANY_DIMENSIONAL_TELEPORT_WAND.get(),
                Adm2.ANY_DIMENSIONAL_SUMMON_WAND.get(),
                Adm2.ANY_DIMENSIONAL_RANDOM_WAND.get(),
                Adm2.ANY_DIMENSIONAL_LIST_WAND.get(),
                Adm2.ANY_DIMENSIONAL_BLINK_WAND.get(),
                Adm2.ANY_DIMENSIONAL_DEBUG_WAND.get(),
                Adm2.ANY_DIMENSIONAL_USELESS_WAND.get(),
            };

            for (Item item : wandItems) {
                player.giveItemStack(new ItemStack(item));
            }

            source.sendFeedback(() -> Text.translatable("message.adm2.command_success"), true);

            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
