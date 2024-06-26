package robot.abilities.recipe;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class CircleRecipe {
    private final List<Integer> supportedLevels;
    private final List<ItemStack> ingredients;
    private final CircleRecipeResult result;
    private final int creationTime;
    private final double manaAmount;
    private final boolean isShaped;


    public CircleRecipe(CircleRecipeResult result, List<ItemStack> ingredients, boolean isShaped, List<Integer> supportedLevels, double manaAmount, int creationTime) {
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

    public CircleRecipeResult getResult() {
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

    public static CircleRecipeResult resultOf(Entity entity) {
        return new CircleRecipeResult(entity);
    }

    public static CircleRecipeResult resultOf(ItemStack stack) {
        return new CircleRecipeResult(stack);
    }

    public static class CircleRecipeResult {
        private Entity entity;
        private ItemStack stack;

        private CircleRecipeResult(Entity entity) {
            this.entity = entity;
        }

        private CircleRecipeResult(ItemStack stack) {
            this.stack = stack;
        }

        public void result(BlockState state, BlockPos pos, World world) {
            if (world == null || world.isClient) return;
            if (entity != null) {
                entity.setPosition(pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5);
                world.spawnEntity(entity);
            }
            if (stack != null) {
                world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, stack));
            }
        }
    }
}
