package robot.abilities.entity.render;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import robot.abilities.entity.IcicleEntity;
import robot.abilities.entity.ModEntities;


public class IcicleEntityRender extends ModeledProjectileEntityRenderer<IcicleEntity, IcicleEntityRender.Model> {
    private static final Identifier texture = new Identifier("abilities:textures/entities/projectiles/fireball.png");

    public IcicleEntityRender(EntityRendererFactory.Context context) {
        super(context, new Model(context.getPart(ModEntities.FIRE_BALL_LAYER)));
    }

    @Override
    public Identifier getTexture(IcicleEntity entity) {
        return texture;
    }

    public static class Model extends EntityModel<IcicleEntity> {
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
        public void setAngles(IcicleEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            // Потом
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
            bb_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);

        }
    }
}
