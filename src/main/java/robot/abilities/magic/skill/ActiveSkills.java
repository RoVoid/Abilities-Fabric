package robot.abilities.magic.skill;

import net.minecraft.nbt.NbtCompound;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.util.ArrayList;
import java.util.List;

public class ActiveSkills {

    public static List<String> fromNbt(NbtCompound nbt) {
        List<String> skills = new ArrayList<>();
        for (int i = 0; i < nbt.getInt("size"); i++) {
            skills.add(nbt.getString(String.valueOf(i)));
        }
        return skills;
    }

    public static NbtCompound toNbt(IPlayerMixin cap) {
        return cap.get(DataKeys.ACTIVE_SKILLS);
    }

    public static NbtCompound toNbt(List<String> skills) {
        NbtCompound nbt = new NbtCompound();
        nbt.putInt("size", skills.size());
        for (int i = 0; i < skills.size(); i++) {
            nbt.putString(String.valueOf(i), skills.get(i));
        }
        return nbt;
    }

    public static void setSkillsID(IPlayerMixin cap, List<String> skills) {
        skills.stream().filter((skillID) -> skillID != null && (skillID.isEmpty() || SkillHelper.get(skillID) != null));
        cap.put(DataKeys.ACTIVE_SKILLS, toNbt(skills));
    }

    public static void setSkillsID(IPlayerMixin cap, NbtCompound skills) {
        skills.getKeys().stream().filter((skillID) -> skillID != null && (skillID.isEmpty() || SkillHelper.get(skillID) != null));
        cap.put(DataKeys.ACTIVE_SKILLS, skills);
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
        System.out.println(skills.indexOf(skillID));
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

    public static int index(IPlayerMixin cap) {
        return cap.get(DataKeys.SKILL);
    }

    public static void swap(IPlayerMixin cap, int index1, int index2) {
        List<String> skills = getSkillsID(cap);
        if (index1 < 0 || index1 >= skills.size() || index2 < 0 || index2 >= skills.size() || index1 == index2) return;
        String skill = skills.get(index1);
        skills.set(index1, skills.get(index2));
        skills.set(index2, skill);
        setSkillsID(cap, skills);
        if (cap.get(DataKeys.SKILL) == index1) updateIndex(cap, index2);
        else if (cap.get(DataKeys.SKILL) == index2) updateIndex(cap, index1);

    }

    public static void insert(IPlayerMixin cap, String oldSkillID, String newSkillID) {
        List<String> skills = getSkillsID(cap);
        if (!skills.contains(oldSkillID)) return;
        if (skills.contains(newSkillID)) {
            skills.set(indexOf(cap, newSkillID), "");
        }
        skills.set(skills.indexOf(oldSkillID), newSkillID);
        setSkillsID(cap, skills);
        updateIndex(cap, newSkillID);
    }

    public static void set(IPlayerMixin cap, String skillID, int index, boolean force) {
        List<String> skills = getSkillsID(cap);
        if ((index < 0 && !skills.contains("")) || index >= skills.size()) return;
        if (skills.contains(skillID)) {
            skills.set(indexOf(cap, skillID), "");
        }
        index = index < 0 ? skills.indexOf("") : index;
        skills.set(index, skillID);
        setSkillsID(cap, skills);
        if (force) updateIndex(cap, skillID);
    }

    public static void clear(IPlayerMixin cap, int index) {
        List<String> skills = getSkillsID(cap);
        if (index < 0 || index >= skills.size() || skills.get(index).isEmpty()) return;
        skills.set(index, "");
        setSkillsID(cap, skills);
        updateIndex(cap, -1);
        for (int i = 0; i < skills.size(); i++) {
            if (skills.get(i).isEmpty()) continue;
            updateIndex(cap, i);
            break;
        }
    }

    public static int indexOf(IPlayerMixin cap, String skillID) {
        List<String> skills = getSkillsID(cap);
        for (int i = 0; i < skills.size(); i++) {
            if (skills.get(i).equals(skillID)) {
                return i;
            }
        }
        return -1;
    }

    public static void setSize(IPlayerMixin cap, int size) {
        List<String> skills = getSkillsID(cap);
        if (size < 0 || size >= 10 || size == skills.size()) return;
        String skill = skills.get(cap.get(DataKeys.SKILL));
        if (skills.size() < size) for (int i = skills.size(); i < size; i++) skills.add("");
        else {
            while (skills.size() > size && skills.contains("")) {
                skills.remove("");
            }
            while (skills.size() > size) {
                skills.remove(0);
            }
        }
        setSkillsID(cap, skills);
        int i = skills.indexOf(skill);
        if (i > -1) updateIndex(cap, skill);
        else updateIndex(cap, skills.get(0));
    }

    public static int getSize(IPlayerMixin cap) {
        return getSkillsID(cap).size();
    }

    public static void add(IPlayerMixin cap, int size) {
        if (size <= 0) return;
        List<String> skills = getSkillsID(cap);
        for (int i = 0; i < size; i++) skills.add("");
        setSkillsID(cap, skills);
    }
}
