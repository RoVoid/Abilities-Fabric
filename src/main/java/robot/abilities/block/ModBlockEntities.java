package robot.abilities.block;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;
import robot.abilities.block.blockentity.SoulFurnaceBlockEntity;

public class ModBlockEntities {
    public static void register() {
    }

    public static final BlockEntityType<SoulFurnaceBlockEntity> SOUL_FURNACE_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(AbilitiesMod.ID, "soul_furnace"), FabricBlockEntityTypeBuilder.create(SoulFurnaceBlockEntity::new, ModBlocks.SOUL_FURNACE).build());
}
