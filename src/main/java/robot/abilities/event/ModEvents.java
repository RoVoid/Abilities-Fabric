package robot.abilities.event;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import robot.abilities.client.ManaBarOverlay;
import robot.abilities.command.ModCommands;
import robot.abilities.effect.DormantPhoenixEffect;
import robot.abilities.effect.ModEffects;

public class ModEvents {
    public static void register() {
        ServerMessageEvents.CHAT_MESSAGE.register(new ChatEvents());
        ServerTickEvents.END_SERVER_TICK.register(new PlayerEvents());
        ServerPlayerEvents.AFTER_RESPAWN.register(new PlayerEvents());
        ServerPlayerEvents.COPY_FROM.register(new PlayerEvents());
        ServerPlayConnectionEvents.JOIN.register(new PlayerEvents());
        PlayerBlockBreakEvents.BEFORE.register(new PlayerEvents());
        UseItemCallback.EVENT.register(new ItemEvents());
        ItemTooltipCallback.EVENT.register(new ItemEvents());
        ServerLivingEntityEvents.ALLOW_DEATH.register(((entity, damageSource, damageAmount) -> {
            if (entity.hasStatusEffect(ModEffects.DORMANT_PHOENIX)) {
                DormantPhoenixEffect.applyEffect(entity, entity.getStatusEffect(ModEffects.DORMANT_PHOENIX).getAmplifier());
                entity.setHealth(entity.getMaxHealth());
                return false;
            }
            return true;
        }));
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ModCommands.register(dispatcher));
    }

    public static void registerClient() {
        HudRenderCallback.EVENT.register(new ManaBarOverlay());
    }
}
