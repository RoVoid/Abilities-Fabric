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
    public static final ItemGroup CUBES_ITEM_GROUP =
            Registry.register(Registries.ITEM_GROUP, new Identifier(AbilitiesMod.ID, "cubes"),
                    FabricItemGroup.builder()
                            .icon(() -> new ItemStack(FIRE_CUBE))
                            .displayName(Text.translatable("itemGroup.abilities.cubes"))
                            .entries((context, entries) -> {
                                entries.add(ALTAR);
                                entries.add(FIRE_CUBE);
                                entries.add(WATER_CUBE);
                                entries.add(EARTH_CUBE);
                                entries.add(AIR_CUBE);
                            })
                            .build());

    public static final ItemGroup METAL_ITEM_GROUP =
            Registry.register(Registries.ITEM_GROUP, new Identifier(AbilitiesMod.ID, "metal"),
                    FabricItemGroup.builder()
                            .icon(() -> new ItemStack(DOREEL_ARMOR.HELMET))
                            .displayName(Text.translatable("itemGroup.abilities.metal"))
                            .entries((context, entries) -> {
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
                            })
                            .build());

    public static final ItemGroup OTHER_ITEM_GROUP =
            Registry.register(Registries.ITEM_GROUP, new Identifier(AbilitiesMod.ID, "other"),
                    FabricItemGroup.builder()
                            .icon(() -> new ItemStack(DISTORTED_BERRIES))
                            .displayName(Text.translatable("itemGroup.abilities.other"))
                            .entries((context, entries) -> {
                                entries.add(DISTORTED_BERRIES);
                                entries.add(SOUL_FURNACE);
                                entries.add(CRYSTAL_BUD);
                                entries.add(CRYSTAL_BLOCK);
                                entries.add(CRYSTAL_SHARD);
                                entries.add(CIRCLE_TEMPLATE);
                                entries.add(POWDER);
                                entries.add(CATALYST);
                                entries.add(SKILL_BOOK);
                                entries.add(LECTERN);
                                entries.add(MANA_CRYSTAL);
                            })
                            .build());

    public static void register() {
    }
}
