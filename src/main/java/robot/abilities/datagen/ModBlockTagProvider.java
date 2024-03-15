package robot.abilities.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

import static robot.abilities.block.ModBlocks.*;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL).add(CRYSTAL_BUD, CRYSTAL_BLOCK, ALTAR, WILD_MAGIC_BEACON, DOREEL_ORE, MITHRIL_ORE);
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(CRYSTAL_BUD, CRYSTAL_BLOCK, ALTAR, SOUL_FURNACE, WILD_MAGIC_BEACON, DOREEL_ORE, MITHRIL_ORE);
    }
}
