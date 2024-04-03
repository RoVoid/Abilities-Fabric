package robot.abilities.magic.skill;

import net.minecraft.nbt.NbtCompound;
import robot.abilities.magic.Magic;
import robot.abilities.magic.ModMagics;
import robot.abilities.util.Constants;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static robot.abilities.magic.skill.SkillHelper.Keys.EXPERIENCE;
import static robot.abilities.magic.skill.SkillHelper.Keys.LEVEL;

public class SkillHelper {

    public static Skill get(String skillName) {
        return ModSkills.skills.getOrDefault(skillName, null);
    }

    public static boolean contain(String skillName) {
        return ModSkills.skills.containsKey(skillName);
    }

    public static List<Skill> getSkillsWithType(String magicName, Skill.Type type) {
        Magic magic = ModMagics.get(magicName);
        List<Skill> list = new ArrayList<>();
        if (magic != null) {
            magic.getAll().stream()
                    .filter(skill -> skill.getType() == type)
                    .forEach(list::add);
        }
        return list;
    }

    public static List<SkillEnchantment> getEnchantments() {
        return ModSkills.skills.values().stream()
                .map(Skill::getEnchantment)
                .filter(Objects::nonNull)
                .toList();
    }

    public static NbtCompound getData(IPlayerMixin cap, String skill) {
        if (skill == null) skill = ActiveSkills.get(cap).id();
        return cap.get(DataKeys.SKILLS).getCompound(skill);
    }

    public static int getData(IPlayerMixin cap, String skill, Keys key) {
        return getData(cap, skill).getInt(key.key());
    }

    public static void upLevel(IPlayerMixin cap, Skill skill, int levelUp) {
        upLevel(cap, skill.id(), levelUp);
    }

    public static void upLevel(IPlayerMixin cap, String skillID, int levelUp) {
        NbtCompound skillNBT = cap.get(DataKeys.SKILLS).getCompound(skillID);
        if (skillNBT != null) {
            skillNBT.putInt(LEVEL.key(), skillNBT.getInt(LEVEL.key()) + 1);
            cap.put(DataKeys.SKILLS, skillID, skillNBT);
        }
    }

    public static void addExperience(IPlayerMixin cap, Skill skill, int experience) {
        addExperience(cap, skill.id(), experience);
    }

    public static void addExperience(IPlayerMixin cap, String skillID, int experience) {
        cap.add(DataKeys.EXPERIENCE, experience);
        int p = Constants.getExperienceLimit(cap);
        NbtCompound skillNBT = cap.get(DataKeys.SKILLS).getCompound(skillID);
        if (cap.get(DataKeys.EXPERIENCE) < p) return;
        cap.add(DataKeys.LEVEL, 1);
        if (skillNBT != null) {
            skillNBT.putInt(EXPERIENCE.key(), skillNBT.getInt(EXPERIENCE.key()) + experience);
            cap.put(DataKeys.SKILLS, skillID, skillNBT);
        }
        cap.put(DataKeys.MAX_MANA, Constants.getManaLimit(cap));
    }

    public enum Keys {
        LEVEL("level"), CAST_TIME("castTime"), EXPERIENCE("experience");

        private final String key;

        Keys(String key) {
            this.key = key;
        }

        public String key() {
            return key;
        }
    }
}
