package robot.abilities.magic.skill;

import net.minecraft.entity.LivingEntity;

public abstract class AbstractSkill {
    private final String name;
    private final Property mp, price;

    public AbstractSkill(String name, Property mp, Property price) {
        this.name = name;
        this.mp = mp;
        this.price = price;
    }

    public abstract void use(LivingEntity entity, int level);

    public String getName() {
        return name;
    }

    public Property getMp() {
        return mp;
    }

    public Property getPrice() {
        return price;
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
            return this.delta == 0 ? this.initial : this.initial + this.delta * level;
        }
    }
}
