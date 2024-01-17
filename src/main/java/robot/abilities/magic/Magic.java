package robot.abilities.magic;

import robot.abilities.magic.skill.AbstractSkill;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Magic {
    private final String name;
    private final Map<String, AbstractSkill> skills = new HashMap<>();

    public Magic(String name, AbstractSkill... skills) {
        this.name = name;
        for (AbstractSkill skill : Arrays.stream(skills).toList()) {
            this.skills.put(skill.getName(), skill);
        }
    }

    public String getName() {
        return name;
    }

    public AbstractSkill getSkill(String name) {
        return skills.getOrDefault(name, null);
    }

    public List<AbstractSkill> getSkills() {
        return skills.values().stream().toList();
    }
}
