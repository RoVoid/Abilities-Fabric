package robot.abilities.magic.skill;

import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSkill {
    private final String name;
    private final Map<String, Property> properties = new HashMap<>();
    private final Type type;
    private Identifier icon = null;

    public AbstractSkill(String name, Type type, Property mp, Property price) {
        this.name = "skill." + name;
        this.type = type;
        add("mp", mp);
        add("price", price);
    }

    public abstract void use(PlayerEntity player, int level);

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public MutableText getDisplayName() {
        return Text.translatable(name);
    }

    public Property get(String key) {
        return this.properties.get(key);
    }

    public double get(String key, int level) {
        Property property = get(key);
        return property == null ? -1 : property.get(level);
    }

    public void add(String key, Property property) {
        if (!has(key)) this.properties.put(key, property);
    }

    public boolean has(String key) {
        return this.properties.containsKey(key);
    }

    public void setIcon(Identifier icon) {
        this.icon = icon;
    }

    public Identifier getIcon() {
        return icon;
    }

    public MutableText getTooltipText(int level) {
        return Text.translatable(getName() + ".tooltip");
    }

    public Tooltip getTooltip(int level) {
        return Tooltip.of(getDisplayName().append("\n").append(getTooltipText(level)));
    }

    public boolean canUse(PlayerEntity player, int level) {
        return level > 0;
    }

    public boolean canUse(PlayerEntity player) {
        return canUse(player, ((IPlayerMixin) player).get(DataKeys.SKILLS).getInt(getName()));
    }

    public enum Type {
        ATTACK, DEFEND, SUPPORT
    }

    public static class Property {
        double initial, delta;

        public Property(double initial, double delta) {
            this.initial = initial;
            this.delta = delta;
        }

        public Property(double initial) {
            this(initial, 0);
        }

        public double get(int level) {
            return (level - 1) < 0 ? 0 : this.delta == 0 ? this.initial : this.initial + this.delta * (level - 1);
        }
    }
}
