package robot.abilities.entity.render;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;
import robot.abilities.entity.GolemEntity;
import robot.abilities.entity.ModEntities;
import robot.abilities.entity.model.GolemEntityModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class GolemEntityRender  extends GeoEntityRenderer<GolemEntity> {
    public GolemEntityRender(EntityRendererFactory.Context renderManager) {
        super(renderManager, new GolemEntityModel());
    }

    @Override
    public Identifier getTextureLocation(GolemEntity animatable) {
        return new Identifier(AbilitiesMod.ID, "textures/entities/golem.png");
    }

    @Override
    public void render(GolemEntity entity, float entityYaw, float partialTick, MatrixStack poseStack,
                       VertexConsumerProvider bufferSource, int packedLight) {
        if (entity.isBaby()) {
            poseStack.scale(0.4f, 0.4f, 0.4f);
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}