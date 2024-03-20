package robot.abilities.magic.property;

public class DoubleProperty implements Property {
    public final double initial, delta;

    public DoubleProperty(double initial, double delta) {
        this.initial = initial;
        this.delta = delta;
    }

    public DoubleProperty(double initial) {
        this(initial, 0);
    }
    @Override
    public double get(int level) {
        return (level - 1) < 0 ? 0 : this.delta == 0 ? this.initial : this.initial + this.delta * (level - 1);
    }
}
