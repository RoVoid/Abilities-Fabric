package robot.abilities.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModCircleRecipes {
    private static final Map<String, CircleRecipe> recipes = new HashMap<>();

    public static final CircleRecipe GOLD = register("gold", new CircleRecipe(CircleRecipe.resultOf(new ItemStack(Items.GOLD_INGOT, 2)),
            List.of(new ItemStack(Items.IRON_INGOT, 4), new ItemStack(Items.GOLD_NUGGET, 2)), false, List.of(0), 10, 80));
    public static final CircleRecipe NETHERITE_INGOT = register("netherite_ingot", new CircleRecipe(CircleRecipe.resultOf(new ItemStack(Items.NETHERITE_INGOT, 1)),
            List.of(new ItemStack(Items.NETHERITE_SCRAP, 1), new ItemStack(Items.GOLD_INGOT, 3)), false, List.of(0), 10, 100));

    private static CircleRecipe register(String name, CircleRecipe recipe) {
        recipes.put(name, recipe);
        return recipe;
    }

    public static CircleRecipe getRecipe(String name) {
        return recipes.getOrDefault(name, null);
    }

    public static Map<String, CircleRecipe> getRecipes() {
        return recipes;
    }
}
