package robot.abilities.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import robot.abilities.magic.skill.Skill;
import robot.abilities.util.IPlayerMixin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillEvents {
    private static final Map<Events, List<Skill>> SUBSCRIBE_EVENTS = new HashMap<>();

    public enum Events {
        USE_BLOCK, USE_ITEM, ATTACK_ENTITY, DEATH_ENTITY, BLOCK_BREAK, CHANGE_DIMENSION;
    }

    public static void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.isClient) return ActionResult.CONSUME;
            IPlayerMixin cap = (IPlayerMixin) player;
            NbtCompound nbt = new NbtCompound();
            SUBSCRIBE_EVENTS.get(Events.USE_BLOCK).forEach((skill -> skill.eventHandle(cap, (ServerWorld) world, nbt)));
            return ActionResult.CONSUME;
        });

        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (world.isClient) return TypedActionResult.consume(stack);
            IPlayerMixin cap = (IPlayerMixin) player;
            NbtCompound nbt = new NbtCompound();
            SUBSCRIBE_EVENTS.get(Events.USE_ITEM).forEach((skill -> skill.eventHandle(cap, (ServerWorld) world, nbt)));
            return TypedActionResult.consume(stack);
        });

        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (world.isClient) return ActionResult.CONSUME;
            IPlayerMixin cap = (IPlayerMixin) player;
            NbtCompound nbt = new NbtCompound();
            SUBSCRIBE_EVENTS.get(Events.ATTACK_ENTITY).forEach((skill -> skill.eventHandle(cap, (ServerWorld) world, nbt)));
            return ActionResult.CONSUME;
        });

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (entity instanceof PlayerEntity player && !player.getWorld().isClient) {
                IPlayerMixin cap = (IPlayerMixin) player;
                NbtCompound nbt = new NbtCompound();
                SUBSCRIBE_EVENTS.get(Events.DEATH_ENTITY).forEach((skill -> skill.eventHandle(cap, (ServerWorld) player.getWorld(), nbt)));
            }
        });

        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
            if (world.isClient) return;
            IPlayerMixin cap = (IPlayerMixin) player;
            NbtCompound nbt = new NbtCompound();
            SUBSCRIBE_EVENTS.get(Events.BLOCK_BREAK).forEach((skill -> skill.eventHandle(cap, (ServerWorld) world, nbt)));
        });

        PlayerChangeDimensionCallback.EVENT.register(((player, world) -> {
            if (world.isClient) return;
            IPlayerMixin cap = (IPlayerMixin) player;
            NbtCompound nbt = new NbtCompound();
            SUBSCRIBE_EVENTS.get(Events.CHANGE_DIMENSION).forEach((skill -> skill.eventHandle(cap, (ServerWorld) world, nbt)));
        }));
    }

    public void subscribe(Skill skill, Events event) {
        SUBSCRIBE_EVENTS.get(event).add(skill);
    }
}
