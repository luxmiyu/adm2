package dev.luxmiyu.adm2;

import dev.luxmiyu.adm2.command.Adm2Command;
import dev.luxmiyu.adm2.item.*;
import dev.luxmiyu.adm2.util.Adm2Util;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.ColorCode;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.server.command.CommandManager.*;

public class Adm2 implements ModInitializer {
	public static final String MOD_ID = "adm2";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final Item.Settings WAND_SETTINGS = new Item.Settings().maxCount(1);
	private static final AbstractBlock.Settings SAND_SETTINGS = AbstractBlock.Settings.copy(Blocks.SAND).strength(3f);

	public static final Item ANY_DIMENSIONAL_PORTAL_WAND = registerItem("any_dimensional_portal_wand", new PortalWandItem(WAND_SETTINGS));
	public static final Item ANY_DIMENSIONAL_PORTAL_WAND_CLASSIC = registerItem("any_dimensional_portal_wand_classic", new PortalWandClassicItem(WAND_SETTINGS));
	public static final Item ANY_DIMENSIONAL_TELEPORT_WAND = registerItem("any_dimensional_teleport_wand", new TeleportWandItem(WAND_SETTINGS));
	public static final Item ANY_DIMENSIONAL_SUMMON_WAND = registerItem("any_dimensional_summon_wand", new SummonWandItem(WAND_SETTINGS));
	public static final Item ANY_DIMENSIONAL_RANDOM_WAND = registerItem("any_dimensional_random_wand", new RandomWandItem(WAND_SETTINGS));
	public static final Item ANY_DIMENSIONAL_LIST_WAND = registerItem("any_dimensional_list_wand", new ListWandItem(WAND_SETTINGS));
	public static final Item ANY_DIMENSIONAL_BLINK_WAND = registerItem("any_dimensional_blink_wand", new BlinkWandItem(WAND_SETTINGS));
	public static final Item ANY_DIMENSIONAL_DEBUG_WAND = registerItem("any_dimensional_debug_wand", new DebugWandItem(WAND_SETTINGS));
	public static final Item ANY_DIMENSIONAL_USELESS_WAND = registerItem("any_dimensional_useless_wand", new UselessWandItem(WAND_SETTINGS));

	public static final Item ANY_DIMENSIONAL_ROD = registerItem("any_dimensional_rod", new Item(new Item.Settings().maxCount(64)));
	public static final Item ANY_DIMENSIONAL_RING = registerItem("any_dimensional_ring", new Item(new Item.Settings().maxCount(64)));

	public static final Block ANY_DIMENSIONAL_SAND = registerBlock("any_dimensional_sand", new ColoredFallingBlock(new ColorCode(0x19141f), SAND_SETTINGS));
	public static final Block ANY_DIMENSIONAL_COAL_ORE = registerBlock("any_dimensional_coal_ore", new ColoredFallingBlock(new ColorCode(0x19141f), SAND_SETTINGS));
	public static final Block ANY_DIMENSIONAL_IRON_ORE = registerBlock("any_dimensional_iron_ore", new ColoredFallingBlock(new ColorCode(0x19141f), SAND_SETTINGS));
	public static final Block ANY_DIMENSIONAL_GOLD_ORE = registerBlock("any_dimensional_gold_ore", new ColoredFallingBlock(new ColorCode(0x19141f), SAND_SETTINGS));
	public static final Block ANY_DIMENSIONAL_DIAMOND_ORE = registerBlock("any_dimensional_diamond_ore", new ColoredFallingBlock(new ColorCode(0x19141f), SAND_SETTINGS));
	public static final Block ANY_DIMENSIONAL_EMERALD_ORE = registerBlock("any_dimensional_emerald_ore", new ColoredFallingBlock(new ColorCode(0x19141f), SAND_SETTINGS));
	public static final Block ANY_DIMENSIONAL_LAPIS_ORE = registerBlock("any_dimensional_lapis_ore", new ColoredFallingBlock(new ColorCode(0x19141f), SAND_SETTINGS));
	public static final Block ANY_DIMENSIONAL_REDSTONE_ORE = registerBlock("any_dimensional_redstone_ore", new ColoredFallingBlock(new ColorCode(0x19141f), SAND_SETTINGS));
	public static final Block ANY_DIMENSIONAL_COPPER_ORE = registerBlock("any_dimensional_copper_ore", new ColoredFallingBlock(new ColorCode(0x19141f), SAND_SETTINGS));
	public static final Block ANY_DIMENSIONAL_QUARTZ_ORE = registerBlock("any_dimensional_quartz_ore", new ColoredFallingBlock(new ColorCode(0x19141f), SAND_SETTINGS));
	public static final Block ANY_DIMENSIONAL_BLOCK = registerBlock("any_dimensional_block", new Block(AbstractBlock.Settings.copy(Blocks.STONE)));

