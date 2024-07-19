package dev.luxmiyu.adm2.item;

import dev.luxmiyu.adm2.portal.Portal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;

public class ListWandItem extends WandItem {
    public ListWandItem(Settings settings) {
        super(settings, "tooltip.adm2.list_wand", 0xff99be);
    }

    private void listBlocks(World world, PlayerEntity player, BlockPos pos, List<Block> blocks) {
        int x = 0;
        int z = 0;
        final int width = (int) Math.sqrt(blocks.size());

        for (Block block : blocks) {
            BlockState blockState = block.getDefaultState();
            BlockState floorState = Blocks.BARRIER.getDefaultState();
            BlockState airState = Blocks.AIR.getDefaultState();

            if (block instanceof LeavesBlock) {
                blockState = blockState.with(LeavesBlock.PERSISTENT, true);
            }

            world.setBlockState(pos.add(x, 0, z), blockState);
            world.setBlockState(pos.add(x, -1, z), floorState);
            world.setBlockState(pos.add(x, -2, z), airState);

            x++;

            if (x >= width) {
                x = 0;
                z++;
            }
        }

        player.teleport(pos.getX(), pos.getY() + 1, pos.getZ(), true);

        player.sendMessage(Text.translatable("message.adm2.list_blocks", String.valueOf(blocks.size())), true);
    }

    private void listPortals(World world, PlayerEntity player, BlockPos pos, List<Block> blocks) {
        int x = 0;
        int z = 0;
        final int width = (int) Math.sqrt(blocks.size());

        // --#----#--
        //
        //
        //
        //
        // --#----#--

        for (Block block : blocks) {
            BlockState frameState = block.getDefaultState();
            BlockState floorState = Blocks.QUARTZ_BRICKS.getDefaultState();

            if (block instanceof LeavesBlock) {
                frameState = frameState.with(LeavesBlock.PERSISTENT, true);
            }

            BlockPos portalPos = pos.add(x * 5, 0, z * 5);

            Portal.generatePortalWithFloor(world, portalPos, Direction.Axis.Z, frameState, floorState);

            x++;

            if (x >= width) {
                x = 0;
                z++;
            }
        }

        player.teleport(pos.getX(), pos.getY() + 5.5d, pos.getZ(), true);

        player.sendMessage(Text.translatable("message.adm2.list_portals", String.valueOf(blocks.size())), true);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (world.isClient()) {
            return TypedActionResult.success(player.getStackInHand(hand));
        }

        List<Block> blocks = Portal.getAllValidBlocks(world.getServer());
        BlockPos pos = player.getBlockPos();

        if (player.isSneaking()) {
            listPortals(world, player, pos, blocks);
        } else {
            listBlocks(world, player, pos, blocks);
        }

        return TypedActionResult.success(player.getStackInHand(hand));
    }
}