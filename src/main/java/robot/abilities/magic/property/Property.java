package robot.abilities.magic.property;

public interface Property {
    static Property of(Number initial, double delta) {
        if (initial instanceof Integer) {
            return new IntProperty(initial.intValue(), delta);
        } else if(initial instanceof Double){
            return new DoubleProperty(initial.doubleValue(), delta);
        }
        return null;
    }

    static Property of(Number initial) {
        if (initial instanceof Integer) {
            return new IntProperty(initial.intValue(), 0);
        } else if(initial instanceof Double){
            return new DoubleProperty(initial.doubleValue(), 0);
        }
        return null;
    }

    double get(int level);
}
