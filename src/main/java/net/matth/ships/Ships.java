package net.matth.ships;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ships implements ModInitializer {
	public static final String MOD_ID = "ships";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final ScreenHandlerType<ShipbuildingScreenHandler> SHIPBUILDING_SCREEN_HANDLER = Registry.register(
			Registries.SCREEN_HANDLER, new Identifier(MOD_ID, "shipbuilding_table"),
			new ScreenHandlerType<>(ShipbuildingScreenHandler::new, FeatureSet.empty()));

	public static final EntityType<ShipEntity> SHIP = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(MOD_ID, "ship"),
			EntityType.Builder.create(ShipEntity::new, SpawnGroup.MISC).setDimensions(1.375F, 0.5625F).build("ship"));

	public static final Identifier SPAWN_SHIP_PACKET_ID = new Identifier(Ships.MOD_ID, "spawn_ship");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// LOGGER.info("Hello Fabric world!");

		ModItems.initialize();
		ModBlocks.initialize();

		ServerPlayNetworking.registerGlobalReceiver(Ships.SPAWN_SHIP_PACKET_ID,
				(server, player, handler, buf, responseSender) -> {
					server.execute(() -> {
						if (player.currentScreenHandler instanceof ShipbuildingScreenHandler screenHandler) {
							screenHandler.buildShip();
						}
					});
				});
	}
}
