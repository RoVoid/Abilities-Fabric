package robot.abilities.util;

import net.minecraft.nbt.NbtCompound;

public class WalkSpeedData {
    public static float setWalkSpeed(IEntityDataSaver player, float speed){
        NbtCompound nbt = player.getPersistentData();
        nbt.putFloat("walkSpeed", speed);
        return speed;
    }
}
