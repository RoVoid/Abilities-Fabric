package robot.abilities.magic.property;

public class IntProperty implements Property<Integer> {
    public final int initial;
    public final double delta;
    public final int max;

    public IntProperty(int initial, double delta, int max) {
        this.initial = initial;
        this.delta = delta;
        this.max = max;
    }

    public IntProperty(int initial) {
        this(initial, 0, 0);
    }

    @Override
    public Integer get(int level) {
        int value = (level - 1) < 0 ? 0 : this.delta == 0 ? this.initial : (int) Math.round(this.initial + this.delta * (level - 1));
        return max > 0 ? Math.min(value, max) : value;
    }
}
