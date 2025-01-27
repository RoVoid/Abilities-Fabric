package robot.abilities.entity.render;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

@Environment(value = EnvType.CLIENT)
public abstract class ModeledProjectileEntityRenderer<T extends ProjectileEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final float field_32939 = 0.1f;
    protected M model;
    protected final List<FeatureRenderer<T, M>> features = Lists.newArrayList();

    public ModeledProjectileEntityRenderer(EntityRendererFactory.Context ctx, M model) {
        super(ctx);
        this.model = model;
        this.shadowRadius = 0;
    }

    protected final boolean addFeature(FeatureRenderer<T, M> feature) {
        return this.features.add(feature);
    }

    @Override
    public M getModel() {
        return this.model;
    }

    @Override
    public void render(T projectileEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        float n;
        Direction direction;
        Entity entity;
        matrixStack.push();
        float h = MathHelper.lerpAngleDegrees(g, projectileEntity.prevYaw, projectileEntity.getBodyYaw());
        float k = -h;
        float m = MathHelper.lerp(g, projectileEntity.prevPitch, projectileEntity.getPitch());
        float l = this.getAnimationProgress(projectileEntity, g);
        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        this.scale(projectileEntity, matrixStack, g);
        matrixStack.translate(0.0f, -1.501f, 0.0f);
        n = 0.0f;
        float o = 0.0f;
        this.model.animateModel(projectileEntity, o, n, g);
        this.model.setAngles(projectileEntity, o, n, l, k, m);
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        boolean bl = this.isVisible(projectileEntity);
        boolean bl2 = !bl && !projectileEntity.isInvisibleTo(minecraftClient.player);
        boolean bl3 = minecraftClient.hasOutline(projectileEntity);
        RenderLayer renderLayer = this.getRenderLayer(projectileEntity, bl, bl2, bl3);
        if (renderLayer != null) {
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
            this.model.render(matrixStack, vertexConsumer, i, getOverlay(0), 1.0f, 1.0f, 1.0f, bl2 ? 0.15f : 1.0f);
        }
        for (FeatureRenderer<T, M> featureRenderer : this.features) {
            featureRenderer.render(matrixStack, vertexConsumerProvider, i, projectileEntity, o, n, g, l, k, m);
        }
        matrixStack.pop();
        super.render(projectileEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public static int getOverlay(float whiteOverlayProgress) {
        return OverlayTexture.packUv(OverlayTexture.getU(whiteOverlayProgress), OverlayTexture.getV(false));
    }

    @Nullable
    protected RenderLayer getRenderLayer(T entity, boolean showBody, boolean translucent, boolean showOutline) {
        Identifier identifier = this.getTexture(entity);
        if (translucent) {
            return RenderLayer.getItemEntityTranslucentCull(identifier);
        }
        if (showBody) {
            return this.model.getLayer(identifier);
        }
        if (showOutline) {
            return RenderLayer.getOutline(identifier);
        }
        return null;
    }

    protected boolean isVisible(T entity) {
        return !entity.isInvisible();
    }

    private static float getYaw(Direction direction) {
        return switch (direction) {
            case SOUTH -> 90.0f;
            case WEST -> 0.0f;
            case NORTH -> 270.0f;
            case EAST -> 180.0f;
            default -> 0.01f;
        };
    }

    protected float getAnimationProgress(T entity, float tickDelta) {
        return (float) entity.age + tickDelta;
    }

    protected float getLyingAngle(T entity) {
        return 90.0f;
    }

    protected float getAnimationCounter(T entity, float tickDelta) {
        return 0.0f;
    }

    protected void scale(T entity, MatrixStack matrices, float amount) {
    }
}


