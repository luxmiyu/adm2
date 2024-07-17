package dev.luxmiyu.adm2.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static dev.luxmiyu.adm2.Adm2.*;

public class Adm2Command {
    public static int adm2Execute(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        if (!source.isExecutedByPlayer()) return 0;

        ServerPlayerEntity player = source.getPlayer();
        if (player == null) return 0;
        if (!player.hasPermissionLevel(2)) return 0;

        Item[] wands = new Item[] {
            ANY_DIMENSIONAL_PORTAL_WAND.get(),
            ANY_DIMENSIONAL_PORTAL_WAND_CLASSIC.get(),
            ANY_DIMENSIONAL_TELEPORT_WAND.get(),
            ANY_DIMENSIONAL_SUMMON_WAND.get(),
            ANY_DIMENSIONAL_RANDOM_WAND.get(),
            ANY_DIMENSIONAL_LIST_WAND.get(),
            ANY_DIMENSIONAL_BLINK_WAND.get(),
            ANY_DIMENSIONAL_DEBUG_WAND.get(),
            ANY_DIMENSIONAL_USELESS_WAND.get()
        };

        for (Item wand : wands) {
            player.giveItemStack(new ItemStack(wand));
        }

        source.sendFeedback(() -> Text.translatable("message.adm2.command_success"), true);

        return 1;
    }
}
