package robot.abilities.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import robot.abilities.item.ModItems;

import static robot.abilities.block.ModBlocks.*;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(MITHRIL_BLOCK);
        addDrop(MITHRIL_ORE);
        addDrop(DOREEL_BLOCK);
        addDrop(DOREEL_ORE);
        addDrop(CRYSTAL_BUD, ModItems.CRYSTAL_SHARD);
    }
}