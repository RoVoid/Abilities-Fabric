package robot.abilities.block;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;

public class ModBlocks {

    public static final Block CRYSTAL_BLOCK = registerBlockWithItem("crystal_block", new Block(AbstractBlock.Settings.create().strength(1f).requiresTool().luminance((state) -> 5)));
    public static final Block CRYSTAL_BUD = registerBlockWithItem("crystal_bud", new CrystalBudBlock(AbstractBlock.Settings.create().strength(1f).requiresTool()));
    public static final Block SOUL_FURNACE = registerBlockWithItem("soul_furnace", new SoulFurnaceBlock(AbstractBlock.Settings.create().strength(1f).requiresTool()));
    public static final Block MITHRIL_BLOCK = registerBlockWithItem("mithril_block", new Block(AbstractBlock.Settings.create().strength(2f).requiresTool()));
    public static final Block DOREEL_BLOCK = registerBlockWithItem("doreel_block", new Block(AbstractBlock.Settings.create().strength(2f).requiresTool()));
    public static final Block MITHRIL_ORE = registerBlockWithItem("mithril_ore", new Block(AbstractBlock.Settings.create().strength(2f).requiresTool()));
    public static final Block DOREEL_ORE = registerBlockWithItem("doreel_ore", new Block(AbstractBlock.Settings.create().strength(2f).requiresTool()));
    public static final Block DISTORTED_BERRY_BUSH_BLOCK = registerBlock("distorted_berry_bush", new DistortedBerryBushBlock(FabricBlockSettings.copy(Blocks.SWEET_BERRY_BUSH)));
    public static final Block WILD_MAGIC_BEACON = registerBlockWithItem("wild_magic_beacon", new WildMagicBeacon(AbstractBlock.Settings.create().strength(2f).requiresTool()));
    public static final Block ALTAR = registerBlockWithItem("cubes_altar", new CubesAltarBlock(AbstractBlock.Settings.create().strength(2f).requiresTool()));
    public static final Block MAGIC_CIRCLE = registerBlockWithItem("magic_circle", new MagicCircleBlock(AbstractBlock.Settings.create()));
    public static final Block LECTERN = registerBlockWithItem("lectern", new LecternBlock(AbstractBlock.Settings.create()));


    private static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(AbilitiesMod.ID, name), block);
    }

    private static Block registerBlockWithItem(String name, Block block) {
        Registry.register(Registries.ITEM, new Identifier(AbilitiesMod.ID, name), new BlockItem(block, new FabricItemSettings()));
        return Registry.register(Registries.BLOCK, new Identifier(AbilitiesMod.ID, name), block);
    }

    public static void register() {
    }

    public static void registerRender() {
        BlockRenderLayerMap.INSTANCE.putBlock(DISTORTED_BERRY_BUSH_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(WILD_MAGIC_BEACON, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CRYSTAL_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ALTAR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(MAGIC_CIRCLE, RenderLayer.getCutout());
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> BiomeColors.getWaterColor(view, pos), ALTAR);
    }
}
