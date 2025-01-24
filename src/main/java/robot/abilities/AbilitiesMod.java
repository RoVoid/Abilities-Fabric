package robot.abilities;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeatures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import robot.abilities.block.ModBlockEntities;
import robot.abilities.block.ModBlocks;
import robot.abilities.effect.ModEffects;
import robot.abilities.entity.ModEntities;
import robot.abilities.event.ModEvents;
import robot.abilities.item.ModItemGroups;
import robot.abilities.item.ModItems;
import robot.abilities.magic.ModMagics;
import robot.abilities.magic.skill.ModSkills;
import robot.abilities.network.ModMessages;
import robot.abilities.particle.ModParticles;
import robot.abilities.world.gen.ModWorldGeneration;
import software.bernie.geckolib.GeckoLib;


public class AbilitiesMod implements ModInitializer {
    public static final String ID = "abilities";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    @Override
    public void onInitialize() {
        GeckoLib.initialize();
        ModItems.register();
        ModBlocks.init();
        ModBlockEntities.init();
        ModItemGroups.init();
        ModEntities.register();
        ModMessages.registerC2SPackets();
        ModEvents.register();
        ModWorldGeneration.generateModWorldGen();
        ModMagics.init();
        ModSkills.init();
        ModEffects.init();
        ModParticles.init();
        BiomeModifications.addFeature(BiomeSelectors.all(), GenerationStep.Feature.UNDERGROUND_DECORATION, PlacedFeatures.of("abilities:crystal_geode"));
    }
}