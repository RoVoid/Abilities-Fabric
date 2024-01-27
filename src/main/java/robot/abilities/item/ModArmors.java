package robot.abilities.item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;

import java.util.function.Supplier;

public class ModArmors {
    public static final CustomArmor MITHRIL_ARMOR = new CustomArmor(ModArmorMaterials.MITHRIL);
    public static final CustomArmor DOREEL_ARMOR = new CustomArmor(ModArmorMaterials.DOREEL);

    public static void register() {
        registerArmor(MITHRIL_ARMOR, "mithril");
        registerArmor(DOREEL_ARMOR, "doreel");
    }

    public static void registerArmor(CustomArmor armor, String name) {
        Registry.register(Registries.ITEM, new Identifier(AbilitiesMod.ID, name + "_helmet"), armor.HELMET);
        Registry.register(Registries.ITEM, new Identifier(AbilitiesMod.ID, name + "_chestplate"), armor.CHESTPLATE);
        Registry.register(Registries.ITEM, new Identifier(AbilitiesMod.ID, name + "_leggings"), armor.LEGGINGS);
        Registry.register(Registries.ITEM, new Identifier(AbilitiesMod.ID, name + "_boots"), armor.BOOTS);
    }

    public enum ModArmorMaterials implements ArmorMaterial {
        MITHRIL("mithril", 15, new int[]{3, 8, 6, 3}, 19,
                SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 1f, 0.01f,
                () -> Ingredient.ofItems(ModItems.MITHRIL_INGOT)),
        DOREEL("doreel", 25, new int[]{6, 12, 8, 5}, 5,
                SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 3f, 0.4f,
                () -> Ingredient.ofItems(ModItems.DOREEL_INGOT));

        private static final int[] BASE_DURABILITY = {11, 16, 15, 13};
        private final String name;
        private final int durabilityMultiplier;
        private final int[] protectionAmounts;
        private final int enchantability;
        private final SoundEvent equipSound;
        private final float toughness;
        private final float knockbackResistance;
        private final Supplier<Ingredient> repairIngredient;

        ModArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound,
                          float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
            this.name = name;
            this.durabilityMultiplier = durabilityMultiplier;
            this.protectionAmounts = protectionAmounts;
            this.enchantability = enchantability;
            this.equipSound = equipSound;
            this.toughness = toughness;
            this.knockbackResistance = knockbackResistance;
            this.repairIngredient = repairIngredient;
        }

        @Override
        public int getDurability(ArmorItem.Type type) {
            return BASE_DURABILITY[type.ordinal()] * this.durabilityMultiplier;
        }

        @Override
        public int getProtection(ArmorItem.Type type) {
            return protectionAmounts[type.ordinal()];
        }

        @Override
        public int getEnchantability() {
            return this.enchantability;
        }

        @Override
        public SoundEvent getEquipSound() {
            return this.equipSound;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return this.repairIngredient.get();
        }

        @Override
        public String getName() {
            return AbilitiesMod.ID + ":" + this.name;
        }

        @Override
        public float getToughness() {
            return this.toughness;
        }

        @Override
        public float getKnockbackResistance() {
            return this.knockbackResistance;
        }
    }

    public static class CustomArmor {
        public Item HELMET, CHESTPLATE, LEGGINGS, BOOTS;

        public CustomArmor(ArmorMaterial material) {
            HELMET = new ArmorItem(material, ArmorItem.Type.HELMET, new Item.Settings());
            CHESTPLATE = new ArmorItem(material, ArmorItem.Type.CHESTPLATE, new Item.Settings());
            LEGGINGS = new ArmorItem(material, ArmorItem.Type.LEGGINGS, new Item.Settings());
            BOOTS = new ArmorItem(material, ArmorItem.Type.BOOTS, new Item.Settings());
        }
    }
}
