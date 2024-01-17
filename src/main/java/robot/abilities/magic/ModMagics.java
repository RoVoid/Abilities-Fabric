package robot.abilities.magic;

import robot.abilities.AbilitiesMod;
import robot.abilities.magic.skill.AbstractSkill;
import robot.abilities.magic.skill.ModSkills;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModMagics {
    public static final Magic FIRE_MAGIC = new Magic(AbilitiesMod.ID + ":fire_magic", ModSkills.FIRE_BALL, ModSkills.FIRE_RESISTANCE);
    public static final Magic WATER_MAGIC = new Magic(AbilitiesMod.ID + ":water_magic", ModSkills.FERTILITY);
    public static final Magic EARTH_MAGIC = new Magic(AbilitiesMod.ID + ":earth_magic", ModSkills.GOLEM_SUMMON);
    public static final Magic AIR_MAGIC = new Magic(AbilitiesMod.ID + ":air_magic", ModSkills.DASH);

    public static final Map<String, Magic> magics = new HashMap<>();

    static {
        add(FIRE_MAGIC);
        add(WATER_MAGIC);
        add(EARTH_MAGIC);
        add(AIR_MAGIC);
    }

    public static Magic getMagic(String name) {
        return magics.getOrDefault(name, null);
    }

    public static AbstractSkill getSkill(String magicName, String skillName) {
        Magic magic = getMagic(magicName);
        return magic == null ? null : magic.getSkill(skillName);
    }

    public static List<AbstractSkill> getSkillsWithType(String magicName, AbstractSkill.Type type) {
        Magic magic = getMagic(magicName);
        List<AbstractSkill> list = new ArrayList<>();
        if (magic != null) {
            magic.getSkills().forEach((skill) -> {
                if (skill.getType() == type) list.add(skill);
            });
        }
        return list;
    }

    private static void add(Magic magic) {
        magics.put(magic.getName(), magic);
    }
}
