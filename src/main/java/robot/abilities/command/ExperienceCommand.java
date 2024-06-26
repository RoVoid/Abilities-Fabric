package robot.abilities.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.util.Collection;

public class ExperienceCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("exp")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.argument("targets", EntityArgumentType.entities())
                                .then(CommandManager.literal("set")
                                        .then(CommandManager.argument("amount", IntegerArgumentType.integer())
                                                .executes(context -> executeSetExp(context.getSource(), EntityArgumentType.getEntities(context, "targets"), IntegerArgumentType.getInteger(context, "amount")))))
                                .then(CommandManager.literal("get")
                                        .executes(context -> executeGetExp(context.getSource(), EntityArgumentType.getEntities(context, "targets"))))
                                .then(CommandManager.literal("add")
                                        .then(CommandManager.argument("amount", IntegerArgumentType.integer())
                                                .executes(context -> executeAddExp(context.getSource(), EntityArgumentType.getEntities(context, "targets"), IntegerArgumentType.getInteger(context, "amount")))))
                        )
        );
    }

    public static int executeSetExp(ServerCommandSource source, Collection<? extends Entity> targets, int amount) {
        for (Entity entity : targets) {
            if (!(entity instanceof LivingEntity)) continue;
            SkillHelper.setExperience(((IPlayerMixin) entity), amount);
            ((IPlayerMixin) entity).sync();
        }
        source.sendFeedback(() -> Text.literal("Set experience to " + amount + " for " + targets.size() + " entities."), true);
        return targets.size();
    }

    public static int executeGetExp(ServerCommandSource source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (!(entity instanceof LivingEntity)) continue;
            int exp = ((IPlayerMixin) entity).get(DataKeys.EXPERIENCE);
            source.sendFeedback(() -> Text.literal("Experience of " + entity.getName() + ": " + exp), true);
        }
        return targets.size();
    }

    public static int executeAddExp(ServerCommandSource source, Collection<? extends Entity> targets, int amount) {
        for (Entity entity : targets) {
            if (!(entity instanceof LivingEntity)) continue;
            SkillHelper.addExperience(((IPlayerMixin) entity), amount);
            ((IPlayerMixin) entity).sync();
        }
        source.sendFeedback(() -> Text.literal("Added " + amount + " experience to " + targets.size() + " entities."), true);
        return targets.size();
    }
}
