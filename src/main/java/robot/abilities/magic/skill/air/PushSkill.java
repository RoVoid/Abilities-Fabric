package robot.abilities.magic.skill.air;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import robot.abilities.AbilitiesMod;
import robot.abilities.magic.property.DoubleProperty;
import robot.abilities.magic.property.IntProperty;
import robot.abilities.magic.skill.Skill;
import robot.abilities.util.Utils;

import java.util.List;

public class PushSkill extends Skill {
    public final DoubleProperty PUSH = new DoubleProperty(1.2, 0.2);

    public PushSkill() {
        super(AbilitiesMod.ID, "push", Type.ATTACK, Rarity.COMMON, new DoubleProperty(1.0), new IntProperty(10));
    }

    public static boolean shouldDamageAttacker(int level, Random random) {
        if (level <= 0) {
            return false;
        }
        return random.nextFloat() < 0.015f * (float) level;
    }

    private static boolean isEntityInFront(LivingEntity user, Entity entity) {
        Vec3d playerLook = user.getRotationVector();
        Vec3d entityPos = entity.getPos().subtract(user.getPos());
        return playerLook.dotProduct(entityPos.normalize()) > 0.8;
    }

    @Override
    public boolean use(LivingEntity user, int level) {
        if (user.getWorld().isClient) return false;
        Vec3d pos = user.getPos();
        Vec3d look = user.getRotationVector().normalize();
        Box searchBox = new Box(
                pos.add(-5, -5, -5),
                pos.add(5, 5, 5)
        );
        List<LivingEntity> entities = user.getWorld().getOtherEntities(user, searchBox).stream().filter((e) -> e instanceof LivingEntity).map((e) -> (LivingEntity) e).filter((e) -> isEntityInFront(user, e)).toList();
        for (LivingEntity entity : entities) {
            Vec3d entityPos = entity.getPos();
            Vec3d toEntity = entityPos.subtract(pos).normalize();
            double angle = look.dotProduct(toEntity);
            double distance = pos.distanceTo(entityPos);
            double forceStrength = PUSH.get(level) * angle / distance;
            entity.velocityModified = true;
            entity.addVelocity(look.multiply(forceStrength));
        }
        return true;
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip",
                Text.literal(Utils.decimal("#.#", PUSH.get(level))).formatted(Formatting.GOLD),
                Text.literal(Utils.decimal("#.#", MP.get(level))).formatted(Formatting.GOLD));
    }
}
