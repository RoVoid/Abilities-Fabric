package robot.abilities.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;
import robot.abilities.world.feature.ModPlacedFeatures;

public class ModOreGeneration {
    public static void generateOres() {
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES, ModPlacedFeatures.MITHRIL_ORE_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES, ModPlacedFeatures.DOREEL_ORE_PLACED_KEY);

//        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(),
//                GenerationStep.Feature.UNDERGROUND_ORES, ModPlacedFeatures.NETHER_RUBY_ORE_PLACED_KEY);
//
//        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(),
//                GenerationStep.Feature.UNDERGROUND_ORES, ModPlacedFeatures.END_RUBY_ORE_PLACED_KEY);
    }
}
