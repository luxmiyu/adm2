package dev.luxmiyu.adm2.util;

import dev.luxmiyu.adm2.Adm2;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.*;

public class Adm2Util {
    public static final Random random = Random.create();

    public static boolean isModLoaded(String modId) {
        Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(modId);
        return modContainer.isPresent();
    }

    public static Block getRandomPortalBlock(MinecraftServer server) {
        List<Block> blocks = getAllValidBlocks(server);

        int randomIndex = random.nextBetween(0, blocks.size() - 1);
        return blocks.get(randomIndex);
    }

    public static String posToString(BlockPos blockPos) {
        return "[" + blockPos.toShortString() + "]";
    }

    public static Text textReplaceable(String translationKey, String... replacements) {
        String text = Text.translatable(translationKey).getString();
        for (int i = 0; i < replacements.length; i++) {
            text = text.replace("{" + i + "}", replacements[i]);
        }
        return Text.of(text);
    }

    public static String dimensionIdFromBlock(Block block) {
        Identifier id = Registries.BLOCK.getId(block);
        return id.getNamespace() + "__" + id.getPath();
    }

    public static ServerWorld getDimensionWorld(MinecraftServer server, String dimensionId) {
        RegistryKey<World> targetDimensionKey = RegistryKey.of(RegistryKeys.WORLD, new Identifier(Adm2.MOD_ID, dimensionId));
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

    private static void registerPortal(Identifier dimensionId, Identifier blockId, boolean isImmersivePortalsLoaded) {
        Adm2.LOGGER.info("Registering portal: {} ({})", dimensionId, blockId.toString());

        if (!isImmersivePortalsLoaded) {
            CustomPortalBuilder.beginPortal()
                .destDimID(dimensionId)
                .frameBlock(blockId)
                .tintColor(13421772)
                .lightWithItem(Adm2.ANY_DIMENSIONAL_PORTAL_WAND)
                .registerPortalForced();
        } else {
            CustomPortalBuilder.beginPortal()
                .destDimID(dimensionId)
                .frameBlock(blockId)
                .tintColor(13421772)
                .lightWithItem(Adm2.ANY_DIMENSIONAL_PORTAL_WAND_CLASSIC)
                .registerPortalForced();
        }
    }

    private static boolean doesPortalLinkExist(Block frameBlock) {
        /*
            Can't use CustomPortalApiRegistry.getPortalLinkFromBase() because
            we force register multiple links for the same block.
            We have to iterate over the collection manually.
        */

        Collection<PortalLink> portalLinks = CustomPortalApiRegistry.getAllPortalLinks();

        for (PortalLink portalLink : portalLinks) {
            Identifier portalBlockId = portalLink.block;
            Identifier frameBlockId = Registries.BLOCK.getId(frameBlock);
            boolean blockMatch = portalBlockId.equals(frameBlockId);

            Identifier wandId = Registries.ITEM.getId(Adm2.ANY_DIMENSIONAL_PORTAL_WAND_CLASSIC);
            Identifier ignitionSourceId = portalLink.portalIgnitionSource.ignitionSourceID;
            boolean itemMatch = wandId.equals(ignitionSourceId);

            if (blockMatch && itemMatch) {
                return true;
            }
        }

        return false;
    }

    public static void onServerStart(MinecraftServer server) {
        Adm2.LOGGER.info("Server started");

        for (Block block : getAllValidBlocks(server)) {
            String dimensionId = dimensionIdFromBlock(block);
            if (!isDimensionLoaded(server, dimensionId)) {
                Adm2.LOGGER.info("Dimension not loaded: {}", dimensionId);
                continue;
            }

            if (!doesPortalLinkExist(block)) {
                registerPortal(new Identifier(Adm2.MOD_ID, dimensionId), Registries.BLOCK.getId(block), isModLoaded("immersive_portals"));
            } else {
                Adm2.LOGGER.info("Portal link already exists for block: {}", dimensionId);
            }
        }
    }
}
