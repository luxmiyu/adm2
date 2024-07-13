package dev.luxmiyu.adm2.item;

import dev.luxmiyu.adm2.util.Adm2Util;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class BlinkWandItem extends WandItem {
    public BlinkWandItem(Settings settings) {
        super(settings, "tooltip.adm2.blink_wand", 0xffb200);
    }

    private void teleportPlayerToDimension(PlayerEntity player, Block block) {
        BlockPos pos = player.getBlockPos();
        String blockName = block.getName().getString();

        MinecraftServer server = player.getServer();

        if (server == null) {
            player.sendMessage(Adm2Util.textReplaceable("message.adm2.blink_fail", blockName), true);
            return;
        }

        ServerWorld world = Adm2Util.getDimensionWorld(server, block);
        World playerWorld = player.getWorld();

        if (world == null || playerWorld == null) {
            player.sendMessage(Adm2Util.textReplaceable("message.adm2.blink_fail", blockName), true);
            return;
        }

        String dimensionName = "the " + blockName + " Dimension";
        boolean sameDimension = playerWorld.getRegistryKey().equals(world.getRegistryKey());
        if (sameDimension) {
            world = player.getServer().getWorld(ServerWorld.OVERWORLD);
            dimensionName = "the Overworld";
        }

        player.teleport(world, pos.getX(), pos.getY(), pos.getZ(), Set.of(), player.getYaw(), player.getPitch());
        player.sendMessage(Adm2Util.textReplaceable("message.adm2.blink_success", dimensionName, Adm2Util.posToString(pos)), true);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (world.isClient()) {
            return TypedActionResult.success(player.getStackInHand(hand));
        }

        Block block = Adm2Util.getRandomPortalBlock(world.getServer());

        teleportPlayerToDimension(player, block);

        return TypedActionResult.success(player.getStackInHand(hand));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().isClient()) {
            return ActionResult.SUCCESS;
        }

        PlayerEntity player = context.getPlayer();
        Block block = context.getWorld().getBlockState(context.getBlockPos()).getBlock();

        if (player == null) {
            return ActionResult.PASS;
        }

        teleportPlayerToDimension(player, block);

        return ActionResult.SUCCESS;
    }
}
