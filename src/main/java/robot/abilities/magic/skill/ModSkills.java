package robot.abilities.magic.skill;

import robot.abilities.magic.Magic;
import robot.abilities.magic.skill.air.DashSkill;
import robot.abilities.magic.skill.air.EvasionSkill;
import robot.abilities.magic.skill.air.PushSkill;
import robot.abilities.magic.skill.earth.GolemSummonSkill;
import robot.abilities.magic.skill.earth.StrongFistSkill;
import robot.abilities.magic.skill.fire.FireBallSkill;
import robot.abilities.magic.skill.fire.FireResistanceSkill;
import robot.abilities.magic.skill.fire.FireRingSkill;
import robot.abilities.magic.skill.fire.PhoenixSkill;
import robot.abilities.magic.skill.water.FertilitySkill;
import robot.abilities.magic.skill.water.FreezeSkill;
import robot.abilities.magic.skill.water.WaterBallSkill;

import java.util.HashMap;
import java.util.Map;

import static robot.abilities.magic.ModMagics.*;

public class ModSkills {

    public static final Map<String, Skill> skills = new HashMap<>();
    public static final FireBallSkill FIREBALL = registerSkill(new FireBallSkill(), FIRE_MAGIC);
    public static final FireResistanceSkill FIRE_RESISTANCE = registerSkill(new FireResistanceSkill(), FIRE_MAGIC);
    public static final FireRingSkill FIRE_RING = registerSkill(new FireRingSkill(), FIRE_MAGIC);
    public static final PhoenixSkill PHOENIX = registerSkill(new PhoenixSkill(), FIRE_MAGIC);
    public static final FertilitySkill FERTILITY = registerSkill(new FertilitySkill(), WATER_MAGIC);
    public static final WaterBallSkill WATER_BALL = registerSkill(new WaterBallSkill(), WATER_MAGIC);
    public static final FreezeSkill FREEZE_SKILL = registerSkill(new FreezeSkill(), WATER_MAGIC);
    public static final GolemSummonSkill GOLEM_SUMMON = registerSkill(new GolemSummonSkill(), EARTH_MAGIC);
    public static final StrongFistSkill STRONG_FIST = registerSkill(new StrongFistSkill(), EARTH_MAGIC);
    public static final DashSkill DASH = registerSkill(new DashSkill(), AIR_MAGIC);
    public static final PushSkill PUSH = registerSkill(new PushSkill(), AIR_MAGIC);
    public static final EvasionSkill EVASION = registerSkill(new EvasionSkill(), AIR_MAGIC);

    public static <T extends Skill> T registerSkill(T skill, Magic magic) {
        if (skill == null) {
            throw new IllegalArgumentException("Skill cannot be null");
        }

        if (magic == null) {
            throw new IllegalArgumentException("Magic cannot be null");
        }

        String skillId = skill.id();
        if (skills.containsKey(skillId)) {
            throw new IllegalStateException("Skill with ID '" + skillId + "' is already registered.");
        }

        skill.applyEventsHandler();

        skills.put(skillId, skill);
        magic.put(skill);
        return skill;
    }


    public static void init() {
    }
}
