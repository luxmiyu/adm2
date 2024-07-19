package dev.luxmiyu.adm2.portal;

import dev.luxmiyu.adm2.Adm2;
import dev.luxmiyu.adm2.block.AnyDimensionalPortalBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record PortalArea(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
    public BlockPos[] getPositions() {
        List<BlockPos> list = new ArrayList<>();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    list.add(new BlockPos(x, y, z));
                }
            }
        }

        return list.toArray(new BlockPos[0]);
    }

    public boolean isLit(World world) {
        for (BlockPos pos : getPositions()) {
            if (world.getBlockState(pos).getBlock() != Adm2.ANY_DIMENSIONAL_PORTAL_BLOCK.get()) {
                return false;
            }
        }
        return true;
    }

    public boolean isAir(World world) {
        for (BlockPos pos : getPositions()) {
            if (world.getBlockState(pos).getBlock() != Blocks.AIR) {
                return false;
            }
        }
        return true;
    }

    public boolean isReplaceable(World world) {
        for (BlockPos pos : getPositions()) {
            Block block = world.getBlockState(pos).getBlock();
            if (block != Blocks.AIR && block != Adm2.ANY_DIMENSIONAL_PORTAL_BLOCK.get()) {
                return false;
            }
        }
        return true;
    }

    public Direction.Axis getAxis() {
        if (minX == maxX) {
            return Direction.Axis.X;
        } else if (minZ == maxZ) {
            return Direction.Axis.Z;
        } else {
            throw new IllegalStateException("PortalArea is not aligned to a horizontal axis");
        }
    }

    public void placeIn(World world) {
        for (BlockPos pos : getPositions()) {
            world.setBlockState(pos, Adm2.ANY_DIMENSIONAL_PORTAL_BLOCK.get().getDefaultState().with(AnyDimensionalPortalBlock.AXIS, getAxis()));
        }
    }

    @Nullable
    public Block getFrame(World world) {
        // get the 4 sides of the portal blocks
        Iterable<BlockPos> floorPositions = BlockPos.iterate(minX, minY, minZ, maxX, minY, maxZ);
        Iterable<BlockPos> ceilingPositions = BlockPos.iterate(minX, maxY, minZ, maxX, maxY, maxZ);
        Iterable<BlockPos> minSidePositions = BlockPos.iterate(minX, minY, minZ, minX, maxY, minZ);
        Iterable<BlockPos> maxSidePositions = BlockPos.iterate(maxX, minY, maxZ, maxX, maxY, maxZ);

        // get the frame blocks next to the portal blocks
        Block frameBlock = null;

        for (BlockPos pos : floorPositions) {
            Block block = world.getBlockState(pos.offset(Direction.DOWN)).getBlock();

            if (frameBlock == null) frameBlock = block;
            if (frameBlock == Blocks.AIR) return null;
            if (frameBlock != block) return null;
        }

        for (BlockPos pos : ceilingPositions) {
            Block block = world.getBlockState(pos.offset(Direction.UP)).getBlock();

            if (frameBlock == null) frameBlock = block;
            if (frameBlock == Blocks.AIR) return null;
            if (frameBlock != block) return null;
        }

        final Direction L = getAxis() == Direction.Axis.X ? Direction.NORTH : Direction.WEST;
        final Direction R = getAxis() == Direction.Axis.X ? Direction.SOUTH : Direction.EAST;

        for (BlockPos pos : minSidePositions) {
            Block block = world.getBlockState(pos.offset(L)).getBlock();

            if (frameBlock == null) frameBlock = block;
            if (frameBlock == Blocks.AIR) return null;
            if (frameBlock != block) return null;
        }

        for (BlockPos pos : maxSidePositions) {
            Block block = world.getBlockState(pos.offset(R)).getBlock();

            if (frameBlock == null) frameBlock = block;
            if (frameBlock == Blocks.AIR) return null;
            if (frameBlock != block) return null;
        }

        return frameBlock;
    }
}
