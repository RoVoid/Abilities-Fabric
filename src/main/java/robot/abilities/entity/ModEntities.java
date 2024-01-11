package robot.abilities.entity;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;

public class ModEntities {
    public static final EntityType<CustomArrowEntity> CUSTOM_ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(AbilitiesMod.ID, "arrow_entity"),
            FabricEntityTypeBuilder.<CustomArrowEntity>create(SpawnGroup.MISC, CustomArrowEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f)).build());

    public static void registerRender() {
        EntityRendererRegistry.register(ModEntities.CUSTOM_ARROW_ENTITY, ArrowEntityRenderer::new);
    }
}
