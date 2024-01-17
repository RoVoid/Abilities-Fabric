package robot.abilities.client;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.util.Objects;

public class ManaBarOverlay implements HudRenderCallback {
    public static final Identifier MANA_BAR = new Identifier(AbilitiesMod.ID, "textures/gui/mana_bar.png");
    public static final Identifier MANA_BALL = new Identifier(AbilitiesMod.ID, "textures/gui/mana_ball.png");

    final int x = 5, y = 5;

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;
        IPlayerMixin cap = (IPlayerMixin) client.player;
        if (cap == null || !cap.isInit() || cap.get(DataKeys.MAGIC).isEmpty()) return;
        double m = Math.min(1, cap.get(DataKeys.MP) / cap.get(DataKeys.MP_MAX));
        context.drawTexture(MANA_BAR, x, y, 0, 0, 0, 130, 18, 130, 21);
        if (m > 0) context.drawTexture(MANA_BAR, x + 20, y + 5, 0, 0, 18, (int) Math.floor(106 * m), 3, 130, 21);
        if (Objects.requireNonNull(client.interactionManager).hasStatusBars()) {
            int x = context.getScaledWindowWidth() / 2 - 9, y = context.getScaledWindowHeight() - 54;
            int h = (int) Math.floor(18 * m);
            context.drawTexture(MANA_BALL, x, y, 0, 0, 0, 18, 18, 36, 18);
            if (h > 0) context.drawTexture(MANA_BALL, x, y + 18 - h, 0, 18, 18 - h, 18, h, 36, 18);
        }
    }
}
