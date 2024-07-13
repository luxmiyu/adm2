package dev.luxmiyu.adm2.item;

import dev.luxmiyu.adm2.Adm2;
import dev.luxmiyu.adm2.util.Adm2Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class DebugWandItem extends WandItem {
    public DebugWandItem(Settings settings) {
        super(settings, "tooltip.adm2.debug_wand", 0xda1f1f);
    }

    private void printBlocksConsole(BlockView world, BlockPos blockPos, String modId, boolean checkFullCube) {
        Adm2.LOGGER.info("");
        Adm2.LOGGER.info(checkFullCube ? "Printing full cube blocks at {} from {}" : "Printing all blocks at {}", Adm2Util.posToString(blockPos), modId);

        if (checkFullCube) {
            Adm2.LOGGER.info("Sneak while using the Debug Wand to print all blocks instead.");
        }
        Adm2.LOGGER.info("");

        StringBuilder blockIds = new StringBuilder();

        for (Block block : Registries.BLOCK) {
            BlockState defaultState = block.getDefaultState();
            String blockModId = Registries.BLOCK.getId(block).getNamespace();

            if (!blockModId.equals(modId)) continue;

            if (!checkFullCube || defaultState.isFullCube(world, blockPos)) {
                Identifier id = Registries.BLOCK.getId(block);
                blockIds.append(id).append(", ");
            }
        }

        String removedComma = blockIds.substring(0, blockIds.length() - 2);
        Adm2.LOGGER.info(removedComma);

        Adm2.LOGGER.info("");
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().isClient()) {
            return ActionResult.SUCCESS;
        }

        PlayerEntity player = context.getPlayer();
        Block block = context.getWorld().getBlockState(context.getBlockPos()).getBlock();
        String modId = Registries.BLOCK.getId(block).getNamespace();

        if (player == null) {
            return ActionResult.FAIL;
        }

        printBlocksConsole(context.getWorld(), context.getBlockPos(), modId, !player.isSneaking());
        player.sendMessage(Text.translatable("message.adm2.debug_print"), true);

        return ActionResult.SUCCESS;
    }
}
