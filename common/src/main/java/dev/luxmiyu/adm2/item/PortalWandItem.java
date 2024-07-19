package dev.luxmiyu.adm2.item;

import dev.luxmiyu.adm2.Adm2;
import dev.luxmiyu.adm2.portal.Portal;
import dev.luxmiyu.adm2.portal.PortalArea;
import net.minecraft.block.Block;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class PortalWandItem extends WandItem {
    public PortalWandItem(Settings settings) {
        super(settings, "tooltip.adm2.portal_wand", 0xb0d1d6);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (world.isClient) return ActionResult.SUCCESS;

        if (Adm2.isModLoaded("immersive_portals")) {
            return ActionResult.SUCCESS;
        } else {
            Direction hitSide = context.getSide();
            BlockPos pos = context.getBlockPos().offset(hitSide);
            PortalArea areaX = Portal.findPortalArea(world, pos, Direction.Axis.X);
            PortalArea areaZ = Portal.findPortalArea(world, pos, Direction.Axis.Z);

            boolean placedX = false;

            if (areaX != null) {
                Block block = areaX.getFrame(world);

                if (block != null) {
                    areaX.placeIn(world);
                    placedX = true;
                }
            }

            if (areaZ != null) {
                Block block = areaZ.getFrame(world);

                if (block != null && !placedX) {
                    areaZ.placeIn(world);
                }
            }

            return ActionResult.SUCCESS;
        }
    }
}
