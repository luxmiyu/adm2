package dev.luxmiyu.adm2.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class WandItem extends Item {
    public final String tooltipKey;
    public final int tooltipColor;

    public WandItem(Settings settings, String tooltipKey, int tooltipColor) {
        super(settings);

        this.tooltipKey = tooltipKey;
        this.tooltipColor = tooltipColor;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        Identifier stackId = Registries.ITEM.getId(stack.getItem());
        Identifier thisId = Registries.ITEM.getId(this);

        if (stackId.equals(thisId)) {
            tooltip.add(Text.translatable(tooltipKey).setStyle(Style.EMPTY.withColor(tooltipColor)));
        }
    }
}