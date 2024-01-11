package robot.abilities.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.client.gui.screen.recipebook.FurnaceRecipeBookScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;
import robot.abilities.client.screen.handler.SoulFurnaceScreenHandler;

@Environment(EnvType.CLIENT)
public class SoulFurnaceScreen
        extends AbstractFurnaceScreen<SoulFurnaceScreenHandler> {
    private static final Identifier LIT_PROGRESS_TEXTURE = new Identifier(AbilitiesMod.ID, "container/soul_furnace/lit_progress");
    private static final Identifier BURN_PROGRESS_TEXTURE = new Identifier(AbilitiesMod.ID, "container/soul_furnace/burn_progress");
    private static final Identifier TEXTURE = new Identifier(AbilitiesMod.ID, "textures/gui/container/soul_furnace/soul_furnace.png");

    public SoulFurnaceScreen(SoulFurnaceScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, new FurnaceRecipeBookScreen(), inventory, title, TEXTURE, LIT_PROGRESS_TEXTURE, BURN_PROGRESS_TEXTURE);
    }
}
