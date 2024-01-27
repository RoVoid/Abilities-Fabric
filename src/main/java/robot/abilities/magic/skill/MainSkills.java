package robot.abilities.magic.skill;

import net.minecraft.nbt.NbtCompound;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainSkills {

    private static final HashMap<String, List<String>> skills = new HashMap<>();

    public static void fromNbt(IPlayerMixin cap, NbtCompound nbt) {
        String playerName = cap.getPlayer().getName().getString();
        List<String> s = getSkillNames(playerName);
        s.clear();
        for (int i = 0; i < nbt.getInt("size"); i++) {
            s.add(nbt.getString(String.valueOf(i)));
        }
        skills.put(playerName, s);
    }

    public static NbtCompound toNbt(IPlayerMixin cap) {
        String playerName = cap.getPlayer().getName().getString();
        List<String> s = getSkillNames(playerName);
        NbtCompound nbt = new NbtCompound();
        nbt.putInt("size", s.size());
        for (int i = 0; i < s.size(); i++) {
            nbt.putString(String.valueOf(i), s.get(i));
        }
        return nbt;
    }

    public static void writeNbt(IPlayerMixin cap) {
        cap.put(DataKeys.MAIN_SKILLS, toNbt(cap));
    }

    public static void put(IPlayerMixin cap, String skillName, boolean force) {
        String playerName = cap.getPlayer().getName().getString();
        List<String> s = getSkillNames(playerName);
        if (s.contains(skillName)) return;
        s.add(skillName);
        skills.put(playerName, s);
        if (force) updateIndex(cap, skillName);
        writeNbt(cap);
    }

    public static void put(IPlayerMixin cap, AbstractSkill skill, boolean force) {
        put(cap, skill.getID(), force);
    }

    public static AbstractSkill get(IPlayerMixin cap) {
        return get(cap, cap.get(DataKeys.SKILL));
    }

    public static AbstractSkill get(IPlayerMixin cap, int index) {
        String playerName = cap.getPlayer().getName().getString();
        List<String> s = getSkillNames(playerName);
        if (index < 0 || index >= s.size()) return null;
        return s.get(index).isEmpty() ? null : SkillHelper.getSkill(s.get(index));
    }

    public static void updateIndex(IPlayerMixin cap, AbstractSkill skill) {
        updateIndex(cap, skill.getID());
    }

    public static void updateIndex(IPlayerMixin cap, String skillName) {
        String playerName = cap.getPlayer().getName().getString();
        List<String> s = getSkillNames(playerName);
        if (s.contains(skillName))
            cap.put(DataKeys.SKILL, s.indexOf(skillName));
    }

    public static void insert(IPlayerMixin cap, String oldSkillName, String newSkillName) {
        String playerName = cap.getPlayer().getName().getString();
        List<String> s = getSkillNames(playerName);
        if (!s.contains(oldSkillName)) return;
        s.set(s.indexOf(oldSkillName), newSkillName);
        skills.put(playerName, s);
        updateIndex(cap, newSkillName);
        writeNbt(cap);
    }

    public static void set(IPlayerMixin cap, String skillName, int index, boolean force) {
        String playerName = cap.getPlayer().getName().getString();
        List<String> s = getSkillNames(playerName);
        if (index < 0 || index >= s.size()) return;
        index = s.contains("") ? s.indexOf("") : index;
        s.set(index, skillName);
        skills.put(playerName, s);
        if (force) updateIndex(cap, skillName);
        writeNbt(cap);
    }

    public static List<String> getSkillNames(IPlayerMixin cap) {
        return getSkillNames(cap.getPlayer().getName().getString());
    }

    public static List<String> getSkillNames(String name) {
        return skills.getOrDefault(name, new ArrayList<>());
    }

    public static void addSlot(IPlayerMixin cap) {
        String playerName = cap.getPlayer().getName().getString();
        List<String> s = getSkillNames(playerName);
        s.add("");
        writeNbt(cap);
        skills.put(playerName, s);
    }
}
