package robot.abilities.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipBackgroundRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import robot.abilities.client.TooltipRenderer;
import robot.abilities.client.screen.PlayerSkillsScreen;

import java.util.List;

@Mixin(DrawContext.class)
public class DrawContextMixin {
    @Inject(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V", at = @At(value = "HEAD"), cancellable = true)
    public void drawTooltip(TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, CallbackInfo ci) {
        ci.cancel();
        Screen screen = MinecraftClient.getInstance().currentScreen;
        boolean isDefault = !(screen instanceof PlayerSkillsScreen);
        TooltipComponent tooltipComponent2;
        int r;
        if (components.isEmpty()) {
            return;
        }
        int i = 0;
        int j = components.size() == 1 ? -2 : 0;
        for (TooltipComponent tooltipComponent : components) {
            int k = tooltipComponent.getWidth(textRenderer);
            if (k > i) {
                i = k;
            }
            j += tooltipComponent.getHeight();
        }
        int l = i;
        int m = j;
        Vector2ic vector2ic = positioner.getPosition(((DrawContext) (Object) this).getScaledWindowWidth(), ((DrawContext) (Object) this).getScaledWindowHeight(), x, y, l, m);
        int n = vector2ic.x();
        int o = vector2ic.y();
        ((DrawContext) (Object) this).getMatrices().push();
        int p = 400, d = 6;
        if (isDefault)
            ((DrawContext) (Object) this).draw(() -> TooltipBackgroundRenderer.render(((DrawContext) (Object) this), n, o, l, m, 400));
        else
            ((DrawContext) (Object) this).draw(() -> TooltipRenderer.render(((DrawContext) (Object) this), n - d, o - d, l + d * 2, m + d * 2, 400));
        ((DrawContext) (Object) this).getMatrices().translate(0.0f, 0.0f, 400.0f);
        int q = o;
        for (r = 0; r < components.size(); ++r) {
            tooltipComponent2 = components.get(r);
            tooltipComponent2.drawText(textRenderer, n, q, ((DrawContext) (Object) this).getMatrices().peek().getPositionMatrix(), ((DrawContext) (Object) this).getVertexConsumers());
            q += tooltipComponent2.getHeight() + (r == 0 ? 2 : 0);
        }
        q = o;
        for (r = 0; r < components.size(); ++r) {
            tooltipComponent2 = components.get(r);
            tooltipComponent2.drawItems(textRenderer, n, q, ((DrawContext) (Object) this));
            q += tooltipComponent2.getHeight() + (r == 0 ? 2 : 0);
        }
        if (!isDefault)
            ((DrawContext) (Object) this).draw(() -> TooltipRenderer.renderFramework(((DrawContext) (Object) this), n - d, o - d, 400, l + d * 2, m + d * 2));
        ((DrawContext) (Object) this).getMatrices().pop();
    }
}
