package dev.luxmiyu.adm2.item;

import dev.luxmiyu.adm2.util.Adm2Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ListWandItem extends WandItem {
    public ListWandItem(Settings settings) {
        super(settings, "tooltip.adm2.list_wand", 0xff99be);
    }

    private void listBlocks(World world, PlayerEntity player, BlockPos pos, List<Block> blocks) {
        int x = 0;
        int z = 0;
        final int width = 20;

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

        player.teleport(pos.getX(), pos.getY() + 1, pos.getZ());

        player.sendMessage(Adm2Util.textReplaceable("message.adm2.list_blocks", String.valueOf(blocks.size())), true);
    }

    private void listPortals(World world, PlayerEntity player, BlockPos pos, List<Block> blocks) {
        int x = 0;
        int z = 0;
        final int width = 20; // number of portals per row
        final int offset = 6; // space between portal spawns

        // place portals
        for (Block block : blocks) {
            placePortal(world, pos.add(x * offset, 0, z * offset), block);

            x++;

            if (x >= width) {
                x = 0;
                z++;
            }
        }

        player.teleport(pos.getX(), pos.getY() + 5, pos.getZ());

        player.sendMessage(Adm2Util.textReplaceable("message.adm2.list_portals", String.valueOf(blocks.size())), true);
    }

    private void placePortalSpace(World world, BlockPos pos) {
        BlockState floorBlock = Blocks.QUARTZ_BRICKS.getDefaultState();
        BlockState airBlock = Blocks.AIR.getDefaultState();

        for (int i = -1; i < 6; i++) {
            for (int j = -1; j < 6; j++) {
                world.setBlockState(pos.add(i, 0, j), floorBlock);

                for (int k = 1; k < 7; k++) {
                    world.setBlockState(pos.add(i, k, j), airBlock);
                }
            }
        }
    }

    private void placePortal(World world, BlockPos pos, Block block) {
        // #####
        // #   #
        // #   #
        // #   #
        // O####  O = pos

        placePortalSpace(world, pos.add(0, -1, 0));

        BlockState frameBlock = block.getDefaultState();
        BlockState airBlock = Blocks.AIR.getDefaultState();

        if (block instanceof LeavesBlock) {
            frameBlock = frameBlock.with(LeavesBlock.PERSISTENT, true);
        }

        BlockPos[] framePositions = {
            pos.add(0, 0, 0),
            pos.add(1, 0, 0),
            pos.add(2, 0, 0),
            pos.add(3, 0, 0),
            pos.add(4, 0, 0),

            pos.add(0, 1, 0),
            pos.add(4, 1, 0),

            pos.add(0, 2, 0),
            pos.add(4, 2, 0),

            pos.add(0, 3, 0),
            pos.add(4, 3, 0),

            pos.add(0, 4, 0),
            pos.add(1, 4, 0),
            pos.add(2, 4, 0),
            pos.add(3, 4, 0),
            pos.add(4, 4, 0),
        };

        BlockPos[] airPositions = {
            pos.add(1, 1, 0),
            pos.add(2, 1, 0),
            pos.add(3, 1, 0),

            pos.add(1, 2, 0),
            pos.add(2, 2, 0),
            pos.add(3, 2, 0),

            pos.add(1, 3, 0),
            pos.add(2, 3, 0),
            pos.add(3, 3, 0),
        };

        for (BlockPos p : framePositions) {
            world.setBlockState(p, frameBlock);
        }

        for (BlockPos p : airPositions) {
            world.setBlockState(p, airBlock);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (world.isClient()) {
            return TypedActionResult.success(player.getStackInHand(hand));
        }

        List<Block> blocks = Adm2Util.getAllValidBlocks(world.getServer());
        BlockPos pos = player.getBlockPos();

        if (player.isSneaking()) {
            listPortals(world, player, pos, blocks);
        } else {
            listBlocks(world, player, pos, blocks);
        }

        return TypedActionResult.success(player.getStackInHand(hand));
    }
}
