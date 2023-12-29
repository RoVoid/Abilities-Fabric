package robot.abilities.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;

import static robot.abilities.block.ModBlocks.MITHRIL_BLOCK;
import static robot.abilities.item.ModItems.DISTORTED_BERRIES;
import static robot.abilities.item.ModItems.MITHRIL_INGOT;
import static robot.abilities.item.armor.ModArmors.MITHRIL_ARMOR;

public class ModItemGroups {
    public static final ItemGroup ITEM_GROUP =
            Registry.register(Registries.ITEM_GROUP, new Identifier(AbilitiesMod.ID, "main"),
                    FabricItemGroup.builder()
                            .icon(() -> new ItemStack(MITHRIL_INGOT))
                            .displayName(Text.translatable("itemGroup.abilities.main"))
                            .entries((context, entries) -> {
                                entries.add(MITHRIL_INGOT);
                                entries.add(MITHRIL_BLOCK);
                                entries.add(MITHRIL_ARMOR.HELMET);
                                entries.add(MITHRIL_ARMOR.CHESTPLATE);
                                entries.add(MITHRIL_ARMOR.LEGGINGS);
                                entries.add(MITHRIL_ARMOR.BOOTS);
                                entries.add(DISTORTED_BERRIES);
                            })
                            .build());

    public static void register() {
        AbilitiesMod.LOGGER.debug("Registering ModItemGroups for " + AbilitiesMod.ID);
    }
}
