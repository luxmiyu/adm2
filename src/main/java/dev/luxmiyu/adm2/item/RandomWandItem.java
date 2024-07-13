package dev.luxmiyu.adm2.item;

import dev.luxmiyu.adm2.util.Adm2Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RandomWandItem extends WandItem {
    public RandomWandItem(Settings settings) {
        super(settings, "tooltip.adm2.random_wand", 0x3434de);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();

        BlockPos blockPos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();

        if (player == null) {
            return ActionResult.PASS;
        }

        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }

        BlockPos summonPos = blockPos.offset(Direction.UP, 0);

        BlockState summonState = world.getBlockState(summonPos);
        if (summonState.isFullCube(world, summonPos)) {
            summonPos = blockPos.offset(Direction.UP, 1);
        }

        Vec3d summonPosVec = new Vec3d(summonPos.getX(), summonPos.getY(), summonPos.getZ());
        Vec3d playerPos = player.getPos();

        double diffX = Math.abs(summonPosVec.getX() - playerPos.getX());
        double diffZ = Math.abs(summonPosVec.getZ() - playerPos.getZ());

        Direction.Axis horizontal = diffX > diffZ ? Direction.Axis.Z : Direction.Axis.X;
        Direction up = Direction.UP;

        // #####
        // #   #
        // #   #
        // #   #
        // ##O##  O = summonPos
        BlockPos[] framePositions = {
            summonPos.offset(horizontal, -2).offset(up, 0),
            summonPos.offset(horizontal, -1).offset(up, 0),
            summonPos.offset(horizontal,  0).offset(up, 0),
            summonPos.offset(horizontal,  1).offset(up, 0),
            summonPos.offset(horizontal,  2).offset(up, 0),

            summonPos.offset(horizontal, -2).offset(up, 1),
            summonPos.offset(horizontal,  2).offset(up, 1),

            summonPos.offset(horizontal, -2).offset(up, 2),
            summonPos.offset(horizontal,  2).offset(up, 2),

            summonPos.offset(horizontal, -2).offset(up, 3),
            summonPos.offset(horizontal,  2).offset(up, 3),

            summonPos.offset(horizontal, -2).offset(up, 4),
            summonPos.offset(horizontal, -1).offset(up, 4),
            summonPos.offset(horizontal,  0).offset(up, 4),
            summonPos.offset(horizontal,  1).offset(up, 4),
            summonPos.offset(horizontal,  2).offset(up, 4),
        };

        BlockPos[] airPositions = {
            summonPos.offset(horizontal, -1).offset(up, 1),
            summonPos.offset(horizontal,  0).offset(up, 1),
            summonPos.offset(horizontal,  1).offset(up, 1),

            summonPos.offset(horizontal, -1).offset(up, 2),
            summonPos.offset(horizontal,  0).offset(up, 2),
            summonPos.offset(horizontal,  1).offset(up, 2),

            summonPos.offset(horizontal, -1).offset(up, 3),
            summonPos.offset(horizontal,  0).offset(up, 3),
            summonPos.offset(horizontal,  1).offset(up, 3),
        };

        Block randomBlock = Adm2Util.getRandomPortalBlock(world.getServer());

        BlockState frameBlock = randomBlock.getDefaultState();
        BlockState airBlock = Blocks.AIR.getDefaultState();

        for (BlockPos pos : framePositions) {
            world.setBlockState(pos, frameBlock);
        }

        for (BlockPos pos : airPositions) {
            world.setBlockState(pos, airBlock);
        }

        Identifier id = Registries.BLOCK.getId(randomBlock);
        String blockName = Text.translatable("block." + id.toTranslationKey()).getString();

        player.sendMessage(Adm2Util.textReplaceable("message.adm2.summon_summon", blockName, Adm2Util.posToString(summonPos)), true);

        return ActionResult.SUCCESS;
    }
}
