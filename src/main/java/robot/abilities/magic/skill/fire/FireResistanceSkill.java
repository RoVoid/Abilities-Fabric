package robot.abilities.magic.skill.fire;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import robot.abilities.AbilitiesMod;
import robot.abilities.magic.skill.AbstractSkill;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IEntityDataSaver;

public class FireResistanceSkill extends AbstractSkill {
    Property time = new Property(100);

    public FireResistanceSkill() {
        super(AbilitiesMod.ID + ":fire_resistance", new Property(3, 0.01), new Property(1));
    }

    public void use(LivingEntity entity, int level) {
        double mp = getMp().get(level);
        IEntityDataSaver cap = (IEntityDataSaver) entity;
        if (entity.getWorld().isClient || cap.get(DataKeys.MP) < mp) return;
        cap.add(DataKeys.MP, -mp);
        cap.add(DataKeys.SCORE, 2);
        cap.sync(entity, DataKeys.MP, DataKeys.SCORE);
        StatusEffectInstance customEffect = new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, (int) time.get(level), 0);
        entity.addStatusEffect(customEffect);
    }
}
