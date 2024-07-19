package dev.luxmiyu.adm2.item;

import dev.luxmiyu.adm2.portal.Portal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SummonWandItem extends WandItem {
    private Block savedBlock = null;

    public SummonWandItem(Settings settings) {
        super(settings, "tooltip.adm2.summon_wand", 0x34b124);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (world.isClient()) {
            return TypedActionResult.success(player.getStackInHand(hand));
        }

        if (savedBlock == null) {
            player.sendMessage(Text.translatable("message.adm2.summon_cancel_fail"), true);
        } else {
            savedBlock = null;
            player.sendMessage(Text.translatable("message.adm2.summon_cancel_success"), true);
        }

        return TypedActionResult.success(player.getStackInHand(hand));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();

        BlockPos blockPos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();

        Block block = context.getWorld().getBlockState(context.getBlockPos()).getBlock();

        if (player == null) {
            return ActionResult.PASS;
        }

        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }

        if (savedBlock != null) {
            BlockPos summonPos = blockPos.offset(Direction.UP, 0);

            BlockState summonState = world.getBlockState(summonPos);
            if (summonState.isFullCube(world, summonPos)) {
                summonPos = blockPos.offset(Direction.UP, 1);
            }

            Vec3d summonPosVec = new Vec3d(summonPos.getX(), summonPos.getY(), summonPos.getZ());
            Vec3d playerPos = player.getPos();

            double diffX = Math.abs(summonPosVec.getX() - playerPos.getX());
            double diffZ = Math.abs(summonPosVec.getZ() - playerPos.getZ());

            Direction.Axis horizontal = diffX > diffZ ? Direction.Axis.X : Direction.Axis.Z;

            BlockState frameState = savedBlock.getDefaultState();

            if (savedBlock instanceof LeavesBlock) {
                frameState = frameState.with(LeavesBlock.PERSISTENT, true);
            }

            Portal.placePortalFrame(world, summonPos, horizontal, frameState);
            Portal.placePortalBlocks(world, summonPos, horizontal);

            Identifier id = Registries.BLOCK.getId(savedBlock);
            String blockName = Text.translatable("block." + id.toTranslationKey()).getString();

            player.sendMessage(Text.translatable("message.adm2.summon_summon", blockName, summonPos.toShortString()), true);

            savedBlock = null;
        } else {
            if (Portal.isDimensionLoaded(world.getServer(), block)) {
                savedBlock = block;

                Identifier id = Registries.BLOCK.getId(block);
                String blockName = Text.translatable("block." + id.toTranslationKey()).getString();

                player.sendMessage(Text.translatable("message.adm2.summon_save_success", blockName), true);
            } else {
                Identifier id = Registries.BLOCK.getId(block);
                String blockName = Text.translatable("block." + id.toTranslationKey()).getString();

                player.sendMessage(Text.translatable("message.adm2.summon_save_fail", blockName), true);
            }
        }

        return ActionResult.SUCCESS;
    }
}