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
     * Get layers of {@code BlockPos} up to a certain {@code radius} from the center.
     * Each layer contains edge positions from the center.
     * @param radius the maximum radius
     * @return an array of layers, each layer containing edge positions
     */
    public BlockPos[][] getEdgeLayers(int radius) {
        int layerCount = radius + 1;

        BlockPos[][] layers = new BlockPos[layerCount][];
        List<List<BlockPos>> lists = new ArrayList<>(layerCount);

        for (int i = 0; i <= radius; i++) {
            lists.add(new ArrayList<>());
        }

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    int layer = max(Math.abs(x), Math.abs(y), Math.abs(z));
                    lists.get(layer).add(new BlockPos(center.getX() + x, center.getY() + y, center.getZ() + z));
                }
            }
        }

        for (int i = 0; i < layerCount; i++) {
            layers[i] = lists.get(i).toArray(new BlockPos[0]);
        }

        return layers;
    }
}
