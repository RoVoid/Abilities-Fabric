package robot.abilities.magic.skill;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;

public class SkillEnchantment extends Enchantment {
    private final int minLevel, maxLevel;

    private final onTargetDamagedHandler onTargetDamaged;
    private final onUserDamagedHandler onUserDamaged;
    private final onUsedHandler onUsed;

    public SkillEnchantment(Rarity rarity, EnchantmentTarget target, EquipmentSlot[] slotTypes, int minLevel, int maxLevel, onUsedHandler onUsed, onTargetDamagedHandler onTargetDamaged, onUserDamagedHandler onUserDamaged) {
        super(rarity, target, slotTypes);
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.onUsed = onUsed;
        this.onTargetDamaged = onTargetDamaged;
        this.onUserDamaged = onUserDamaged;
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
        return onUsed != null && onUsed.onUsed(user, level);
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (onTargetDamaged != null) onTargetDamaged.onTargetDamaged(user, target, level);
    }

    @Override
    public void onUserDamaged(LivingEntity user, Entity attacker, int level) {
        if (onUserDamaged != null) onUserDamaged.onUserDamaged(user, attacker, level);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Rarity rarity;
        private EnchantmentTarget target;
        private EquipmentSlot[] slotTypes;
        private int min, max;
        private onUsedHandler onUsedHandler;
        private onTargetDamagedHandler targetDamagedHandler;
        private onUserDamagedHandler onUserDamagedHandler;

        public Builder() {
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
            return new SkillEnchantment(this.rarity, this.target, this.slotTypes, this.min, this.max, this.onUsedHandler, this.targetDamagedHandler, this.onUserDamagedHandler);
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


