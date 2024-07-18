package dev.luxmiyu.adm2;

import dev.luxmiyu.adm2.block.Adm2SandBlock;
import dev.luxmiyu.adm2.item.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

@Mod(Adm2.MOD_ID)
public final class Adm2 {
    public static final String MOD_ID = "adm2";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Random RANDOM = Random.create();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(RegistryKeys.BLOCK, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(RegistryKeys.ITEM, MOD_ID);

    private static final Item.Settings WAND_SETTINGS = new Item.Settings().maxCount(1);
    private static final AbstractBlock.Settings SAND_SETTINGS = AbstractBlock.Settings.copy(Blocks.SAND).strength(3f);

    public static final RegistryObject<Item> ANY_DIMENSIONAL_PORTAL_WAND = ITEMS.register("any_dimensional_portal_wand", () -> new PortalWandItem(WAND_SETTINGS));
    public static final RegistryObject<Item> ANY_DIMENSIONAL_PORTAL_WAND_CLASSIC = ITEMS.register("any_dimensional_portal_wand_classic", () -> new PortalWandClassicItem(WAND_SETTINGS));
    public static final RegistryObject<Item> ANY_DIMENSIONAL_TELEPORT_WAND = ITEMS.register("any_dimensional_teleport_wand", () -> new TeleportWandItem(WAND_SETTINGS));
    public static final RegistryObject<Item> ANY_DIMENSIONAL_SUMMON_WAND = ITEMS.register("any_dimensional_summon_wand", () -> new SummonWandItem(WAND_SETTINGS));
    public static final RegistryObject<Item> ANY_DIMENSIONAL_RANDOM_WAND = ITEMS.register("any_dimensional_random_wand", () -> new RandomWandItem(WAND_SETTINGS));
    public static final RegistryObject<Item> ANY_DIMENSIONAL_LIST_WAND = ITEMS.register("any_dimensional_list_wand", () -> new ListWandItem(WAND_SETTINGS));
    public static final RegistryObject<Item> ANY_DIMENSIONAL_BLINK_WAND = ITEMS.register("any_dimensional_blink_wand", () -> new BlinkWandItem(WAND_SETTINGS));
    public static final RegistryObject<Item> ANY_DIMENSIONAL_DEBUG_WAND = ITEMS.register("any_dimensional_debug_wand", () -> new DebugWandItem(WAND_SETTINGS));
    public static final RegistryObject<Item> ANY_DIMENSIONAL_USELESS_WAND = ITEMS.register("any_dimensional_useless_wand", () -> new UselessWandItem(WAND_SETTINGS));

    public static final RegistryObject<Item> ANY_DIMENSIONAL_ROD = ITEMS.register("any_dimensional_rod", () -> new Item(new Item.Settings()));
    public static final RegistryObject<Item> ANY_DIMENSIONAL_RING = ITEMS.register("any_dimensional_ring", () -> new Item(new Item.Settings()));

    public static final RegistryObject<Block> ANY_DIMENSIONAL_SAND = registerBlock("any_dimensional_sand", () -> new Adm2SandBlock(SAND_SETTINGS));
    public static final RegistryObject<Block> ANY_DIMENSIONAL_COAL_ORE = registerBlock("any_dimensional_coal_ore", () -> new Adm2SandBlock(SAND_SETTINGS));
    public static final RegistryObject<Block> ANY_DIMENSIONAL_IRON_ORE = registerBlock("any_dimensional_iron_ore", () -> new Adm2SandBlock(SAND_SETTINGS));
    public static final RegistryObject<Block> ANY_DIMENSIONAL_GOLD_ORE = registerBlock("any_dimensional_gold_ore", () -> new Adm2SandBlock(SAND_SETTINGS));
    public static final RegistryObject<Block> ANY_DIMENSIONAL_DIAMOND_ORE = registerBlock("any_dimensional_diamond_ore", () -> new Adm2SandBlock(SAND_SETTINGS));
    public static final RegistryObject<Block> ANY_DIMENSIONAL_EMERALD_ORE = registerBlock("any_dimensional_emerald_ore", () -> new Adm2SandBlock(SAND_SETTINGS));
    public static final RegistryObject<Block> ANY_DIMENSIONAL_LAPIS_ORE = registerBlock("any_dimensional_lapis_ore", () -> new Adm2SandBlock(SAND_SETTINGS));
    public static final RegistryObject<Block> ANY_DIMENSIONAL_REDSTONE_ORE = registerBlock("any_dimensional_redstone_ore", () -> new Adm2SandBlock(SAND_SETTINGS));
    public static final RegistryObject<Block> ANY_DIMENSIONAL_COPPER_ORE = registerBlock("any_dimensional_copper_ore", () -> new Adm2SandBlock(SAND_SETTINGS));
    public static final RegistryObject<Block> ANY_DIMENSIONAL_QUARTZ_ORE = registerBlock("any_dimensional_quartz_ore", () -> new Adm2SandBlock(SAND_SETTINGS));
    public static final RegistryObject<Block> ANY_DIMENSIONAL_BLOCK = registerBlock("any_dimensional_block", () -> new Block(AbstractBlock.Settings.copy(Blocks.STONE)));

    public static final DeferredRegister<ItemGroup> TABS = DeferredRegister.create(RegistryKeys.ITEM_GROUP, MOD_ID);
    public static final RegistryObject<ItemGroup> ADM2_TAB = TABS.register("adm2_tab", () -> ItemGroup.builder()
        .displayName(Text.translatable("adm2.display_name"))
        .icon(() -> new ItemStack(ANY_DIMENSIONAL_PORTAL_WAND.get()))
        .entries((displayContext, entries) -> {
            entries.add(ANY_DIMENSIONAL_PORTAL_WAND.get());
            entries.add(ANY_DIMENSIONAL_PORTAL_WAND_CLASSIC.get());
            entries.add(ANY_DIMENSIONAL_TELEPORT_WAND.get());
            entries.add(ANY_DIMENSIONAL_SUMMON_WAND.get());
            entries.add(ANY_DIMENSIONAL_RANDOM_WAND.get());
            entries.add(ANY_DIMENSIONAL_LIST_WAND.get());
            entries.add(ANY_DIMENSIONAL_BLINK_WAND.get());
            entries.add(ANY_DIMENSIONAL_DEBUG_WAND.get());
            entries.add(ANY_DIMENSIONAL_USELESS_WAND.get());

            entries.add(ANY_DIMENSIONAL_ROD.get());
            entries.add(ANY_DIMENSIONAL_RING.get());

            entries.add(ANY_DIMENSIONAL_SAND.get());
            entries.add(ANY_DIMENSIONAL_COAL_ORE.get());
            entries.add(ANY_DIMENSIONAL_IRON_ORE.get());
            entries.add(ANY_DIMENSIONAL_GOLD_ORE.get());
            entries.add(ANY_DIMENSIONAL_DIAMOND_ORE.get());
            entries.add(ANY_DIMENSIONAL_EMERALD_ORE.get());
            entries.add(ANY_DIMENSIONAL_LAPIS_ORE.get());
            entries.add(ANY_DIMENSIONAL_REDSTONE_ORE.get());
            entries.add(ANY_DIMENSIONAL_COPPER_ORE.get());
            entries.add(ANY_DIMENSIONAL_QUARTZ_ORE.get());
            entries.add(ANY_DIMENSIONAL_BLOCK.get());
        })
        .build()
    );

    private static RegistryObject<Block> registerBlock(String name, Supplier<Block> blockSupplier) {
        RegistryObject<Block> block = BLOCKS.register(name, blockSupplier);
        registerBlockItem(name, block);
        return block;
    }

    private static void registerBlockItem(String name, RegistryObject<Block> block) {
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Settings()));
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    public Adm2() {
        LOGGER.info("Initializing...");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        TABS.register(modEventBus);
    }
}
