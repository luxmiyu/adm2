package dev.luxmiyu.adm2.portal;

import dev.luxmiyu.adm2.Adm2;
import dev.luxmiyu.adm2.block.AnyDimensionalPortalBlock;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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

    public static void placePortalBase(World world, BlockPos blockPos, Direction.Axis horizontal, BlockState blockState) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                world.setBlockState(blockPos.add(i, 0, j), blockState);
            }
        }

        // place a platform for falling blocks
        Block block = blockState.getBlock();
        if (block instanceof FallingBlock) {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    world.setBlockState(blockPos.add(i, -1, j), Adm2.ANY_DIMENSIONAL_BLOCK.get().getDefaultState());
                }
            }

            for (int i = -2; i < 3; i++) {
                Direction.Axis sideAxis = horizontal == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
                BlockPos pos = blockPos.offset(sideAxis, i);
                world.setBlockState(pos.offset(Direction.DOWN), Adm2.ANY_DIMENSIONAL_BLOCK.get().getDefaultState());
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

    public static void generatePortalWithFloor(World world, BlockPos blockPos, Direction.Axis horizontal, BlockState frameState, BlockState floorState) {
        placePortalFloor(world, blockPos, floorState);
        placePortalFrame(world, blockPos, horizontal, frameState);
        placePortalBlocks(world, blockPos, horizontal);
    }

    public static void generatePortalWithBase(World world, BlockPos blockPos, Direction.Axis horizontal, BlockState frameState) {
        placePortalBase(world, blockPos, horizontal, frameState);
        placePortalFrame(world, blockPos, horizontal, frameState);
        placePortalBlocks(world, blockPos, horizontal);
    }

    public static boolean isPortalReplaceable(Block block) {
        return block == Blocks.AIR || block == Adm2.ANY_DIMENSIONAL_PORTAL_BLOCK.get();
    }

    @Nullable
    public static PortalArea findPortalArea(World world, BlockPos blockPos, Direction.Axis horizontalAxis) {
        Block originBlock = world.getBlockState(blockPos).getBlock();
        if (!isPortalReplaceable(originBlock)) return null;

        Direction.Axis sideAxis = horizontalAxis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;

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
        while (isPortalReplaceable(world.getBlockState(blockPos.offset(sideAxis, -i)).getBlock())) {
            i++;
            if (i > max) return null;
        }
        left = blockPos.offset(sideAxis, -i + 1);

        i = 0;
        while (isPortalReplaceable(world.getBlockState(blockPos.offset(sideAxis, i)).getBlock())) {
            i++;
            if (i > max) return null;
        }
        right = blockPos.offset(sideAxis, i - 1);

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
        return area.validate(world);
    }

    public static void portalIsNull(PlayerEntity player) {
        player.sendMessage(Text.translatable("message.adm2.portal_null"), true);
        Adm2.LOGGER.debug("Portal is null");
    }

    public static void worldIsNull(PlayerEntity player) {
        player.sendMessage(Text.translatable("message.adm2.world_null"), true);
        Adm2.LOGGER.debug("World is null");
    }

    @Nullable
    public static PortalArea findPortal(World world, BlockPos center, Block block) {
        BlockPos[][] layers = new BoxArea(center).getEdgeLayers(12);
        List<BlockPos> banned = new ArrayList<>(); // already checked

        for (BlockPos[] layer : layers) {
            for (BlockPos pos : layer) {
                if (banned.contains(pos)) continue;

                BlockState state = world.getBlockState(pos);
                if (state.getBlock() != Adm2.ANY_DIMENSIONAL_PORTAL_BLOCK.get()) continue;

                Direction.Axis axis = state.get(AnyDimensionalPortalBlock.AXIS);
                PortalArea area = findPortalArea(world, pos, axis);
                if (area == null) continue;

                Block targetBlock = area.getFrame(world);
                if (targetBlock == null || !area.isLit(world)) {
                    banned.addAll(Arrays.asList(area.getPositions()));
                    continue;
                }

                if (targetBlock == block) return area;
            }
        }

        return null;
    }

    public static BlockPos findEmptyArea(World world, BlockPos center, int radius) {
        BlockPos[][] layers = new BoxArea(center).getEdgeLayers(12);

        for (BlockPos[] layer : layers) {
            for (BlockPos pos : layer) {
                BoxArea box = new BoxArea(pos);
                if (box.isAir(world, radius)) {
                    return pos;
                }
            }
        }

        return center;
    }

    /**
     * Find the emptiest area around the center to place a portal
     * @return the center of the emptiest area
     */
    public static BlockPos findEmptyPortalArea(World world, BlockPos center) {
        BlockPos a = findEmptyArea(world, center, 2);
        BlockPos b = findEmptyArea(world, center, 1);

        if (!a.equals(center)) return a;
        if (!b.equals(center)) return b;

        return center;
    }

    /**
     * Check if the player is in a portal, validate the portal, and attempt to teleport the player
     */
    public static void attemptTeleport(PlayerEntity player) {
        World world = player.getWorld();
        if (world.isClient) return;

        Adm2.LOGGER.info("Attempting to teleport player: {}", player.getName());

        MinecraftServer server = world.getServer();
        if (server == null) return;

        BlockPos pos = player.getBlockPos();
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() != Adm2.ANY_DIMENSIONAL_PORTAL_BLOCK.get()) return;
        Direction.Axis axis = state.get(AnyDimensionalPortalBlock.AXIS);

        PortalArea area = findPortalArea(world, pos, axis);
        if (area == null || !area.isLit(world)) { portalIsNull(player); return; }

        Block targetBlock = area.getFrame(world);
        if (targetBlock == null) { portalIsNull(player); return; }

        BlockState targetState = targetBlock.getDefaultState();
        if (targetBlock instanceof LeavesBlock) {
            targetState = targetState.with(LeavesBlock.PERSISTENT, true);
        }

        ServerWorld targetWorld = getDimensionWorld(world.getServer(), targetBlock);
        final boolean goToOverworld = world == targetWorld;
        if (goToOverworld) targetWorld = world.getServer().getOverworld();

        if (targetWorld == null) { worldIsNull(player); return; }

        BlockPos targetPos = area.getCenterPos();

        PortalArea targetArea = findPortal(targetWorld, targetPos, targetBlock);

        Vec3d teleportPos;

        if (targetArea == null) {
            targetPos = findEmptyPortalArea(targetWorld, targetPos);

            // prevent the player from falling into the void
            if (!goToOverworld && targetPos.getY() < 4) targetPos = new BlockPos(targetPos.getX(), 4, targetPos.getZ());

            generatePortalWithBase(targetWorld, targetPos.offset(Direction.DOWN, 2), state.get(AnyDimensionalPortalBlock.AXIS), targetState);
            teleportPos = targetPos.toCenterPos();
            Adm2.LOGGER.info("Portal generated at {} in {}", targetPos, targetWorld.getRegistryKey().getValue());
        } else {
            teleportPos = targetArea.getCenterVec();
            Adm2.LOGGER.info("Portal found at {} in {}", teleportPos, targetWorld.getRegistryKey().getValue());
        }

        player.teleport(targetWorld, teleportPos.getX(), teleportPos.getY(), teleportPos.getZ(), Set.of(), player.getYaw(), player.getPitch());
    }
}
