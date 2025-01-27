package robot.abilities.magic.property;

public abstract class Property<T extends Number> {
    T initial, max;
    double delta;

    public Property(T initial, double delta, T max) {
        this.initial = initial;
        this.delta = delta;
        this.max = max;
    }

    public Property(T initial, double delta) {
        this(initial, delta, (T) (Double.valueOf(0)));
    }

    public Property(T initial) {
        this(initial, 0, (T) (Double.valueOf(0)));
    }

    public abstract T get(int level);
}
