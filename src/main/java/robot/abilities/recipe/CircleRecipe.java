package robot.abilities.recipe;

import net.minecraft.item.ItemStack;

import java.util.List;

public class CircleRecipe {
    private final List<Integer> supportedLevels;
    private final List<ItemStack> ingredients;
    private final ItemStack result;
    private final int creationTime;
    private final double manaAmount;
    private final boolean isShaped;


    public CircleRecipe(ItemStack result, List<ItemStack> ingredients, boolean isShaped, List<Integer> supportedLevels, double manaAmount, int creationTime) {
        this.result = result;
        this.ingredients = ingredients;
        this.isShaped = isShaped;
        this.supportedLevels = supportedLevels;
        this.manaAmount = manaAmount;
        this.creationTime = creationTime;

    }

    public double getManaAmount() {
        return manaAmount;
    }

    public int getCreationTime() {
        return creationTime;
    }

    public ItemStack getResult() {
        return result;
    }

    public List<ItemStack> getIngredients() {
        return ingredients;
    }

    public List<Integer> getSupportedLevels() {
        return supportedLevels;
    }

    public boolean isShaped() {
        return isShaped;
    }
}
