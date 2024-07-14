package dev.luxmiyu.adm2.item;

import dev.luxmiyu.adm2.util.Adm2Util;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
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
            // TODO: Wait for CustomPortalAPI to update to 1.21,
            //   then change ActionResult.SUCCESS to ActionResult.PASS
            //   and remove the message
            if (context.getPlayer() != null)
                context.getPlayer().sendMessage(Text.literal("CustomPortalAPI not implemented in this pre-release version").withColor(0xffff00), true);
            return ActionResult.SUCCESS;
        }
    }
}
