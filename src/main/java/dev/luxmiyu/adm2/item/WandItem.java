package dev.luxmiyu.adm2.item;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class WandItem extends Item {
    public WandItem(Settings settings, String tooltipKey, int tooltipColor) {
        super(settings);

        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) -> {
            Identifier stackId = Registries.ITEM.getId(stack.getItem());
            Identifier thisId = Registries.ITEM.getId(this);

            if (stackId.equals(thisId)) {
                lines.add(Text.translatable(tooltipKey).setStyle(Style.EMPTY.withColor(tooltipColor)));
            }
        });
    }
}
