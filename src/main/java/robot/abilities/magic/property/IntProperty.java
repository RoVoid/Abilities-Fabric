package robot.abilities.magic.property;

public class IntProperty extends Property<Integer> {

    public IntProperty(Integer initial) {
        super(initial);
    }

    public IntProperty(Integer initial, double delta) {
        super(initial, delta);
    }

    public IntProperty(Integer initial, double delta, Integer max) {
        super(initial, delta, max);
    }

    @Override
    public Integer get(int level) {
        int value = (level - 1) < 0 ? 0 : delta == 0 ? initial : (int) Math.round(initial + delta * (level - 1));
        return max > 0 ? Math.min(value, max) : value;
    }
}
