package robot.abilities.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Unique;

public interface IPlayerMixin {
    NbtCompound getPersistentData();

    void setPersistentData(NbtCompound nbt);

    <T> void put(DataKeys.Key<T> key, T value);

    <N extends NbtCompound, T> void put(DataKeys.Key<N> key, String key2, T value);

    <T extends Number> IPlayerMixin add(DataKeys.Key<T> key, T value);

    <N extends NbtCompound, T> IPlayerMixin add(DataKeys.Key<N> key, String key2, T value);

    <T> T get(DataKeys.Key<T> key);

    boolean isNull();

    void sync();

    @Unique
    void sync(DataKeys.Key<?>... keys);

    PlayerEntity getPlayer();
}
