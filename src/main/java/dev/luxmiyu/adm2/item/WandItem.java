package dev.luxmiyu.adm2.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WandItem extends Item {
    private final String tooltipKey;
    private final int tooltipColor;

    public WandItem(Settings settings, String tooltipKey, int tooltipColor) {
        super(settings);

        this.tooltipKey = tooltipKey;
        this.tooltipColor = tooltipColor;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        Text text = Text.translatable(tooltipKey).setStyle(Style.EMPTY.withColor(tooltipColor));
        tooltip.add(text);

        super.appendTooltip(stack, world, tooltip, context);
    }
}
