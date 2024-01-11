package robot.abilities.magic.skill.fire;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.math.Vec3d;
import robot.abilities.AbilitiesMod;
import robot.abilities.magic.skill.AbstractSkill;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IEntityDataSaver;

public class FireBallSkill extends AbstractSkill {
    public FireBallSkill() {
        super(AbilitiesMod.ID + ":fireball", new Property(1, 0.02), new Property(1));
    }

    public void use(LivingEntity entity, int level) {
        double mp = getMp().get(level);
        IEntityDataSaver cap = (IEntityDataSaver) entity;
        if (entity.getWorld().isClient || cap.get(DataKeys.MP) < mp) return;
        cap.add(DataKeys.MP, -mp);
        cap.add(DataKeys.SCORE, 5);
        cap.sync(entity, DataKeys.MP, DataKeys.SCORE);
        Vec3d look = entity.getRotationVec(1.0f);
        float speed = 1.5f;
        FireballEntity fireball = new FireballEntity(entity.getWorld(), (LivingEntity) entity, look.x * speed, look.y * speed, look.z * speed, 1);
        fireball.setPos(entity.getX() + look.x * 1.5, entity.getY() + look.y + entity.getEyeHeight(entity.getPose()), entity.getZ() + look.z * 1.5);
        entity.getWorld().spawnEntity(fireball);
    }
}
