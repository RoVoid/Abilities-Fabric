package robot.abilities.client.screen.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.PropertyDelegate;

import static robot.abilities.client.screen.ModScreens.SOUL_FURNACE;

public class SoulFurnaceScreenHandler extends AbstractFurnaceScreenHandler {

    public SoulFurnaceScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(SOUL_FURNACE, RecipeType.SMELTING, RecipeBookCategory.FURNACE, syncId, playerInventory);
    }

    public SoulFurnaceScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(SOUL_FURNACE, RecipeType.SMELTING, RecipeBookCategory.FURNACE, syncId, playerInventory, inventory, propertyDelegate);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return super.canUse(player);
    }
}
