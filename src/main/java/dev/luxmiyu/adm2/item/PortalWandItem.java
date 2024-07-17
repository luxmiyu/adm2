package dev.luxmiyu.adm2.item;

import dev.luxmiyu.adm2.Adm2;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public class PortalWandItem extends WandItem {
    public PortalWandItem(Settings settings) {
        super(settings, "tooltip.adm2.portal_wand", 0xb0d1d6);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (Adm2.isModLoaded("immersive_portals")) {
            return ActionResult.SUCCESS;
        } else {
            // TODO: Add portals
            if (context.getPlayer() != null)
                context.getPlayer().sendMessage(Text.literal("Portals disabled in this Alpha version, use Blink Wand instead")
                    .setStyle(Style.EMPTY.withColor(0xffff00)), true);
            return ActionResult.SUCCESS;
        }
    }
}
