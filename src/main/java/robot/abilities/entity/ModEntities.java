package robot.abilities.entity;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;
import robot.abilities.entity.render.FireBallEntityRender;
import robot.abilities.entity.render.GolemEntityRender;
import robot.abilities.entity.render.WaterBallEntityRender;

public class ModEntities {
    public static final EntityType<CustomArrowEntity> CUSTOM_ARROW = Registry.register(Registries.ENTITY_TYPE, new Identifier(AbilitiesMod.ID, "arrow_entity"), FabricEntityTypeBuilder.<CustomArrowEntity>create(SpawnGroup.MISC, CustomArrowEntity::new).dimensions(EntityDimensions.fixed(0.25f, 0.25f)).trackRangeBlocks(4).trackedUpdateRate(10).build());

    public static final EntityType<FireBallEntity> FIRE_BALL = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(AbilitiesMod.ID, "fireball_entity"),
            FabricEntityTypeBuilder.<FireBallEntity>create(SpawnGroup.MISC, FireBallEntity::new).dimensions(EntityDimensions.fixed(0.5f, 0.5f)).trackRangeBlocks(100).trackedUpdateRate(10).build());
    public static final EntityModelLayer FIRE_BALL_LAYER = new EntityModelLayer(new Identifier("fireball_layer", "cube"), "main");
    public static final EntityType<WaterBallEntity> WATER_BALL = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(AbilitiesMod.ID, "water_ball_entity"),
            FabricEntityTypeBuilder.<WaterBallEntity>create(SpawnGroup.MISC, WaterBallEntity::new).dimensions(EntityDimensions.fixed(0.5f, 0.5f)).trackRangeBlocks(100).trackedUpdateRate(10).build());
    public static final EntityModelLayer WATER_BALL_LAYER = new EntityModelLayer(new Identifier("water_ball_layer", "cube"), "main");

    public static final EntityType<GolemEntity> GOLEM = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(AbilitiesMod.ID, "golem_entity"),
            FabricEntityTypeBuilder.<GolemEntity>create(SpawnGroup.MISC, GolemEntity::new).dimensions(EntityDimensions.fixed(0.8f, 0.85f)).build());
    public static final EntityModelLayer GOLEM_LAYER = new EntityModelLayer(new Identifier("golem_layer", "cube"), "main");

    public static void register() {
        FabricDefaultAttributeRegistry.register(ModEntities.GOLEM, GolemEntity.createAttributes());
    }

    public static void registerRender() {
        EntityRendererRegistry.register(ModEntities.CUSTOM_ARROW, ArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.FIRE_BALL, FireBallEntityRender::new);
        EntityRendererRegistry.register(ModEntities.WATER_BALL, WaterBallEntityRender::new);
        EntityRendererRegistry.register(ModEntities.GOLEM, GolemEntityRender::new);
        EntityModelLayerRegistry.registerModelLayer(FIRE_BALL_LAYER, FireBallEntityRender.Model::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WATER_BALL_LAYER, WaterBallEntityRender.Model::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(GOLEM_LAYER, GolemEntityRender.Model::getTexturedModelData);
    }
}
