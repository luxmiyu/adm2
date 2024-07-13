package dev.luxmiyu.adm2.item;

import dev.luxmiyu.adm2.util.Adm2Util;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class TeleportWandItem extends WandItem {
    public TeleportWandItem(Settings settings) {
        super(settings, "tooltip.adm2.teleport_wand", 0xccc492);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (world.isClient) {
            return TypedActionResult.success(player.getStackInHand(hand));
        }

        Vec3d startPos = player.getCameraPosVec(1.0F);
        Vec3d direction = player.getRotationVec(1.0F);
        double distance = 200.0D;

        Vec3d endPos = startPos.add(direction.multiply(distance));

        BlockHitResult hitResult = world.raycast(new RaycastContext(
            startPos,
            endPos,
            RaycastContext.ShapeType.OUTLINE,
            RaycastContext.FluidHandling.NONE,
            player
        ));

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = hitResult.getBlockPos();

            if (player.isSneaking()) {
                // Find the surface from the block the player is looking at
                BlockPos surfacePos = blockPos;
                BlockState currentState = world.getBlockState(surfacePos);

                while (!currentState.isAir()) {
                    surfacePos = surfacePos.up();
                    currentState = world.getBlockState(surfacePos);
                }

                Vec3d teleportPos = new Vec3d(surfacePos.getX() + 0.5F, surfacePos.getY() + 0.2F, surfacePos.getZ() + 0.5F);

                player.teleport(teleportPos.getX(), teleportPos.getY(), teleportPos.getZ());

                String blockName = currentState.getBlock().getName().getString();
                player.sendMessage(Adm2Util.textReplaceable("message.adm2.teleport_success", blockName, Adm2Util.posToString(surfacePos)), true);
            } else {
                // Teleport player to the side of the block they are looking at
                Vec3d teleportPos = new Vec3d(blockPos.getX() + 0.5F, blockPos.getY() + 0.2F, blockPos.getZ() + 0.5F);

                Direction blockFacing = hitResult.getSide();
                teleportPos = teleportPos.offset(blockFacing, 1.0D);

                player.teleport(teleportPos.getX(), teleportPos.getY(), teleportPos.getZ());

                String blockName = world.getBlockState(blockPos).getBlock().getName().getString();
                player.sendMessage(Adm2Util.textReplaceable("message.adm2.teleport_success", blockName, Adm2Util.posToString(blockPos)), true);
            }

            world.playSound(null, player.getBlockPos(), net.minecraft.sound.SoundEvents.ENTITY_ENDERMAN_TELEPORT, net.minecraft.sound.SoundCategory.PLAYERS, 1.0F, 1.0F);
        } else {
            player.sendMessage(Text.translatable("message.adm2.teleport_fail"), true);
        }

        return TypedActionResult.success(player.getStackInHand(hand));
    }
}
