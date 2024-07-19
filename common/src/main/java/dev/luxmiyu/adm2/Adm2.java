package dev.luxmiyu.adm2;

import dev.architectury.platform.Platform;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.luxmiyu.adm2.block.AnyDimensionalPortalBlock;
import dev.luxmiyu.adm2.block.AnyDimensionalSandBlock;
import dev.luxmiyu.adm2.event.Adm2Events;
import dev.luxmiyu.adm2.item.*;
import dev.luxmiyu.adm2.portal.TeleportManager;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class Adm2 {
    public static final String MOD_ID = "adm2";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Random RANDOM = new Random();
    public static final TeleportManager TELEPORT_MANAGER = new TeleportManager();

    public static final DeferredRegister<ItemGroup> TABS = DeferredRegister.create(MOD_ID, RegistryKeys.ITEM_GROUP);
    public static final RegistrySupplier<ItemGroup> ADM2_TAB = TABS.getRegistrar().register(
        Identifier.of(MOD_ID, "adm2_tab"),
        () -> CreativeTabRegistry.create(
            Text.translatable("adm2.display_name"),
            () -> new ItemStack(Registries.ITEM.get(Identifier.of(MOD_ID, "any_dimensional_portal_wand")))
        )
    );

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MOD_ID, RegistryKeys.BLOCK);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, RegistryKeys.ITEM);

    private static final Item.Settings WAND_SETTINGS = new Item.Settings().maxCount(1).arch$tab(ADM2_TAB);
    private static final AbstractBlock.Settings SAND_SETTINGS = AbstractBlock.Settings.copy(Blocks.SAND).strength(3f);

    public static final RegistrySupplier<Item> ANY_DIMENSIONAL_PORTAL_WAND = ITEMS.register("any_dimensional_portal_wand", () -> new PortalWandItem(WAND_SETTINGS));
    public static final RegistrySupplier<Item> ANY_DIMENSIONAL_PORTAL_WAND_CLASSIC = ITEMS.register("any_dimensional_portal_wand_classic", () -> new PortalWandClassicItem(WAND_SETTINGS));
    public static final RegistrySupplier<Item> ANY_DIMENSIONAL_TELEPORT_WAND = ITEMS.register("any_dimensional_teleport_wand", () -> new TeleportWandItem(WAND_SETTINGS));
    public static final RegistrySupplier<Item> ANY_DIMENSIONAL_SUMMON_WAND = ITEMS.register("any_dimensional_summon_wand", () -> new SummonWandItem(WAND_SETTINGS));
    public static final RegistrySupplier<Item> ANY_DIMENSIONAL_RANDOM_WAND = ITEMS.register("any_dimensional_random_wand", () -> new RandomWandItem(WAND_SETTINGS));
    public static final RegistrySupplier<Item> ANY_DIMENSIONAL_LIST_WAND = ITEMS.register("any_dimensional_list_wand", () -> new ListWandItem(WAND_SETTINGS));
    public static final RegistrySupplier<Item> ANY_DIMENSIONAL_BLINK_WAND = ITEMS.register("any_dimensional_blink_wand", () -> new BlinkWandItem(WAND_SETTINGS));
    public static final RegistrySupplier<Item> ANY_DIMENSIONAL_DEBUG_WAND = ITEMS.register("any_dimensional_debug_wand", () -> new DebugWandItem(WAND_SETTINGS));
    public static final RegistrySupplier<Item> ANY_DIMENSIONAL_USELESS_WAND = ITEMS.register("any_dimensional_useless_wand", () -> new UselessWandItem(WAND_SETTINGS));

    public static final RegistrySupplier<Item> ANY_DIMENSIONAL_ROD = ITEMS.register("any_dimensional_rod", () -> new Item(new Item.Settings().arch$tab(ADM2_TAB)));
    public static final RegistrySupplier<Item> ANY_DIMENSIONAL_RING = ITEMS.register("any_dimensional_ring", () -> new Item(new Item.Settings().arch$tab(ADM2_TAB)));

    public static final RegistrySupplier<Block> ANY_DIMENSIONAL_SAND = registerBlock("any_dimensional_sand", () -> new AnyDimensionalSandBlock(SAND_SETTINGS));
    public static final RegistrySupplier<Block> ANY_DIMENSIONAL_COAL_ORE = registerBlock("any_dimensional_coal_ore", () -> new AnyDimensionalSandBlock(SAND_SETTINGS));
    public static final RegistrySupplier<Block> ANY_DIMENSIONAL_IRON_ORE = registerBlock("any_dimensional_iron_ore", () -> new AnyDimensionalSandBlock(SAND_SETTINGS));
    public static final RegistrySupplier<Block> ANY_DIMENSIONAL_GOLD_ORE = registerBlock("any_dimensional_gold_ore", () -> new AnyDimensionalSandBlock(SAND_SETTINGS));
    public static final RegistrySupplier<Block> ANY_DIMENSIONAL_DIAMOND_ORE = registerBlock("any_dimensional_diamond_ore", () -> new AnyDimensionalSandBlock(SAND_SETTINGS));
    public static final RegistrySupplier<Block> ANY_DIMENSIONAL_EMERALD_ORE = registerBlock("any_dimensional_emerald_ore", () -> new AnyDimensionalSandBlock(SAND_SETTINGS));
    public static final RegistrySupplier<Block> ANY_DIMENSIONAL_LAPIS_ORE = registerBlock("any_dimensional_lapis_ore", () -> new AnyDimensionalSandBlock(SAND_SETTINGS));
    public static final RegistrySupplier<Block> ANY_DIMENSIONAL_REDSTONE_ORE = registerBlock("any_dimensional_redstone_ore", () -> new AnyDimensionalSandBlock(SAND_SETTINGS));
    public static final RegistrySupplier<Block> ANY_DIMENSIONAL_COPPER_ORE = registerBlock("any_dimensional_copper_ore", () -> new AnyDimensionalSandBlock(SAND_SETTINGS));
    public static final RegistrySupplier<Block> ANY_DIMENSIONAL_QUARTZ_ORE = registerBlock("any_dimensional_quartz_ore", () -> new AnyDimensionalSandBlock(SAND_SETTINGS));
    public static final RegistrySupplier<Block> ANY_DIMENSIONAL_BLOCK = registerBlock("any_dimensional_block", () -> new Block(AbstractBlock.Settings.copy(Blocks.STONE)));

    public static final RegistrySupplier<Block> ANY_DIMENSIONAL_PORTAL_BLOCK = BLOCKS.register("any_dimensional_portal_block", () -> new AnyDimensionalPortalBlock(AbstractBlock.Settings.copy(Blocks.NETHER_PORTAL).strength(0f)));

    private static RegistrySupplier<Block> registerBlock(String name, Supplier<Block> blockSupplier) {
        RegistrySupplier<Block> block = BLOCKS.register(name, blockSupplier);
        registerBlockItem(name, block);
        return block;
    }

    private static void registerBlockItem(String name, RegistrySupplier<Block> block) {
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Settings().arch$tab(ADM2_TAB)));
    }

    public static boolean isModLoaded(String modId) {
        return Platform.isModLoaded(modId);
    }

    public static void init() {
        LOGGER.info("Initializing...");

        BLOCKS.register();
        ITEMS.register();

        Adm2Events.init();
    }
}
