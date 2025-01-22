package robot.abilities.magic;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import robot.abilities.magic.skill.Skill;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Magic {
    private final String name, namespace;
    private final Map<String, Skill> skills = new HashMap<>();

    public Magic(String name, Skill... skills) {
        this(name.substring(0, name.indexOf(".")), name.substring(name.indexOf(".") + 1), skills);
    }

    public Magic(String namespace, String name, Skill... skills) {
        this.name = name;
        this.namespace = namespace;
        Arrays.stream(skills).toList().forEach(this::put);
    }

    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace;
    }

    public String id() {
        return "%s:%s".formatted(namespace, name);
    }

    public String getTranslateKey() {
        return "magic.%s.%s".formatted(namespace, name);
    }

    public MutableText getDisplayName() {
        return Text.translatable(getTranslateKey());
    }

    public void put(Skill skill) {
        this.skills.put(skill.getName(), skill);
    }

    public Skill get(String name) {
        return skills.getOrDefault(name, null);
    }

    public List<Skill> getAll() {
        return skills.values().stream().toList();
    }
}
