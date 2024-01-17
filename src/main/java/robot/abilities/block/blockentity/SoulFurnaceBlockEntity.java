package robot.abilities.block.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import robot.abilities.block.ModBlockEntities;
import robot.abilities.client.screen.handler.SoulFurnaceScreenHandler;

public class SoulFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    public SoulFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SOUL_FURNACE_BLOCK_ENTITY, pos, state, RecipeType.SMELTING);
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.abilities.soul_furnace");
    }

    @Override
    protected int getFuelTime(ItemStack fuel) {
        return super.getFuelTime(fuel) / 3;
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new SoulFurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    public int getSpeedModifier() {
        return 3;
    }
}