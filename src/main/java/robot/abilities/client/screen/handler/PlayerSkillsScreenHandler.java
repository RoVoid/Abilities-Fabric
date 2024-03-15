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

public class PlayerSkillsScreenHandler extends ScreenHandler {

    private boolean upgradeable;
    public PlayerSkillsScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    public PlayerSkillsScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ModScreens.PLAYER_SKILLS, syncId);
    }



    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return !((IPlayerMixin) player).get(DataKeys.MAGIC).isEmpty();
    }

    public PlayerSkillsScreenHandler upgradeable(boolean upgradeable){
        this.upgradeable = upgradeable;
        return this;
    }

    public PlayerSkillsScreenHandler upgradeable(){
        return upgradeable(true);
    }

    public boolean isUpgradeable() {
        return upgradeable;
    }
}
