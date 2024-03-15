package robot.abilities.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;
import robot.abilities.block.ModBlocks;
import robot.abilities.item.cubes.AirCube;
import robot.abilities.item.cubes.EarthCube;
import robot.abilities.item.cubes.FireCube;
import robot.abilities.item.cubes.WaterCube;
import robot.abilities.item.food.DistortedBerries;

public class ModItems {
    public static final Item FIRE_CUBE = registerItem("cube_fire", new FireCube(new FabricItemSettings().maxCount(1)));
    public static final Item WATER_CUBE = registerItem("cube_water", new WaterCube(new FabricItemSettings().maxCount(1)));
    public static final Item EARTH_CUBE = registerItem("cube_earth", new EarthCube(new FabricItemSettings().maxCount(1)));
    public static final Item AIR_CUBE = registerItem("cube_air", new AirCube(new FabricItemSettings().maxCount(64)));
    public static final Item CRYSTAL_SHARD = registerItem("crystal_shard", new Item(new FabricItemSettings().maxCount(16)));
    public static final Item MITHRIL_INGOT = registerItem("mithril_ingot", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item DOREEL_INGOT = registerItem("doreel_ingot", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item MITHRIL_NUGGET = registerItem("mithril_nugget", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item DOREEL_NUGGET = registerItem("doreel_nugget", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item MITHRIL_STICK = registerItem("mithril_stick", new Item(new FabricItemSettings().maxCount(64)));
    public static final Item DOREEL_STICK = registerItem("doreel_stick", new Item(new FabricItemSettings().maxCount(64)));

    public static final Item DISTORTED_BERRIES = registerItem("distorted_berries",
            new DistortedBerries(ModBlocks.DISTORTED_BERRY_BUSH_BLOCK,
                    new FabricItemSettings().maxCount(64)
                            .food(new FoodComponent.Builder().hunger(6).saturationModifier(8f).build()), 5));
    public static final ToolItem MITHRIL_SWORD =
            registerToolItem("mithril_sword",
                    new SwordItem(ModToolMaterials.MITHRIL, 7, -0.2f, new FabricItemSettings()));

    public static final ToolItem MITHRIL_AXE =
            registerToolItem("mithril_axe",
                    new AxeItem(ModToolMaterials.MITHRIL, 11, -2.8f, new FabricItemSettings()));

    public static final ToolItem MITHRIL_PICKAXE =
            registerToolItem("mithril_pickaxe",
                    new PickaxeItem(ModToolMaterials.MITHRIL, 2, -1f, new FabricItemSettings()));

    public static final ToolItem MITHRIL_SHOVEL =
            registerToolItem("mithril_shovel",
                    new ShovelItem(ModToolMaterials.MITHRIL, 2, -1f, new FabricItemSettings()));

    public static final ToolItem MITHRIL_HOE =
            registerToolItem("mithril_hoe",
                    new HoeItem(ModToolMaterials.MITHRIL, 2, -1f, new FabricItemSettings()));

    public static final ToolItem DOREEL_SWORD =
            registerToolItem("doreel_sword",
                    new SwordItem(ModToolMaterials.DOREEL, 7, -0.2f, new FabricItemSettings()));

    public static final ToolItem DOREEL_AXE =
            registerToolItem("doreel_axe",
                    new AxeItem(ModToolMaterials.DOREEL, 11, -2.8f, new FabricItemSettings()));

    public static final ToolItem DOREEL_PICKAXE =
            registerToolItem("doreel_pickaxe",
                    new PickaxeItem(ModToolMaterials.DOREEL, 2, -1.8f, new FabricItemSettings()));

    public static final ToolItem DOREEL_SHOVEL =
            registerToolItem("doreel_shovel",
                    new ShovelItem(ModToolMaterials.DOREEL, 2, -1.8f, new FabricItemSettings()));

    public static final ToolItem DOREEL_HOE =
            registerToolItem("doreel_hoe",
                    new HoeItem(ModToolMaterials.DOREEL, 2, -1.8f, new FabricItemSettings()));

    public static final Item MITHRIL_ARROW =
            registerItem("mithril_arrow",
                    new CustomArrowItem(new FabricItemSettings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(AbilitiesMod.ID, name), item);
    }

    private static ToolItem registerToolItem(String name, ToolItem item) {
        return Registry.register(Registries.ITEM, new Identifier(AbilitiesMod.ID, name), item);
    }

    public static void register() {
        ModArmors.register();
    }
}
