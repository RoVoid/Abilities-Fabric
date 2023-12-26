package robot.abilities.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;
import robot.abilities.item.armor.ArmorsInit;

public class ItemsInit {
    public static final Item MITHRIL_INGOT = new Item(new FabricItemSettings().maxCount(64));

    public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(MITHRIL_INGOT))
            .displayName(Text.translatable("itemGroup.abilities.main"))
            .entries((context, entries) -> {
                entries.add(MITHRIL_INGOT);
                entries.add(ArmorsInit.MITHRIL_ARMOR.HELMET);
                entries.add(ArmorsInit.MITHRIL_ARMOR.CHESTPLATE);
                entries.add(ArmorsInit.MITHRIL_ARMOR.LEGGINGS);
                entries.add(ArmorsInit.MITHRIL_ARMOR.BOOTS);
            })
            .build();



    public static void register() {
        Registry.register(Registries.ITEM, new Identifier(AbilitiesMod.ID, "mithril_ingot"), MITHRIL_INGOT);
        ArmorsInit.register();
        Registry.register(Registries.ITEM_GROUP, new Identifier(AbilitiesMod.ID, "main"), ITEM_GROUP);
    }
}
