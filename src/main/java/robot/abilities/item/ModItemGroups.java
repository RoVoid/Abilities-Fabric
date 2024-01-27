package robot.abilities.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;

import static robot.abilities.block.ModBlocks.*;
import static robot.abilities.item.ModArmors.DOREEL_ARMOR;
import static robot.abilities.item.ModArmors.MITHRIL_ARMOR;
import static robot.abilities.item.ModItems.*;

public class ModItemGroups {
    public static final ItemGroup ITEM_GROUP =
            Registry.register(Registries.ITEM_GROUP, new Identifier(AbilitiesMod.ID, "main"),
                    FabricItemGroup.builder()
                            .icon(() -> new ItemStack(DISTORTED_BERRIES))
                            .displayName(Text.translatable("itemGroup.abilities.main"))
                            .entries((context, entries) -> {
                                entries.add(FIRE_CUBE);
                                entries.add(WATER_CUBE);
                                entries.add(EARTH_CUBE);
                                entries.add(AIR_CUBE);
                                entries.add(MITHRIL_INGOT);
                                entries.add(MITHRIL_NUGGET);
                                entries.add(MITHRIL_BLOCK);
                                entries.add(MITHRIL_ORE);
                                entries.add(MITHRIL_ARMOR.HELMET);
                                entries.add(MITHRIL_ARMOR.CHESTPLATE);
                                entries.add(MITHRIL_ARMOR.LEGGINGS);
                                entries.add(MITHRIL_ARMOR.BOOTS);
                                entries.add(MITHRIL_SWORD);
                                entries.add(MITHRIL_AXE);
                                entries.add(MITHRIL_PICKAXE);
                                entries.add(MITHRIL_SHOVEL);
                                entries.add(MITHRIL_HOE);
                                entries.add(MITHRIL_ARROW);
                                entries.add(MITHRIL_STICK);
                                entries.add(DOREEL_INGOT);
                                entries.add(DOREEL_NUGGET);
                                entries.add(DOREEL_BLOCK);
                                entries.add(DOREEL_ORE);
                                entries.add(DOREEL_ARMOR.HELMET);
                                entries.add(DOREEL_ARMOR.CHESTPLATE);
                                entries.add(DOREEL_ARMOR.LEGGINGS);
                                entries.add(DOREEL_ARMOR.BOOTS);
                                entries.add(DOREEL_SWORD);
                                entries.add(DOREEL_AXE);
                                entries.add(DOREEL_PICKAXE);
                                entries.add(DOREEL_SHOVEL);
                                entries.add(DOREEL_HOE);
                                entries.add(DOREEL_STICK);
                                entries.add(DISTORTED_BERRIES);
                                entries.add(SOUL_FURNACE);
                                entries.add(ALTAR);
                                entries.add(WILD_MAGIC_BEACON);
                            })
                            .build());

    public static void register() {
    }
}
