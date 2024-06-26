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
import robot.abilities.util.IPlayerMixin;

import java.util.Collection;

public class ModCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        ExperienceCommand.register(dispatcher);
    }
}
