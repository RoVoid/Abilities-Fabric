package robot.abilities.magic;

import robot.abilities.magic.skill.Skill;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Magic {
    private final String name;
    private final Map<String, Skill> skills = new HashMap<>();

    public Magic(String name, Skill... skills) {
        this.name = name;
        for (Skill skill : Arrays.stream(skills).toList()) {
            putSkill(skill);
        }
    }

    public String getName() {
        return name;
    }

    public void putSkill(Skill skill) {
        this.skills.put(skill.getName(), skill);
    }

    public Skill getSkill(String name) {
        return skills.getOrDefault(name, null);
    }

    public List<Skill> getSkills() {
        return skills.values().stream().toList();
    }

    public String getTranslateKey() {
        return name;
    }
}
