package robot.abilities.magic;

import robot.abilities.AbilitiesMod;
import robot.abilities.magic.skill.AbstractSkill;
import robot.abilities.magic.skill.ModSkills;

import java.util.HashMap;
import java.util.Map;

public class ModMagics {
    public static final Magic FIRE_MAGIC = new Magic(AbilitiesMod.ID + ":fire_magic", ModSkills.FIRE_BALL);
    public static final Magic WATER_MAGIC = new Magic(AbilitiesMod.ID + ":water_magic", ModSkills.FERTILITY);

    public static final Map<String, Magic> magics = new HashMap<>();

    static {
        add(FIRE_MAGIC);
        add(WATER_MAGIC);
    }

    public static Magic getMagic(String name) {
        return magics.getOrDefault(name, null);
    }

    public static AbstractSkill getSkill(String magicName, String skillName) {
        Magic magic = getMagic(magicName);
        return magic == null ? null : magic.getSkill(skillName);
    }

    private static void add(Magic magic) {
        magics.put(magic.getName(), magic);
    }
}
