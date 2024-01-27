package robot.abilities.client;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;
import robot.abilities.event.KetInputHandler;
import robot.abilities.magic.skill.MainSkills;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.util.Objects;

public class ManaBarOverlay implements HudRenderCallback {
    public static final Identifier MANA_BAR = new Identifier(AbilitiesMod.ID, "textures/gui/hud/mana_bar.png");
    public static final Identifier MANA_BALL = new Identifier(AbilitiesMod.ID, "textures/gui/hud/mana_ball.png");
    public static final Identifier SKILL_USE = new Identifier(AbilitiesMod.ID, "textures/gui/hud/skill_use_indicator.png");
    public static final Identifier SKILL_USE_PROGRESS = new Identifier(AbilitiesMod.ID, "textures/gui/hud/skill_use_indicator_progress.png");
    public static final Identifier SKILL_USE_FULL = new Identifier(AbilitiesMod.ID, "textures/gui/hud/skill_use_indicator_full.png");

    final int x = 5, y = 5;

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        IPlayerMixin cap = (IPlayerMixin) MinecraftClient.getInstance().player;
        if (!cap.get(DataKeys.MAGIC).isEmpty()) {
            drawManaHud(context, tickDelta, cap);
            if (KetInputHandler.pressed > 0) drawSkillUse(context, tickDelta, cap);
        }

    }

    public void drawManaHud(DrawContext context, float tickDelta, IPlayerMixin cap) {
        double m = Math.min(1, cap.get(DataKeys.MP) / cap.get(DataKeys.MP_MAX));
        context.drawTexture(MANA_BAR, x, y, 0, 0, 0, 130, 18, 130, 21);
        if (m > 0) context.drawTexture(MANA_BAR, x + 20, y + 5, 0, 0, 18, (int) Math.floor(106 * m), 3, 130, 21);
        if (Objects.requireNonNull(MinecraftClient.getInstance().interactionManager).hasStatusBars()) {
            int x = context.getScaledWindowWidth() / 2 - 9, y = context.getScaledWindowHeight() - 54;
            int h = (int) Math.floor(18 * m);
            context.drawTexture(MANA_BALL, x, y, 0, 0, 0, 18, 18, 36, 18);
            if (h > 0) context.drawTexture(MANA_BALL, x, y + 18 - h, 0, 18, 18 - h, 18, h, 36, 18);
        }
    }

    public void drawSkillUse(DrawContext context, float tickDelta, IPlayerMixin cap) {
        if (Objects.requireNonNull(MinecraftClient.getInstance().interactionManager).hasStatusBars()) {
            int x = context.getScaledWindowWidth() / 2 - 8, y = context.getScaledWindowHeight() / 2 + 8;
            int level = SkillHelper.getData(cap, MainSkills.get(cap).getID(), SkillHelper.Keys.LEVEL);
            int castTime = MainSkills.get(cap) == null ? 1 : (int) Math.floor(MainSkills.get(cap).get("castTime", level)) - SkillHelper.getData(cap, null, SkillHelper.Keys.CAST_TIME);
            double m = (double) KetInputHandler.pressed / castTime;
            if (m <= 1) {
                context.drawTexture(SKILL_USE, x, y, 0, 0, 0, 16, 4, 16, 4);
                context.drawTexture(SKILL_USE_PROGRESS, x, y, 0, 0, 0, (int) Math.floor(16 * m), 4, 16, 4);
            } else {
                context.drawTexture(SKILL_USE_FULL, x, y, 0, 0, 0, 16, 16, 16, 16);
            }
        }
    }
}
