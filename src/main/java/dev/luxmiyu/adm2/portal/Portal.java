package dev.luxmiyu.adm2.portal;

import dev.luxmiyu.adm2.Adm2;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

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

    public static ServerWorld getDimensionWorld(MinecraftServer server, Block block) {
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

        int randomIndex = Adm2.RANDOM.nextBetween(0, blocks.size() - 1);
        return blocks.get(randomIndex);
    }
}
