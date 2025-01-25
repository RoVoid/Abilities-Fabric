package robot.abilities.magic.skill;

import net.minecraft.nbt.NbtCompound;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.util.ArrayList;
import java.util.List;

public class ActiveSkills {

    public static List<String> fromNbt(NbtCompound nbt) {
        List<String> skills = new ArrayList<>();
        for (int i = 0; i < nbt.getInt("size"); i++) skills.add(nbt.getString(String.valueOf(i)));
        return skills;
    }

    public static NbtCompound toNbt(List<String> skills) {
        NbtCompound nbt = new NbtCompound();
        nbt.putInt("size", skills.size());
        for (int i = 0; i < skills.size(); i++) nbt.putString(String.valueOf(i), skills.get(i));
        return nbt;
    }

    public static void setSkillsID(IPlayerMixin cap, List<String> skills) {
        if (skills == null) return;
        List<String> filteredSkills = skills.stream()
                .filter(skillID -> skillID != null && (skillID.isEmpty() || SkillHelper.get(skillID) != null))
                .toList();
        cap.put(DataKeys.ACTIVE_SKILLS, toNbt(filteredSkills));
    }

    public static void setSkillsID(IPlayerMixin cap, NbtCompound skills) {
        NbtCompound filteredSkills = skills.getKeys().stream()
                .filter(skillID -> skillID != null && !skillID.isEmpty() && SkillHelper.get(skillID) != null)
                .collect(NbtCompound::new, (nbt, key) -> nbt.put(key, skills.get(key)), NbtCompound::copyFrom);
        cap.put(DataKeys.ACTIVE_SKILLS, filteredSkills);
    }

    public static List<String> getSkillsID(IPlayerMixin cap) {
        return fromNbt(cap.get(DataKeys.ACTIVE_SKILLS));
    }

    public static void put(IPlayerMixin cap, String skillID, boolean force) {
        set(cap, skillID, -1, force);
    }

    public static void put(IPlayerMixin cap, Skill skill, boolean force) {
        put(cap, skill.id(), force);
    }

    public static Skill get(IPlayerMixin cap) {
        return get(cap, cap.get(DataKeys.SKILL));
    }

    public static Skill get(IPlayerMixin cap, int index) {
        List<String> skills = getSkillsID(cap);
        if (index < 0 || index >= skills.size()) return null;
        return skills.get(index).isEmpty() ? null : SkillHelper.get(skills.get(index));
    }

    public static void updateIndex(IPlayerMixin cap, Skill skill) {
        updateIndex(cap, skill == null ? null : skill.id());
    }

    public static void updateIndex(IPlayerMixin cap, String skillID) {
        List<String> skills = getSkillsID(cap);
        if (skillID != null && skills.contains(skillID))
            cap.put(DataKeys.SKILL, skills.indexOf(skillID));
        else if (cap.get(DataKeys.SKILL) >= 0 && skills.get(cap.get(DataKeys.SKILL)).isEmpty())
            cap.put(DataKeys.SKILL, -1);
    }

    public static void updateIndex(IPlayerMixin cap, int index) {
        List<String> skills = getSkillsID(cap);
        if (index < -1 || index >= skills.size()) return;
        cap.put(DataKeys.SKILL, index);
    }

    public static int getIndex(IPlayerMixin cap) {
        return cap.get(DataKeys.SKILL);
    }

    public static void replace(IPlayerMixin cap, String oldSkillID, String newSkillID) {
        if (oldSkillID.equals(newSkillID)) return;
        List<String> skills = getSkillsID(cap);
        int index = skills.indexOf(oldSkillID);
        if (index < 0) return;
        if (skills.remove(newSkillID) && skills.indexOf(oldSkillID) < index) {
            index--;
            skills.add("");
        }
        skills.set(index, newSkillID);
        setSkillsID(cap, skills);
        updateIndex(cap, index);
    }


    public static void set(IPlayerMixin cap, String skillID, int index, boolean force) {
        List<String> skills = getSkillsID(cap);
        if (index < 0 || index >= skills.size()) index = skills.indexOf("");
        skills.remove(skillID);
        if (index >= 0 && index < skills.size()) skills.add(index, skillID);
        else {
            skills.add(skillID);
            index = skills.size() - 1;
        }
        setSkillsID(cap, skills);
        if (force) updateIndex(cap, index);
    }


    public static void clear(IPlayerMixin cap, int index) {
        List<String> skills = getSkillsID(cap);
        if (index < 0 || index >= skills.size() || skills.get(index).isEmpty()) return;
        skills.remove(index);
        skills.add("");
        setSkillsID(cap, skills);
        updateIndex(cap, -1);
    }


    public static int indexOf(IPlayerMixin cap, String skillID) {
        return getSkillsID(cap).indexOf(skillID);
    }

    public static void setSize(IPlayerMixin cap, int size) {
        List<String> skills = getSkillsID(cap);
        if (size < 0 || size >= 10 || size == skills.size()) return;
        if (skills.size() < size) for (int i = skills.size(); i < size; i++) skills.add("");
        else {
            if (cap.get(DataKeys.SKILL) > size) updateIndex(cap, size - 1);
            for (int i = skills.size(); i > size; i--) skills.remove(i - 1);
        }
        setSkillsID(cap, skills);
    }

    public static int getSize(IPlayerMixin cap) {
        return getSkillsID(cap).size();
    }

    public static void add(IPlayerMixin cap, int size) {
        setSize(cap, getSize(cap) + size);
    }
}
