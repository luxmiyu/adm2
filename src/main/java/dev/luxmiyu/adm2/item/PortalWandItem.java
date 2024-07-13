package dev.luxmiyu.adm2.item;

import dev.luxmiyu.adm2.util.Adm2Util;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class PortalWandItem extends WandItem {
    public PortalWandItem(Settings settings) {
        super(settings, "tooltip.adm2.portal_wand", 0xb0d1d6);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (Adm2Util.isModLoaded("immersive_portals")) {
            return ActionResult.SUCCESS;
        } else {
            return ActionResult.PASS;
        }
    }
}
