package robot.abilities.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;
import robot.abilities.block.ModBlocks;
import robot.abilities.item.armor.ModArmors;

public class ModItems {
    public static final Item MITHRIL_INGOT = registerItem("mithril_ingot", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item DISTORTED_BERRIES = registerItem("distorted_berries",
            new AliasedBlockItem(ModBlocks.DISTORTED_BERRY_BUSH_BLOCK,
                    new FabricItemSettings().maxCount(64)
                            .food(new FoodComponent.Builder().hunger(6).saturationModifier(8f).build())));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(AbilitiesMod.ID, name), item);
    }

    public static void register() {
        ModArmors.register();
        AbilitiesMod.LOGGER.debug("Registering ModItems for " + AbilitiesMod.ID);
    }
}
