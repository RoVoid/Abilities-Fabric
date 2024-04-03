package robot.abilities.item;

import net.minecraft.item.Item;

public class ManaInfusedItem extends Item {

    double manaAmount;

    public ManaInfusedItem(Settings settings) {
        this(settings, 0);
    }

    public ManaInfusedItem(Settings settings, double manaAmount) {
        super(settings);
        this.manaAmount = manaAmount;
    }

    public double getManaAmount() {
        return manaAmount;
    }

    public void setManaAmount(double manaAmount) {
        this.manaAmount = manaAmount;
    }
}