	public static final ItemGroup ANY_DIMENSIONAL_ITEM_GROUP = Registry.register(
		Registries.ITEM_GROUP,
		Identifier.of(Adm2.MOD_ID, "any_dimensional_item_group"),

		FabricItemGroup.builder()
			.displayName(Text.translatable("adm2.display_name"))
			.icon(() -> new ItemStack(ANY_DIMENSIONAL_PORTAL_WAND))
			.entries((displayContext, entries) -> {
				// wands
				entries.add(new ItemStack(ANY_DIMENSIONAL_PORTAL_WAND));
				entries.add(new ItemStack(ANY_DIMENSIONAL_PORTAL_WAND_CLASSIC));
				entries.add(new ItemStack(ANY_DIMENSIONAL_TELEPORT_WAND));
				entries.add(new ItemStack(ANY_DIMENSIONAL_SUMMON_WAND));
				entries.add(new ItemStack(ANY_DIMENSIONAL_RANDOM_WAND));
				entries.add(new ItemStack(ANY_DIMENSIONAL_LIST_WAND));
				entries.add(new ItemStack(ANY_DIMENSIONAL_BLINK_WAND));
				entries.add(new ItemStack(ANY_DIMENSIONAL_DEBUG_WAND));
				entries.add(new ItemStack(ANY_DIMENSIONAL_USELESS_WAND));

				// items
				entries.add(new ItemStack(ANY_DIMENSIONAL_ROD));
				entries.add(new ItemStack(ANY_DIMENSIONAL_RING));

				// blocks
				entries.add(new ItemStack(ANY_DIMENSIONAL_SAND));
				entries.add(new ItemStack(ANY_DIMENSIONAL_COAL_ORE));
				entries.add(new ItemStack(ANY_DIMENSIONAL_IRON_ORE));
				entries.add(new ItemStack(ANY_DIMENSIONAL_GOLD_ORE));
				entries.add(new ItemStack(ANY_DIMENSIONAL_DIAMOND_ORE));
				entries.add(new ItemStack(ANY_DIMENSIONAL_EMERALD_ORE));
				entries.add(new ItemStack(ANY_DIMENSIONAL_LAPIS_ORE));
				entries.add(new ItemStack(ANY_DIMENSIONAL_REDSTONE_ORE));
				entries.add(new ItemStack(ANY_DIMENSIONAL_COPPER_ORE));
				entries.add(new ItemStack(ANY_DIMENSIONAL_QUARTZ_ORE));
				entries.add(new ItemStack(ANY_DIMENSIONAL_BLOCK));
			}).build());

	private static Item registerItem(String name, Item item) {
		return Registry.register(Registries.ITEM, Identifier.of(Adm2.MOD_ID, name), item);
	}

	private static Block registerBlock(String name, Block block) {
		registerItem(name, new BlockItem(block, new Item.Settings()));
		return Registry.register(Registries.BLOCK, Identifier.of(Adm2.MOD_ID, name), block);
	}

	private static void registerCommands() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
			dispatcher.register(literal("adm2")
				.requires(source -> source.hasPermissionLevel(2))
				.executes(Adm2Command::adm2Execute)));

		LOGGER.info("Registered /adm2 command");
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Any Dimension Mod 2");
		registerCommands();

		// TODO: Wait for CustomPortalAPI to update to 1.21
		// register Custom Portal API portal links on server start
//		ServerLifecycleEvents.SERVER_STARTED.register(Adm2Util::onServerStart);
	}
}
