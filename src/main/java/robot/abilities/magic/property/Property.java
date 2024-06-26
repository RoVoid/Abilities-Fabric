package robot.abilities.magic.property;

public interface Property<T extends Number> {
    static Property<Integer> of(int initial, double delta, int max) {
        return new IntProperty(initial, delta, max);
    }

    static Property<Double> of(double initial, double delta, double max) {
        return new DoubleProperty(initial, delta, max);
    }

    static Property<Integer> of(int initial, double delta) {
        return new IntProperty(initial, delta, 0);
    }
    static Property<Double> of(double initial, double delta) {
        return new DoubleProperty(initial, delta, 0);
    }

    static Property<Integer> of(int initial) {
        return new IntProperty(initial, 0, 0);
    }

    static Property<Double> of(double initial) {
        return new DoubleProperty(initial, 0, 0);
    }

    T get(int level);
}
