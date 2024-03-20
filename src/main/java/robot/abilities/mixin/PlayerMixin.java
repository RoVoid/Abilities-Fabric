package robot.abilities.mixin;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import robot.abilities.network.ModMessages;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin implements IPlayerMixin {
    @Unique
    private static final NbtCompound DEFAULT = new NbtCompound();

    static {
        DataKeys.put(DEFAULT, DataKeys.BORN, false);
        DataKeys.put(DEFAULT, DataKeys.MAGIC, "");
        DataKeys.put(DEFAULT, DataKeys.MANA, 0d);
        DataKeys.put(DEFAULT, DataKeys.MAX_MANA, 10d);
        DataKeys.put(DEFAULT, DataKeys.LEVEL, 1);
        DataKeys.put(DEFAULT, DataKeys.SKILLS, new NbtCompound());
        DataKeys.put(DEFAULT, DataKeys.ACTIVE_SKILLS, new NbtCompound());
        DataKeys.put(DEFAULT, DataKeys.SKILL, -1);
        DataKeys.put(DEFAULT, DataKeys.POINTS, 0);
        DataKeys.put(DEFAULT, DataKeys.EXPERIENCE, 0);
        DataKeys.put(DEFAULT, DataKeys.COOLDOWN, 0);
    }

    @Unique
    private final List<DataKeys.Key> ansyncKeys = new ArrayList<>();
    @Shadow
    @Final
    private GameProfile gameProfile;
    @Unique
    private NbtCompound persistentData;

    @Override
    public NbtCompound getPersistentData() {
        if (isNull()) {
            persistentData = DEFAULT.copy();
            DataKeys.put(persistentData, DataKeys.UUID_KEY, gameProfile.getId());
        }
        return persistentData;
    }

    @Override
    public void setPersistentData(NbtCompound nbt) {
        if (nbt == null || nbt.isEmpty() || getPlayer().isDead()) return;
        persistentData = nbt;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfo info) {
        if (!Objects.equals(getPersistentData(), DEFAULT)) {
            nbt.put("abilities.data", getPersistentData());
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("abilities.data")) {
            NbtCompound abilitiesData = nbt.getCompound("abilities.data");
            if (gameProfile.getId().equals(DataKeys.get(abilitiesData, DataKeys.UUID_KEY))) {
                setPersistentData(abilitiesData);
            }
        }
    }

    @Override
    public <T> void put(DataKeys.Key<T> key, T value) {
        DataKeys.put(getPersistentData(), key, value);
        ansyncKeys.add(key);
    }

    @Override
    public <N extends NbtCompound, T> void put(DataKeys.Key<N> key, String key2, T value) {
        DataKeys.put(getPersistentData(), key, key2, value);
        ansyncKeys.add(key);
    }

    @Override
    public <T> PlayerMixin add(DataKeys.Key<T> key, T value) {
        DataKeys.add(getPersistentData(), key, value);
        ansyncKeys.add(key);
        return this;
    }

    @Override
    public <N extends NbtCompound, T> PlayerMixin add(DataKeys.Key<N> key, String key2, T value) {
        DataKeys.add(getPersistentData(), key, key2, value);
        ansyncKeys.add(key);
        return this;
    }

    @Override
    public <T> T get(DataKeys.Key<T> key) {
        return DataKeys.get(getPersistentData(), key);
    }

    @Override
    public boolean isNull() {
        return persistentData == null;
    }

    @Override
    public void sync() {
        ServerPlayerEntity player = (ServerPlayerEntity) getPlayer();
        if (player == null || player.getWorld().isClient) return;
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeNbt(getPersistentData());
        ansyncKeys.clear();
        ServerPlayNetworking.send(player, ModMessages.DATA_SYNC, buf);
    }

    @Override
    public void sync(boolean flag) {
        ServerPlayerEntity player = (ServerPlayerEntity) getPlayer();
        if (player == null || player.getWorld().isClient) return;
        PacketByteBuf buf = PacketByteBufs.create();
        NbtCompound nbt = new NbtCompound();
        for (DataKeys.Key key : ansyncKeys) DataKeys.put(nbt, key, get(key));
        buf.writeNbt(nbt);
        ansyncKeys.clear();
        ServerPlayNetworking.send(player, ModMessages.DATA_SYNC, buf);
    }


    @Override
    public void sync(DataKeys.Key... keys) {
        ServerPlayerEntity player = (ServerPlayerEntity) getPlayer();
        if (player == null || player.getWorld().isClient) return;
        PacketByteBuf buf = PacketByteBufs.create();
        NbtCompound nbt = new NbtCompound();
        for (DataKeys.Key key : keys) DataKeys.put(nbt, key, get(key));
        buf.writeNbt(nbt);
        ansyncKeys.clear();
        ServerPlayNetworking.send(player, ModMessages.DATA_SYNC, buf);
    }

    @Override
    public PlayerEntity getPlayer() {
        return (PlayerEntity) (Object) this;
    }
}
