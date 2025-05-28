package net.matth.ships;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class ShipEntityRenderer extends EntityRenderer<ShipEntity> {
    private final SchoonerModel model;

    public ShipEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = createModel(context);
        this.shadowRadius = 0.5F;
    }

    public static SchoonerModel createModel(EntityRendererFactory.Context context) {
        return new SchoonerModel(context.getPart(ShipsClient.MODEL_SHIP_LAYER));
    }

    @Override
    public Identifier getTexture(ShipEntity entity) {
        return new Identifier(Ships.MOD_ID, "textures/entity/ship/ship.png");
    }

    @Override
    public void render(ShipEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
		matrices.translate(0.0F, -1.5F, 0.0F);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - yaw));
        this.model.setAngles(entity, tickDelta, 0, 0, 0, 0);
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(getTexture(entity))), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }
    
}
