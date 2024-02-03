package robot.abilities.entity.render;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import robot.abilities.entity.FireBallEntity;
import robot.abilities.entity.ModEntities;
import robot.abilities.entity.WaterBallEntity;


public class WaterBallEntityRender extends ModeledProjectileEntityRenderer<WaterBallEntity, WaterBallEntityRender.Model> {
    private static final Identifier texture = new Identifier("abilities:textures/projectiles/fireball.png");

    public WaterBallEntityRender(EntityRendererFactory.Context context) {
        super(context, new Model(context.getPart(ModEntities.WATER_BALL_LAYER)), 0);
    }

    @Override
    public Identifier getTexture(WaterBallEntity entity) {
        return texture;
    }

    public static class Model extends EntityModel<WaterBallEntity> {
        private final ModelPart bb_main;

        public Model(ModelPart root) {
            this.bb_main = root.getChild("bb_main");
        }

        public static TexturedModelData getTexturedModelData() {
            ModelData modelData = new ModelData();
            ModelPartData modelPartData = modelData.getRoot();
            ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -12.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
            return TexturedModelData.of(modelData, 32, 16);
        }

        @Override
        public void setAngles(WaterBallEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            // Потом
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
            bb_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);

        }
    }
}
