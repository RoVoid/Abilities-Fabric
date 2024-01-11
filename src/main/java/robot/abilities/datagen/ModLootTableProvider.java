package robot.abilities.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import robot.abilities.block.ModBlocks;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.MITHRIL_BLOCK);
        addDrop(ModBlocks.MITHRIL_ORE);
        addDrop(ModBlocks.DOREEL_BLOCK);
        addDrop(ModBlocks.DOREEL_ORE);
    }
}