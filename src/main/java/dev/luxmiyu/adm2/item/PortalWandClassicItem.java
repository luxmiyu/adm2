package dev.luxmiyu.adm2.item;

import dev.luxmiyu.adm2.util.Adm2Util;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class PortalWandClassicItem extends WandItem {
    public PortalWandClassicItem(Settings settings) {
        super(settings, "tooltip.adm2.portal_wand_classic", 0xb0d1d6);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (Adm2Util.isModLoaded("immersive_portals")) {
            return ActionResult.PASS;
        } else {
            PlayerEntity player = context.getPlayer();

            if (player == null) return ActionResult.FAIL;

            if (!context.getWorld().isClient()) {
                player.sendMessage(Adm2Util.textReplaceable("message.adm2.portal_wand_classic_fail"), true);
            }

            return ActionResult.SUCCESS;
        }
    }
}
