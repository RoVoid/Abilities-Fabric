package robot.abilities.magic.property;

public class DoubleProperty extends Property<Double> {

    public DoubleProperty(Double initial) {
        super(initial);
    }

    public DoubleProperty(Double initial, double delta) {
        super(initial, delta);
    }

    public DoubleProperty(Double initial, double delta, Double max) {
        super(initial, delta, max);
    }

    @Override
    public Double get(int level) {
        double value = (level - 1) < 0 ? 0 : initial + delta * (level - 1);
        return max > 0 ? Math.min(value, max) : value;
    }
}
