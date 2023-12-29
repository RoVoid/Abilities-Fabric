package robot.abilities.block;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;

public class ModBlocks {

    public static final Block MITHRIL_BLOCK =
            registerBlockWithItem("mithril_block",
                    new Block(AbstractBlock.Settings.create().strength(2f).requiresTool()));

    public static final Block DISTORTED_BERRY_BUSH_BLOCK = registerBlock("distorted_berry_bush",
            new DistortedBerryBushBlock(FabricBlockSettings.copy(Blocks.SWEET_BERRY_BUSH)));


    private static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(AbilitiesMod.ID, name), block);
    }

    private static Block registerBlockWithItem(String name, Block block) {
        Registry.register(Registries.ITEM, new Identifier(AbilitiesMod.ID, name),
                new BlockItem(block, new FabricItemSettings()));
        return Registry.register(Registries.BLOCK, new Identifier(AbilitiesMod.ID, name), block);
    }

    public static void register() {
        AbilitiesMod.LOGGER.debug("Registering ModBlocks for " + AbilitiesMod.ID);
    }

    public static void registerRender() {
        BlockRenderLayerMap.INSTANCE.putBlock(DISTORTED_BERRY_BUSH_BLOCK, RenderLayer.getCutout());
    }
}
