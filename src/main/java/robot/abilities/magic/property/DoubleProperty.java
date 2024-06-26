package robot.abilities.magic.property;

public class DoubleProperty implements Property<Double> {
    public final double initial;
    public final double delta;
    public final double max;

    public DoubleProperty(double initial, double delta, double max) {
        this.initial = initial;
        this.delta = delta;
        this.max = max;
    }

    public DoubleProperty(double initial) {
        this(initial, 0, 0);
    }

    @Override
    public Double get(int level) {
        double value = (level - 1) < 0 ? 0 : this.initial + this.delta * (level - 1);
        return max > 0 ? Math.min(value, max) : value;
    }
}
