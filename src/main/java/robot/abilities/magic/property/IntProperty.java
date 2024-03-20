package robot.abilities.magic.property;

import robot.abilities.util.Utils;

public class IntProperty implements Property {
    public final int initial;
    public final double delta;

    public IntProperty(int initial, double delta) {
        this.initial = initial;
        this.delta = delta;
    }

    public IntProperty(int initial) {
        this(initial, 0);
    }

    @Override
    public double get(int level) {
        return (level - 1) < 0 ? 0 : this.delta == 0 ? this.initial : Double.parseDouble(Utils.decimal("#", this.initial + this.delta * (level - 1)));
    }
}
