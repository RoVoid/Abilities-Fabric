package robot.abilities.magic.skill;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import robot.abilities.magic.skill.air.DashSkill;
import robot.abilities.magic.skill.air.PushSkill;
import robot.abilities.magic.skill.earth.GolemSummonSkill;
import robot.abilities.magic.skill.fire.FireBallSkill;
import robot.abilities.magic.skill.fire.FireResistanceSkill;
import robot.abilities.magic.skill.fire.FireRingSkill;
import robot.abilities.magic.skill.water.FertilitySkill;

import java.util.HashMap;
import java.util.Map;

public class ModSkills {

    public static final Map<String, AbstractSkill> skills = new HashMap<>();
    public static final FireBallSkill FIRE_BALL = add(registerEnchantment(new FireBallSkill()));
    public static final FireResistanceSkill FIRE_RESISTANCE = add(new FireResistanceSkill());
    public static final FireRingSkill FIRE_RING = add(new FireRingSkill());
    public static final FertilitySkill FERTILITY = add(new FertilitySkill());
    public static final GolemSummonSkill GOLEM_SUMMON = add(new GolemSummonSkill());
    public static final DashSkill DASH = add(registerEnchantment(new DashSkill()));
    public static final PushSkill PUSH = add(new PushSkill());

    public static <T extends AbstractSkill> T add(T skill) {
        skills.put(skill.getID(), skill);
        return skill;
    }

    public static <T extends AbstractSkill> T registerEnchantment(T skill) {
        skill.regEnch();
        Registry.register(Registries.ENCHANTMENT, new Identifier(skill.getEnchantment().getNamespace(), "skill." + skill.getEnchantment().getName()), skill.getEnchantment());
        return skill;
    }

    public static void register() {
    }
}
