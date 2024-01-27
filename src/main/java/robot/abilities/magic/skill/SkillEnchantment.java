package robot.abilities.magic.skill;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;

public class SkillEnchantment extends Enchantment {

    private final String namespace, name;
    private final int minLevel, maxLevel;

    private final onTargetDamagedHandler onTargetDamaged;
    private final onUserDamagedHandler onUserDamaged;
    private final onUsedHandler onUsed;

    public SkillEnchantment(String namespace, String name, Rarity rarity, EnchantmentTarget target, EquipmentSlot[] slotTypes, int minLevel, int maxLevel, onUsedHandler onUsed, onTargetDamagedHandler onTargetDamaged, onUserDamagedHandler onUserDamaged) {
        super(rarity, target, slotTypes);
        this.namespace = namespace;
        this.name = name;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.onUsed = onUsed;
        this.onTargetDamaged = onTargetDamaged;
        this.onUserDamaged = onUserDamaged;
    }

    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getID() {
        return "%s:%s".formatted(namespace, name);
    }

    public String getTranslateKey() {
        return "enchantment.%s.skill.%s".formatted(namespace, name);
    }

    @Override
    public int getMinLevel() {
        return this.minLevel;
    }

    @Override
    public int getMaxLevel() {
        return this.maxLevel;
    }

    public boolean onUsed(LivingEntity user, int level) {
        if (onUsed == null) return false;
        return onUsed.onUsed(user, level);
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (onTargetDamaged != null) onTargetDamaged.onTargetDamaged(user, target, level);
    }

    @Override
    public void onUserDamaged(LivingEntity user, Entity attacker, int level) {
        if (onUserDamaged != null) onUserDamaged.onUserDamaged(user, attacker, level);
    }

    public static Builder builder(String id) {
        return new Builder(id.substring(0, id.indexOf(":")), id.substring(id.indexOf(":") + 1));
    }

    public static Builder builder(String namespace, String name) {
        return new Builder(namespace, name);
    }

    public static class Builder {
        private final String namespace, name;
        private Rarity rarity;
        private EnchantmentTarget target;
        private EquipmentSlot[] slotTypes;
        private int min, max;
        private onUsedHandler onUsedHandler;
        private onTargetDamagedHandler targetDamagedHandler;
        private onUserDamagedHandler onUserDamagedHandler;

        public Builder(String namespace, String name) {
            this.namespace = namespace;
            this.name = name;
            this.rarity = Rarity.UNCOMMON;
            this.target = EnchantmentTarget.WEAPON;
            this.slotTypes = new EquipmentSlot[]{EquipmentSlot.MAINHAND};
            this.min = 1;
            this.max = 5;
        }

        public Builder rarity(Rarity rarity) {
            this.rarity = rarity;
            return this;
        }

        public Builder target(EnchantmentTarget target) {
            this.target = target;
            return this;
        }

        public Builder slotTypes(EquipmentSlot[] slotTypes) {
            this.slotTypes = slotTypes;
            return this;
        }

        public Builder levels(int min, int max) {
            this.min = Math.min(min, max);
            this.max = Math.max(min, max);
            return this;
        }

        public Builder onUsed(onUsedHandler onUsedHandler) {
            this.onUsedHandler = onUsedHandler;
            return this;
        }

        public Builder onTargetDamaged(onTargetDamagedHandler targetDamagedHandler) {
            this.targetDamagedHandler = targetDamagedHandler;
            return this;
        }

        public Builder onUserDamaged(onUserDamagedHandler onUserDamagedHandler) {
            this.onUserDamagedHandler = onUserDamagedHandler;
            return this;
        }

        public SkillEnchantment build() {
            return new SkillEnchantment(this.namespace, this.name, this.rarity, this.target, this.slotTypes, this.min, this.max, this.onUsedHandler, this.targetDamagedHandler, this.onUserDamagedHandler);
        }
    }

    @FunctionalInterface
    public interface onUsedHandler {
        boolean onUsed(LivingEntity user, int level);
    }

    @FunctionalInterface
    public interface onTargetDamagedHandler {
        void onTargetDamaged(LivingEntity user, Entity target, int level);
    }

    @FunctionalInterface
    public interface onUserDamagedHandler {
        void onUserDamaged(LivingEntity user, Entity attacker, int level);
    }
}


