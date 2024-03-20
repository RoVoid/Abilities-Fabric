package robot.abilities.client.screen;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;
import robot.abilities.client.screen.handler.PlayerSkillsScreenHandler;
import robot.abilities.client.screen.handler.SoulFurnaceScreenHandler;
import robot.abilities.client.screen.handler.WildMagicBeaconScreenHandler;

public class ModScreens {
    public static final ScreenHandlerType<PlayerSkillsScreenHandler> PLAYER_SKILLS = ScreenHandlerRegistry.registerSimple(new Identifier(AbilitiesMod.ID, "player_skills"), PlayerSkillsScreenHandler::new);

    public static void register() {
        ScreenRegistry.register(SOUL_FURNACE, SoulFurnaceScreen::new);
        ScreenRegistry.register(PLAYER_SKILLS, PlayerSkillsScreen::new);
        ScreenRegistry.register(BEACON, WildMagicBeaconScreen::new);
    }

    public static final ScreenHandlerType<SoulFurnaceScreenHandler> SOUL_FURNACE = ScreenHandlerRegistry.registerSimple(new Identifier(AbilitiesMod.ID, "soul_furnace"), SoulFurnaceScreenHandler::new);
    public static final ScreenHandlerType<WildMagicBeaconScreenHandler> BEACON = ScreenHandlerRegistry.registerSimple(new Identifier(AbilitiesMod.ID, "beacon"), WildMagicBeaconScreenHandler::new);


}

