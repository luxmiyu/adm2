package dev.luxmiyu.adm2.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class UselessWandItem extends WandItem {
    public UselessWandItem(Settings settings) {
        super(settings, "tooltip.adm2.useless_wand", 0x6d6d6d);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
            player.sendMessage(Text.translatable("message.adm2.useless"), true);
        }

        return TypedActionResult.success(player.getStackInHand(hand));
    }
}
