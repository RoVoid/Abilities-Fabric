package robot.abilities.magic.skill.water;

import net.minecraft.entity.LivingEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import robot.abilities.AbilitiesMod;
import robot.abilities.entity.WaterBallEntity;
import robot.abilities.magic.property.DoubleProperty;
import robot.abilities.magic.property.IntProperty;
import robot.abilities.magic.skill.Skill;
import robot.abilities.util.Utils;

public class WaterBallSkill extends Skill {
    public final DoubleProperty DAMAGE = new DoubleProperty(1.0, 0.05);

    public WaterBallSkill() {
        super(AbilitiesMod.ID, "water_ball", Type.ATTACK, Rarity.COMMON, new DoubleProperty(1.0, 0.02), new IntProperty(2, 2));
    }

    public boolean use(LivingEntity user, int level) {
        if (user.getWorld().isClient) return false;
        Vec3d look = user.getRotationVec(1.0f);
        float speed = 1.5f;
        WaterBallEntity waterBall = new WaterBallEntity(user.getWorld(), user, look.x * speed, look.y * speed, look.z * speed);
        waterBall.setDamage(DAMAGE.get(level));
        waterBall.updatePosition(user.getX() + look.x * 1.2, user.getY() + look.y + user.getEyeHeight(user.getPose()), user.getZ() + look.z * 1.2);
        user.getWorld().spawnEntity(waterBall);
        return true;
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip",
                Text.literal(Utils.decimal("#.#", DAMAGE.get(level))).formatted(Formatting.GOLD),
                Text.literal(Utils.decimal("#.#", MP.get(level))).formatted(Formatting.GOLD));
    }
}
