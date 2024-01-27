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

public class SkillHelper {

    public static AbstractSkill getSkill(String skillName) {
        return ModSkills.skills.getOrDefault(skillName, null);
    }

    public static List<AbstractSkill> getSkillsWithType(String magicName, AbstractSkill.Type type) {
        Magic magic = ModMagics.getMagic(magicName);
        List<AbstractSkill> list = new ArrayList<>();
        if (magic != null) {
            magic.getSkills().forEach((skill) -> {
                if (skill.getType() == type) list.add(skill);
            });
        }
        return list;
    }

    public static NbtCompound getData(IPlayerMixin cap, String skill) {
        if (skill == null) skill = MainSkills.get(cap).getID();
        return cap.get(DataKeys.SKILLS).getCompound(skill);
    }

    public static int getData(IPlayerMixin cap, String skill, Keys key) {
        return getData(cap, skill).getInt(key.get());
    }

    public static void upLevel(IPlayerMixin cap, AbstractSkill skill, int levelUp) {
        upLevel(cap, skill.getID(), levelUp);
    }

    public static void upLevel(IPlayerMixin cap, String skillName, int levelUp) {
        NbtCompound skillNBT = cap.get(DataKeys.SKILLS).getCompound(skillName);
        if (skillNBT == null) return;
        skillNBT.putInt(Keys.LEVEL.get(), skillNBT.getInt(Keys.LEVEL.get()) + 1);
        cap.put(DataKeys.SKILLS, skillName, skillNBT);
    }

    public static List<SkillEnchantment> getEnchantments() {
        return ModSkills.skills.values().stream()
                .map(AbstractSkill::getEnchantment)
                .filter(Objects::nonNull)
                .toList();
    }

    public static void addPoints(IPlayerMixin cap, int points) {
        cap.add(DataKeys.POINTS, points);
        int p = (int) Math.floor(Constants.getMpPointsLimit(cap));
        if (cap.get(DataKeys.POINTS) / p >= 1) {
            cap.add(DataKeys.MP_LEVEL, 1);
            cap.add(DataKeys.POINTS, -p);
            cap.put(DataKeys.MP_MAX, Constants.getMpMax(cap));
        }
    }

    public enum Keys {
        LEVEL("level"), CAST_TIME("castTime"), EXPERIENCE("experience");

        private final String key;

        Keys(String key) {
            this.key = key;
        }

        public String get() {
            return key;
        }
    }
}
