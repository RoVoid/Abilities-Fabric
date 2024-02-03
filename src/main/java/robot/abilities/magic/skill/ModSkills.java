package robot.abilities.magic.skill;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import robot.abilities.magic.skill.air.DashSkill;
import robot.abilities.magic.skill.air.PushSkill;
import robot.abilities.magic.skill.earth.GolemSummonSkill;
import robot.abilities.magic.skill.earth.StrongFistSkill;
import robot.abilities.magic.skill.fire.FireBallSkill;
import robot.abilities.magic.skill.fire.FireResistanceSkill;
import robot.abilities.magic.skill.fire.FireRingSkill;
import robot.abilities.magic.skill.water.FertilitySkill;
import robot.abilities.magic.skill.water.WaterBallSkill;

import java.util.HashMap;
import java.util.Map;

public class ModSkills {

    public static final Map<String, AbstractSkill> skills = new HashMap<>();
    public static final FireBallSkill FIREBALL = registerSkill(new FireBallSkill());
    public static final FireResistanceSkill FIRE_RESISTANCE = registerSkill(new FireResistanceSkill());
    public static final FireRingSkill FIRE_RING = registerSkill(new FireRingSkill());
    public static final FertilitySkill FERTILITY = registerSkill(new FertilitySkill());
    public static final WaterBallSkill WATER_BALL = registerSkill(new WaterBallSkill());
    public static final GolemSummonSkill GOLEM_SUMMON = registerSkill(new GolemSummonSkill());
    public static final StrongFistSkill STRONG_FIST = registerSkill(new StrongFistSkill());
    public static final DashSkill DASH = registerSkill(new DashSkill());
    public static final PushSkill PUSH = registerSkill(new PushSkill());

    public static <T extends AbstractSkill> T registerSkill(T skill) {
        skills.put(skill.getID(), skill);
        if (skill.isEnchantment()) {
            Registry.register(Registries.ENCHANTMENT, new Identifier(skill.getEnchantment().getNamespace(), "skill." + skill.getEnchantment().getName()), skill.getEnchantment());
        }
        return skill;
    }

    public static void register() {
    }
}
