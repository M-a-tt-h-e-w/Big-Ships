package net.matth.ships;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ShipsClient implements ClientModInitializer {
    public static final EntityModelLayer MODEL_SHIP_LAYER = new EntityModelLayer(new Identifier(Ships.MOD_ID, "ship"),
            "main");

    @Override
    public void onInitializeClient() {
        HandledScreens.register(Ships.SHIPBUILDING_SCREEN_HANDLER, ShipbuildingScreen::new);
        EntityRendererRegistry.register(Ships.SHIP, ShipEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_SHIP_LAYER, SchoonerModel::getTexturedModelData);
    }
}
