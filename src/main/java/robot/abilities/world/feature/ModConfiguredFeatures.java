package robot.abilities.world.feature;

import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import robot.abilities.AbilitiesMod;
import robot.abilities.block.CrystalBudBlock;
import robot.abilities.block.ModBlocks;

import java.util.List;

public class ModConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> MITHRIL_ORE_KEY = registerKey("mithril_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> DOREEL_ORE_KEY = registerKey("doreel_ore");
//    public static final RegistryKey<ConfiguredFeature<?, ?>> NETHER_RUBY_ORE_KEY = registerKey("nether_ruby_ore");
//    public static final RegistryKey<ConfiguredFeature<?, ?>> END_RUBY_ORE_KEY = registerKey("end_ruby_ore");

    public static void boostrap(Registerable<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplacables = new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplacables = new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest netherReplacables = new TagMatchRuleTest(BlockTags.BASE_STONE_NETHER);
        RuleTest endReplacables = new BlockMatchRuleTest(Blocks.END_STONE);

        List<OreFeatureConfig.Target> overworldMithrilOres = List.of(OreFeatureConfig.createTarget(stoneReplacables, ModBlocks.MITHRIL_ORE.getDefaultState()));
        List<OreFeatureConfig.Target> overworldDoreelOres = List.of(OreFeatureConfig.createTarget(stoneReplacables, ModBlocks.DOREEL_ORE.getDefaultState()));

//        List<OreFeatureConfig.Target> netherRubyOres =
//                List.of(OreFeatureConfig.createTarget(netherReplacables, ModBlocks.NETHER_RUBY_ORE.getDefaultState()));
//
//        List<OreFeatureConfig.Target> endRubyOres =
//                List.of(OreFeatureConfig.createTarget(endReplacables, ModBlocks.END_STONE_RUBY_ORE.getDefaultState()));

        register(context, MITHRIL_ORE_KEY, Feature.ORE, new OreFeatureConfig(overworldMithrilOres, 12));
        register(context, DOREEL_ORE_KEY, Feature.ORE, new OreFeatureConfig(overworldDoreelOres, 10));
        //register(context, NETHER_RUBY_ORE_KEY, Feature.ORE, new OreFeatureConfig(netherRubyOres, 12));
        //register(context, END_RUBY_ORE_KEY, Feature.ORE, new OreFeatureConfig(endRubyOres, 12));
    }

    public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier(AbilitiesMod.ID, name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context, RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
