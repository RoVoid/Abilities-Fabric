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

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin implements IPlayerMixin {

    @Shadow
    @Final
    private GameProfile gameProfile;

    @Unique
    private static final NbtCompound DEFAULT = new NbtCompound();

    static {
        DataKeys.put(DEFAULT, DataKeys.MAGIC, "");
        DataKeys.put(DEFAULT, DataKeys.BORN, false);
        DataKeys.put(DEFAULT, DataKeys.SKILL, "");
        DataKeys.put(DEFAULT, DataKeys.MP, 0d);
        DataKeys.put(DEFAULT, DataKeys.MP_MAX, 10d);
        DataKeys.put(DEFAULT, DataKeys.SCORE, 0);
        DataKeys.put(DEFAULT, DataKeys.SKILLS, new NbtCompound());
        DataKeys.put(DEFAULT, DataKeys.COOLDOWN, 0);
    }

    @Unique
    private NbtCompound persistentData;

    @Override
    public NbtCompound getPersistentData() {
        if (persistentData == null) {
            persistentData = DEFAULT.copy();
            DataKeys.put(persistentData, DataKeys.UUID_KEY, gameProfile.getId());
        }
        return persistentData;
    }

    @Override
    public void setPersistentData(NbtCompound nbt) {
        if (nbt == null || nbt.isEmpty()) return;
        persistentData = nbt;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfo info) {
        nbt.put("abilities.data", getPersistentData());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("abilities.data")) setPersistentData(nbt.getCompound("abilities.data"));
    }

    @Override
    public <T> void put(DataKeys.Key<T> key, T value) {
        DataKeys.put(getPersistentData(), key, value);
    }

    @Override
    public <N extends NbtCompound, T> void put(DataKeys.Key<N> key, String key2, T value) {
        DataKeys.put(getPersistentData(), key, key2, value);
    }

    //@Override
    // public <T> void add(DataKeys.Key<T> key, T value) {
    //     DataKeys.add(getPersistentData(), key, value);
    // }

    @Override
    public <T> PlayerMixin add(DataKeys.Key<T> key, T value) {
        DataKeys.add(getPersistentData(), key, value);
        return this;
    }

    @Override
    public <T> T get(DataKeys.Key<T> key) {
        if (!isInit()) return null;
        return DataKeys.get(getPersistentData(), key);
    }

    @Override
    public boolean isInit() {
        return persistentData != null;
    }

    @Override
    public void sync() {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if (player == null || player.getWorld().isClient) return;
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeNbt(getPersistentData());
        ServerPlayNetworking.send(player, ModMessages.DATA_SYNC, buf);
    }

    @Override
    public void sync(DataKeys.Key... keys) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if (player == null || player.getWorld().isClient) return;
        PacketByteBuf buf = PacketByteBufs.create();
        NbtCompound nbt = new NbtCompound();
        for (DataKeys.Key key : keys) DataKeys.put(nbt, key, get(key));
        buf.writeNbt(nbt);
        ServerPlayNetworking.send(player, ModMessages.DATA_SYNC, buf);
    }
}
