package robot.abilities.magic.skill;

import net.minecraft.nbt.NbtCompound;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActiveSkills {

    private static final HashMap<String, List<String>> skills = new HashMap<>();

    public static void fromNbt(IPlayerMixin cap, NbtCompound nbt) {
        String playerName = cap.getPlayer().getName().getString();
        List<String> s = getSkillIDs(playerName);
        s.clear();
        for (int i = 0; i < nbt.getInt("size"); i++) {
            s.add(nbt.getString(String.valueOf(i)));
        }
        skills.put(playerName, s);
    }

    public static NbtCompound toNbt(IPlayerMixin cap) {
        String playerName = cap.getPlayer().getName().getString();
        List<String> s = getSkillIDs(playerName);
        NbtCompound nbt = new NbtCompound();
        nbt.putInt("size", s.size());
        for (int i = 0; i < s.size(); i++) {
            nbt.putString(String.valueOf(i), s.get(i));
        }
        return nbt;
    }

    public static void writeNbt(IPlayerMixin cap) {
        cap.put(DataKeys.ACTIVE_SKILLS, toNbt(cap));
    }

    public static void put(IPlayerMixin cap, String skillID, boolean force) {
        String playerName = cap.getPlayer().getName().getString();
        List<String> s = getSkillIDs(playerName);
        if (s.contains(skillID)) return;
        s.add(skillID);
        skills.put(playerName, s);
        if (force) updateIndex(cap, skillID);
        writeNbt(cap);
    }

    public static void put(IPlayerMixin cap, Skill skill, boolean force) {
        put(cap, skill.id(), force);
    }

    public static Skill get(IPlayerMixin cap) {
        return get(cap, cap.get(DataKeys.SKILL));
    }

    public static Skill get(IPlayerMixin cap, int index) {
        String playerName = cap.getPlayer().getName().getString();
        List<String> s = getSkillIDs(playerName);
        if (index < 0 || index >= s.size()) return null;
        return s.get(index).isEmpty() ? null : SkillHelper.get(s.get(index));
    }

    public static void updateIndex(IPlayerMixin cap, Skill skill) {
        updateIndex(cap, skill.id());
    }

    public static void updateIndex(IPlayerMixin cap, String skillID) {
        String playerName = cap.getPlayer().getName().getString();
        List<String> s = getSkillIDs(playerName);
        if (s.contains(skillID))
            cap.put(DataKeys.SKILL, s.indexOf(skillID));
    }

    public static void insert(IPlayerMixin cap, String oldSkillID, String newSkillID) {
        String playerName = cap.getPlayer().getName().getString();
        List<String> s = getSkillIDs(playerName);
        if (!s.contains(oldSkillID)) return;
        s.set(s.indexOf(oldSkillID), newSkillID);
        skills.put(playerName, s);
        updateIndex(cap, newSkillID);
        writeNbt(cap);
    }

    public static void set(IPlayerMixin cap, String skillID, int index, boolean force) {
        String playerName = cap.getPlayer().getName().getString();
        List<String> s = getSkillIDs(playerName);
        if (index < 0 || index >= s.size()) return;
        index = s.contains("") ? s.indexOf("") : index;
        s.set(index, skillID);
        skills.put(playerName, s);
        if (force) updateIndex(cap, skillID);
        writeNbt(cap);
    }

    public static List<String> getSkillIDs(IPlayerMixin cap) {
        return getSkillIDs(cap.getPlayer().getName().getString());
    }

    public static List<String> getSkillIDs(String name) {
        return skills.getOrDefault(name, new ArrayList<>());
    }

    public static void addSlot(IPlayerMixin cap) {
        String playerName = cap.getPlayer().getName().getString();
        List<String> s = getSkillIDs(playerName);
        s.add("");
        writeNbt(cap);
        skills.put(playerName, s);
    }
}
