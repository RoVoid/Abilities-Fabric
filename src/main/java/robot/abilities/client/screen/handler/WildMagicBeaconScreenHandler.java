package robot.abilities.client.screen.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;
import robot.abilities.client.screen.ModScreens;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

public class WildMagicBeaconScreenHandler extends ScreenHandler {

    public WildMagicBeaconScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    public WildMagicBeaconScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ModScreens.BEACON, syncId);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return !((IPlayerMixin) player).get(DataKeys.MAGIC).isEmpty();
    }


}
