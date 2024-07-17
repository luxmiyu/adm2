package dev.luxmiyu.adm2.item;

import dev.luxmiyu.adm2.Adm2;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public class PortalWandClassicItem extends WandItem {
    public PortalWandClassicItem(Settings settings) {
        super(settings, "tooltip.adm2.portal_wand_classic", 0xb0d1d6);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (Adm2.isModLoaded("immersive_portals")) {
            // TODO: Add portals
            if (context.getPlayer() != null)
                context.getPlayer().sendMessage(Text.literal("Portals disabled in this Alpha version, use Blink Wand instead").withColor(0xffff00), true);
            return ActionResult.SUCCESS;
        } else {
            PlayerEntity player = context.getPlayer();

            if (player == null) return ActionResult.FAIL;

            if (!context.getWorld().isClient()) {
                player.sendMessage(Text.translatable("message.adm2.portal_wand_classic_fail"), true);
            }

            return ActionResult.SUCCESS;
        }
    }
}
