package robot.abilities.block;

import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;
import robot.abilities.block.blockentity.CubesAltarBlockEntity;
import robot.abilities.block.blockentity.MagicCircleBlockEntity;
import robot.abilities.block.blockentity.SoulFurnaceBlockEntity;

public class ModBlockEntities {
    public static void register() {
    }

    public static void registerRender() {
        BlockEntityRendererRegistry.INSTANCE.register(ALTAR_BLOCK_ENTITY, CubesAltarBlockEntity.Render::new);
        BlockEntityRendererRegistry.INSTANCE.register(MAGIC_CIRCLE_BLOCK_ENTITY, MagicCircleBlockEntity.Render::new);
    }

    public static final BlockEntityType<SoulFurnaceBlockEntity> SOUL_FURNACE_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(AbilitiesMod.ID, "soul_furnace"), FabricBlockEntityTypeBuilder.create(SoulFurnaceBlockEntity::new, ModBlocks.SOUL_FURNACE).build());
    public static final BlockEntityType<CubesAltarBlockEntity> ALTAR_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(AbilitiesMod.ID, "altar"), FabricBlockEntityTypeBuilder.create(CubesAltarBlockEntity::new, ModBlocks.ALTAR).build());
    public static final BlockEntityType<MagicCircleBlockEntity> MAGIC_CIRCLE_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(AbilitiesMod.ID, "magic_circle"), FabricBlockEntityTypeBuilder.create(MagicCircleBlockEntity::new, ModBlocks.MAGIC_CIRCLE).build());
}
