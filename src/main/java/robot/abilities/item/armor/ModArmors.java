package robot.abilities.item.armor;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;

public class ModArmors {
    public static final ArmorMaterial MITHRIL_ARMOR_MATERIAL = new MithrilArmorMaterial();
    public static final CustomArmor MITHRIL_ARMOR = new CustomArmor(MITHRIL_ARMOR_MATERIAL);
    public static void register() {
        registerArmor(MITHRIL_ARMOR, "mithril");
        AbilitiesMod.LOGGER.debug("Registering ModArmors for " + AbilitiesMod.ID);
    }
    public static void registerArmor(CustomArmor armor, String name){
        Registry.register(Registries.ITEM, new Identifier(AbilitiesMod.ID, name + "_helmet"), armor.HELMET);
        Registry.register(Registries.ITEM, new Identifier(AbilitiesMod.ID, name + "_chestplate"), armor.CHESTPLATE);
        Registry.register(Registries.ITEM, new Identifier(AbilitiesMod.ID, name + "_leggings"), armor.LEGGINGS);
        Registry.register(Registries.ITEM, new Identifier(AbilitiesMod.ID, name + "_boots"), armor.BOOTS);
    }
    public static class CustomArmor{
        public Item HELMET, CHESTPLATE, LEGGINGS, BOOTS;
        public CustomArmor(ArmorMaterial material){
            HELMET = new ArmorItem(material, ArmorItem.Type.HELMET, new Item.Settings());
            CHESTPLATE = new ArmorItem(material, ArmorItem.Type.CHESTPLATE, new Item.Settings());
            LEGGINGS = new ArmorItem(material, ArmorItem.Type.LEGGINGS, new Item.Settings());
            BOOTS = new ArmorItem(material, ArmorItem.Type.BOOTS, new Item.Settings());
        }
    }

}
