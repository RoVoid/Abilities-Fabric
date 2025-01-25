package robot.abilities.magic;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import robot.abilities.magic.skill.Skill;

import java.util.*;

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
        this.skills.put(skill.id(), skill);
    }

    public Skill get(String skillID) {
        return skills.getOrDefault(skillID, null);
    }

    public boolean contain(String skillID) {
        return skills.containsKey(skillID);
    }

    public List<Skill> getAll() {
        return getAll(false);
    }

    public List<Skill> getAll(boolean withSorting) {
        if (withSorting) {
            List<Skill> list = new ArrayList<>(skills.values());
            list.sort((s, s1) -> Skill.Rarity.compare(s.getRarity(), s1.getRarity()));
            return list;
        }
        return new ArrayList<>(skills.values());
    }
}
