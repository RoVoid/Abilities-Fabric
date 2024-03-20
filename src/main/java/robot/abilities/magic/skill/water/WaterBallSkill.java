package robot.abilities.magic.skill.water;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import robot.abilities.AbilitiesMod;
import robot.abilities.entity.WaterBallEntity;
import robot.abilities.magic.property.Property;
import robot.abilities.magic.skill.Skill;
import robot.abilities.magic.skill.SkillEnchantment;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.text.DecimalFormat;

public class WaterBallSkill extends Skill {
    public WaterBallSkill() {
        super(AbilitiesMod.ID + ".water_ball", Type.ATTACK, Rarity.COMMON, Property.of(1.0, 0.02), Property.of(2, 2));
        add("damage", Property.of(1, 0.05));
        enchantment(SkillEnchantment.builder(getNamespace(), getName()).levels(1, 100).onUsed(this::useItem).build());
    }

    public boolean useItem(LivingEntity user, int level) {
        return use(user, level / 10 + 1);
    }

    public boolean use(LivingEntity user, int level) {
        if (user.getWorld().isClient) return false;
        Vec3d look = user.getRotationVec(1.0f);
        float speed = 1.5f;
        WaterBallEntity waterBall = new WaterBallEntity(user.getWorld(), user, look.x * speed, look.y * speed, look.z * speed);
        waterBall.setDamage(get("damage", level));
        waterBall.setPos(user.getX() + look.x * 1.2, user.getY() + look.y + user.getEyeHeight(user.getPose()), user.getZ() + look.z * 1.2);
        user.getWorld().spawnEntity(waterBall);
        return true;
    }

    @Override
    public void usePlayer(PlayerEntity player, int level) {
        if (!canPlayerUse(player, level) || player.getWorld().isClient) return;
        if (!use(player, level)) return;
        double mp = get("mp", level);
        IPlayerMixin cap = (IPlayerMixin) player;
        cap.add(DataKeys.POINTS, 5);
        SkillHelper.addExperience(cap, this, 5);
        cap.add(DataKeys.MANA, -mp);
        cap.sync(false);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player, int level) {
        return super.canPlayerUse(player, level) && ((IPlayerMixin) player).get(DataKeys.MANA) >= get("mp", level);
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip",
                Text.literal(new DecimalFormat("#.#").format(get("damage", level))).formatted(Formatting.GOLD),
                Text.literal(new DecimalFormat("#.#").format(get("mp", level))).formatted(Formatting.GOLD));
    }
}
