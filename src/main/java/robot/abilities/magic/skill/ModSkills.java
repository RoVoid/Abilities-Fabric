package robot.abilities.magic.skill;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import robot.abilities.magic.Magic;
import robot.abilities.magic.ModMagics;
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

import static robot.abilities.magic.ModMagics.*;

public class ModSkills {

    public static final Map<String, Skill> skills = new HashMap<>();
    public static final FireBallSkill FIREBALL = registerSkill(new FireBallSkill(), FIRE_MAGIC);
    public static final FireResistanceSkill FIRE_RESISTANCE = registerSkill(new FireResistanceSkill(), FIRE_MAGIC);
    public static final FireRingSkill FIRE_RING = registerSkill(new FireRingSkill(), FIRE_MAGIC);
    public static final FertilitySkill FERTILITY = registerSkill(new FertilitySkill(), WATER_MAGIC);
    public static final WaterBallSkill WATER_BALL = registerSkill(new WaterBallSkill(), WATER_MAGIC);
    public static final GolemSummonSkill GOLEM_SUMMON = registerSkill(new GolemSummonSkill(), EARTH_MAGIC);
    public static final StrongFistSkill STRONG_FIST = registerSkill(new StrongFistSkill(), EARTH_MAGIC);
    public static final DashSkill DASH = registerSkill(new DashSkill(), AIR_MAGIC);
    public static final PushSkill PUSH = registerSkill(new PushSkill(), AIR_MAGIC);

    public static <T extends Skill> T registerSkill(T skill, Magic magic) {
        skills.put(skill.getID(), skill);
        if (skill.isEnchantment()) {
            Registry.register(Registries.ENCHANTMENT, new Identifier(skill.getEnchantment().getNamespace(), "skill." + skill.getEnchantment().getName()), skill.getEnchantment());
        }
        if(magic != null) magic.putSkill(skill);
        return skill;
    }

    public static <T extends Skill> T registerSkill(T skill) {
        return registerSkill(skill, null);
    }

    public static void register() {
    }
}
