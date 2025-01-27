package robot.abilities.magic.skill.fire;

import net.minecraft.entity.LivingEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import robot.abilities.AbilitiesMod;
import robot.abilities.entity.FireBallEntity;
import robot.abilities.magic.property.DoubleProperty;
import robot.abilities.magic.property.IntProperty;
import robot.abilities.magic.skill.Skill;
import robot.abilities.util.Utils;

public class FireBallSkill extends Skill {
    public final DoubleProperty EXPLODE = new DoubleProperty(0.1, 0.05);
    public final DoubleProperty DAMAGE = new DoubleProperty(1.0, 0.05);

    public FireBallSkill() {
        super(AbilitiesMod.ID, "fireball", Type.ATTACK, Rarity.COMMON, new DoubleProperty(1.0, 0.02), new IntProperty(2, 2));
        icon();
    }

    public boolean use(LivingEntity user, int level) {
        if (user.getWorld().isClient) return false;
        Vec3d look = user.getRotationVec(1.0f);
        float speed = 1.75f;
        FireBallEntity fireball = new FireBallEntity(user.getWorld(), user, look.x * speed, look.y * speed, look.z * speed, EXPLODE.get(level));
        fireball.setDamage(DAMAGE.get(level));
        user.getWorld().spawnEntity(fireball);
        return true;
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip",
                Text.literal(Utils.decimal(DAMAGE.get(level))).formatted(Formatting.GOLD),
                Text.literal(Utils.decimal(EXPLODE.get(level))).formatted(Formatting.GOLD),
                Text.literal(Utils.decimal(MP.get(level))).formatted(Formatting.GOLD));
    }
}
