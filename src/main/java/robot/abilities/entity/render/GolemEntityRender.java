package robot.abilities.entity.render;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import robot.abilities.entity.GolemEntity;
import robot.abilities.entity.ModEntities;


public class GolemEntityRender extends MobEntityRenderer<GolemEntity, GolemEntityRender.Model> {
    private static final Identifier texture = new Identifier("abilities:textures/entities/golem.png");

    public GolemEntityRender(EntityRendererFactory.Context context) {
        super(context, new Model(context.getPart(ModEntities.GOLEM_LAYER)), 0);
    }

    @Override
    public Identifier getTexture(GolemEntity entity) {
        return texture;
    }

    @Override
    protected boolean hasLabel(GolemEntity mobEntity) {
        return false;
    }

    public static class Model extends EntityModel<GolemEntity> {
        private final ModelPart body;
        private ModelPart cube_r1;
       /* private final ModelPart head;
        private final ModelPart nose;
        private final ModelPart armR;
        private final ModelPart armL;
        private final ModelPart legR;
        private final ModelPart legL;*/

        public Model(ModelPart root) {
            this.body = root.getChild("body");
           /* this.head = body.getChild("head");
            this.nose = head.getChild("nose");
            this.legL = body.getChild("legR");
            this.legR = body.getChild("legR");
            this.armL = body.getChild("armL");
            this.armR = body.getChild("armR");*/
        }

        public static TexturedModelData getTexturedModelData() {
            ModelData modelData = new ModelData();
            ModelPartData modelPartData = modelData.getRoot();
            ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 16.0F, 0.0F));

            ModelPartData cube_r1 = body.addChild("cube_r1", ModelPartBuilder.create().uv(0, 15).cuboid(-4.0F, -2.5F, -3.0F, 8.0F, 5.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

            ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -2.5F, -4.0F, 8.0F, 5.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -5.5F, 0.0F));

            ModelPartData nose = head.addChild("nose", ModelPartBuilder.create().uv(39, 20).cuboid(-1.0F, -0.5F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 1.0F, -4.0F));

            ModelPartData armR = body.addChild("armR", ModelPartBuilder.create().uv(29, 34).cuboid(-1.0F, -1.0F, -1.5F, 2.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, -2.0F, 0.0F));

            ModelPartData armL = body.addChild("armL", ModelPartBuilder.create().uv(29, 13).cuboid(-1.0F, -1.0F, -1.5F, 2.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.0F, -2.0F, 0.0F));

            ModelPartData legR = body.addChild("legR", ModelPartBuilder.create().uv(1, 27).cuboid(-2.0F, -0.5F, -1.5F, 4.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 3.5F, 0.0F));

            ModelPartData legL = body.addChild("legL", ModelPartBuilder.create().uv(15, 27).cuboid(-2.0F, -0.5F, -1.5F, 4.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 3.5F, 0.0F));
            return TexturedModelData.of(modelData, 48, 48);
        }

        @Override
        public void setAngles(GolemEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
            body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        }
    }
}