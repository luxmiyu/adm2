package dev.luxmiyu.adm2.portal;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public record BoxArea(BlockPos center) {
    public BlockPos[] getPositions(int radius) {
        int minX = center.getX() - radius;
        int minY = center.getY() - radius;
        int minZ = center.getZ() - radius;
        int maxX = center.getX() + radius;
        int maxY = center.getY() + radius;
        int maxZ = center.getZ() + radius;

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

//    public BlockPos[] getEdgePositions(int radius) {
//        int minX = center.getX() - radius;
//        int minY = center.getY() - radius;
//        int minZ = center.getZ() - radius;
//        int maxX = center.getX() + radius;
//        int maxY = center.getY() + radius;
//        int maxZ = center.getZ() + radius;
//
//        List<BlockPos> list = new ArrayList<>();
//
//        for (int x = minX; x <= maxX; x++) {
//            for (int y = minY; y <= maxY; y++) {
//                for (int z = minZ; z <= maxZ; z++) {
//                    if (x == minX || x == maxX || y == minY || y == maxY || z == minZ || z == maxZ) {
//                        list.add(new BlockPos(x, y, z));
//                    }
//                }
//            }
//        }
//
//        return list.toArray(new BlockPos[0]);
//    }

    public boolean isAir(World world, int radius) {
        BlockPos[] positions = getPositions(radius);

        for (BlockPos pos : positions) {
            if (!world.getBlockState(pos).isAir()) {
                return false;
            }
        }

        return true;
    }

    private int max(int... ints) {
        int max = Integer.MIN_VALUE;
        for (int i : ints) {
            if (i > max) {
                max = i;
            }
        }
        return max;
    }

    /**
     * Get edge positions for each radius from 0, like onion layers.
     * @param radius the maximum radius
     * @return an array of edge positions for each radius from 0
     */
    public BlockPos[][] getEdges(int radius) {
        BlockPos[][] edges = new BlockPos[radius + 1][];
        List<BlockPos>[] lists = new List[radius + 1];

        for (int i = 0; i <= radius; i++) {
            lists[i] = new ArrayList<>();
        }

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    int edge = max(Math.abs(x), Math.abs(y), Math.abs(z));
                    lists[edge].add(new BlockPos(center.getX() + x, center.getY() + y, center.getZ() + z));
                }
            }
        }

        for (int i = 0; i <= radius; i++) {
            edges[i] = lists[i].toArray(new BlockPos[0]);
        }

        return edges;
    }
}
