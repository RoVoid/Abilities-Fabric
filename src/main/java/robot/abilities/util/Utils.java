package robot.abilities.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import robot.abilities.item.ModArmors;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Utils {
    public static boolean isTakeFullArmor(LivingEntity entity, ModArmors.CustomArmor armor) {
        return entity.getEquippedStack(EquipmentSlot.HEAD).getItem() == armor.HELMET && entity.getEquippedStack(EquipmentSlot.CHEST).getItem() == armor.CHESTPLATE && entity.getEquippedStack(EquipmentSlot.LEGS).getItem() == armor.LEGGINGS && entity.getEquippedStack(EquipmentSlot.FEET).getItem() == armor.BOOTS;
    }

    public static float getLastDamageTaken(LivingEntity entity) {
        try {
            Field field = LivingEntity.class.getDeclaredField("lastDamageTaken");
            field.setAccessible(true);
            return (float) field.get(entity);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        return 0;
    }

    @Environment(EnvType.CLIENT)
    public static boolean isPressed(int key) {
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), key);
    }

    public static String decimal(Number number) {
        return decimal("#.#", number);
    }

    public static String decimal(String pattern, Number number) {
        return new DecimalFormat(pattern, DecimalFormatSymbols.getInstance(Locale.US)).format(number);
    }

    public static void addParticles(ServerWorld world, ParticleEffect type, boolean longDistance, double x, double y, double z, double offsetX, double offsetY, double offsetZ, float speed, int count) {
        world.getPlayers().forEach(player -> player.networkHandler.sendPacket(new ParticleS2CPacket(type, longDistance, x, y, z, (float) offsetX, (float) offsetY, (float) offsetZ, speed, count)));
    }
}
