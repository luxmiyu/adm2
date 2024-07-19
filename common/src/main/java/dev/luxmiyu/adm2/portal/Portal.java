package dev.luxmiyu.adm2.portal;

import dev.luxmiyu.adm2.Adm2;
import dev.luxmiyu.adm2.block.AnyDimensionalPortalBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Portal {
    public static String dimensionIdFromBlock(Block block) {
        Identifier id = Registries.BLOCK.getId(block);
        return id.getNamespace() + "__" + id.getPath();
    }

    public static ServerWorld getDimensionWorld(MinecraftServer server, String dimensionId) {
        RegistryKey<World> targetDimensionKey = RegistryKey.of(RegistryKeys.WORLD, Identifier.of(Adm2.MOD_ID, dimensionId));
        return server.getWorld(targetDimensionKey);
    }

    public static ServerWorld getDimensionWorld(@NotNull MinecraftServer server, Block block) {
        String dimensionId = dimensionIdFromBlock(block);
        return getDimensionWorld(server, dimensionId);
    }

    public static boolean isDimensionLoaded(MinecraftServer server, String dimensionId) {
        return getDimensionWorld(server, dimensionId) != null;
    }

    public static boolean isDimensionLoaded(MinecraftServer server, Block block) {
        String dimensionId = dimensionIdFromBlock(block);
        return isDimensionLoaded(server, dimensionId);
    }

    public static List<Block> getAllValidBlocks(MinecraftServer server) {
        List<Block> blocks = new ArrayList<>();

        for (Block block : Registries.BLOCK) {
            if (isDimensionLoaded(server, dimensionIdFromBlock(block))) {
                blocks.add(block);
            }
        }

        return blocks;
    }

    public static Block getRandomPortalBlock(MinecraftServer server) {
        List<Block> blocks = getAllValidBlocks(server);

        int randomIndex = Adm2.RANDOM.nextInt(0, blocks.size() - 1);
        return blocks.get(randomIndex);
    }

    public static BlockPos[] getFramePositions(BlockPos origin, Direction.Axis horizontal) {
        final Direction L = horizontal == Direction.Axis.X ? Direction.NORTH : Direction.WEST;
        final Direction R = horizontal == Direction.Axis.X ? Direction.SOUTH : Direction.EAST;
        final Direction U = Direction.UP;

        // #####
        // #   #
        // #   #
        // #   #
        // ##O##  O = origin
        return new BlockPos[]{
            origin.offset(L, 2).offset(U, 0),
            origin.offset(L, 1).offset(U, 0),
            origin.offset(L, 0).offset(U, 0),
            origin.offset(R, 1).offset(U, 0),
            origin.offset(R, 2).offset(U, 0),

            origin.offset(L, 2).offset(U, 1),
            origin.offset(R, 2).offset(U, 1),

            origin.offset(L, 2).offset(U, 2),
            origin.offset(R, 2).offset(U, 2),

            origin.offset(L, 2).offset(U, 3),
            origin.offset(R, 2).offset(U, 3),

            origin.offset(L, 2).offset(U, 4),
            origin.offset(L, 1).offset(U, 4),
            origin.offset(L, 0).offset(U, 4),
            origin.offset(R, 1).offset(U, 4),
            origin.offset(R, 2).offset(U, 4),
        };
    }

    public static BlockPos[] getPortalPositions(BlockPos origin, Direction.Axis horizontal) {
        final Direction L = horizontal == Direction.Axis.X ? Direction.NORTH : Direction.WEST;
        final Direction R = horizontal == Direction.Axis.X ? Direction.SOUTH : Direction.EAST;
        final Direction U = Direction.UP;

        // ###
        // ###
        // ###
        //  O   O = origin
        return new BlockPos[]{
            origin.offset(L, 1).offset(U, 1),
            origin.offset(L, 0).offset(U, 1),
            origin.offset(R, 1).offset(U, 1),

            origin.offset(L, 1).offset(U, 2),
            origin.offset(L, 0).offset(U, 2),
            origin.offset(R, 1).offset(U, 2),

            origin.offset(L, 1).offset(U, 3),
            origin.offset(L, 0).offset(U, 3),
            origin.offset(R, 1).offset(U, 3),
        };
    }

    public static void placePortalFloor(World world, BlockPos blockPos, BlockState blockState) {
        for (int k = -1; k < 5; k++) {
            for (int i = -2; i < 3; i++) {
                for (int j = -2; j < 3; j++) {
                    world.setBlockState(blockPos.add(i, k, j), Blocks.AIR.getDefaultState());
                }
            }
        }

        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                world.setBlockState(blockPos.add(i, -1, j), blockState);
            }
        }
    }

    public static void placePortalFrame(World world, BlockPos blockPos, Direction.Axis horizontal, BlockState blockState) {
        BlockPos[] framePositions = getFramePositions(blockPos, horizontal);
        BlockPos[] portalPositions = getPortalPositions(blockPos, horizontal);
        BlockState airState = Blocks.AIR.getDefaultState();

        for (BlockPos pos : framePositions) {
            world.setBlockState(pos, blockState);
        }

        for (BlockPos pos : portalPositions) {
            world.setBlockState(pos, airState);
        }
    }

    public static void placePortalBlocks(World world, BlockPos blockPos, Direction.Axis horizontal) {
        BlockPos[] portalPositions = getPortalPositions(blockPos, horizontal);

        Block portalBlock = Adm2.ANY_DIMENSIONAL_PORTAL_BLOCK.get();
        BlockState portalState = portalBlock.getDefaultState().with(AnyDimensionalPortalBlock.AXIS, horizontal);

        for (BlockPos pos : portalPositions) {
            world.setBlockState(pos, portalState);
        }
    }

    public static void generatePortal(World world, BlockPos blockPos, Direction.Axis horizontal, BlockState frameState, BlockState floorState) {
        placePortalFloor(world, blockPos, floorState);
        placePortalFrame(world, blockPos, horizontal, frameState);
        placePortalBlocks(world, blockPos, horizontal);
    }

    public static boolean isValidPortalFrame(World world, BlockPos blockPos, Direction.Axis horizontalAxis) {
        BlockState blockState = world.getBlockState(blockPos);
        BlockState airState = Blocks.AIR.getDefaultState();
        if (!Portal.isDimensionLoaded(world.getServer(), blockState.getBlock())) return false;

        BlockPos[] framePositions = getFramePositions(blockPos, horizontalAxis);
        BlockPos[] airPositions = getPortalPositions(blockPos, horizontalAxis);

        for (BlockPos pos : framePositions) {
            if (world.getBlockState(pos) != blockState) return false;
        }

        for (BlockPos pos : airPositions) {
            if (world.getBlockState(pos) != airState) return false;
        }

        return true;
    }

    public static boolean isPortalReplaceable(Block block) {
        return block == Blocks.AIR || block == Adm2.ANY_DIMENSIONAL_PORTAL_BLOCK.get();
    }

    @Nullable
    public static PortalArea findPortalArea(World world, BlockPos blockPos, Direction.Axis horizontalAxis) {
        Block originBlock = world.getBlockState(blockPos).getBlock();
        if (!isPortalReplaceable(originBlock)) return null;

        BlockPos bottom;
        BlockPos top;
        BlockPos left;
        BlockPos right;

        final int max = 20;

        int i = 0;
        while (isPortalReplaceable(world.getBlockState(blockPos.offset(Direction.DOWN, i)).getBlock())) {
            i++;
            if (i > max) return null;
        }
        bottom = blockPos.offset(Direction.DOWN, i - 1);

        i = 0;
        while (isPortalReplaceable(world.getBlockState(blockPos.offset(Direction.UP, i)).getBlock())) {
            i++;
            if (i > max) return null;
        }
        top = blockPos.offset(Direction.UP, i - 1);

        i = 0;
        while (isPortalReplaceable(world.getBlockState(blockPos.offset(horizontalAxis, -i)).getBlock())) {
            i++;
            if (i > max) return null;
        }
        left = blockPos.offset(horizontalAxis, -i + 1);

        i = 0;
        while (isPortalReplaceable(world.getBlockState(blockPos.offset(horizontalAxis, i)).getBlock())) {
            i++;
            if (i > max) return null;
        }
        right = blockPos.offset(horizontalAxis, i - 1);

        int minX = left.getX();
        int minY = bottom.getY();
        int minZ = left.getZ();
        int maxX = right.getX();
        int maxY = top.getY();
        int maxZ = right.getZ();

        int diffX = Math.abs(minX - maxX);
        int diffY = Math.abs(minY - maxY);
        int diffZ = Math.abs(minZ - maxZ);
        if (diffX > max || diffY > max || diffZ > max) return null;

        PortalArea area = new PortalArea(minX, minY, minZ, maxX, maxY, maxZ);
        return area.isReplaceable(world) ? area : null;
    }

    // todo
    public static void attemptTeleport(PlayerEntity player) {

    }
}
