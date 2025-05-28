package net.matth.ships;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
	public static final Block SHIPBUILDING_TABLE_BLOCK = register(
		new ShipbuildingTableBlock(AbstractBlock.Settings.copy(Blocks.CRAFTING_TABLE)),
		"shipbuilding_table",
		true
	);

	public static Block register(Block block, String name, boolean shouldRegisterItem) {
		Identifier id = new Identifier(Ships.MOD_ID, name);

		if (shouldRegisterItem) {
			BlockItem blockItem = new BlockItem(block, new Item.Settings());
			Registry.register(Registries.ITEM, id, blockItem);
		}

		return Registry.register(Registries.BLOCK, id, block);
	}

	public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL)
                .register((itemGroup) -> itemGroup.add(SHIPBUILDING_TABLE_BLOCK.asItem()));
	}
}
