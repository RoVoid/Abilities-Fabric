package robot.abilities.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;

@Environment(EnvType.CLIENT)
public class TooltipRenderer {
    public static final int BACKGROUND_COLOR = 0xFF77AAFF;
    public static final int BORDER_COLOR = 0xFF3366BB;
    public static final Identifier BACKGROUND_TEXTURE = new Identifier(AbilitiesMod.ID, "textures/gui/container/skill/tooltip.png");

    public static final int d = 2;

    public static void render(DrawContext context, int x, int y, int width, int height, int z) {
        renderBorder(context, x, y, width, height, z, BACKGROUND_COLOR, BORDER_COLOR);
        // renderFramework(context, x - d, y - d, width + d * 2, height + d * 2);
    }

    public static void renderFramework(DrawContext context, int x, int y, int z, int width, int height) {
        context.drawTexture(BACKGROUND_TEXTURE, x, y, z + 1, 0, 0, 13, 13, 26, 26);
        context.drawTexture(BACKGROUND_TEXTURE, x + width - 13, y, z + 1, 13, 0, 13, 13, 26, 26);
        context.drawTexture(BACKGROUND_TEXTURE, x, y + height - 13, z + 1, 0, 13, 13, 13, 26, 26);
        context.drawTexture(BACKGROUND_TEXTURE, x + width - 13, y + height - 13, z + 1, 13, 13, 13, 13, 26, 26);
    }

    private static void renderBorder(DrawContext context, int x, int y, int width, int height, int z, int color, int color2) {
        fill(context, x, y, width, height, z, color);
        //
        fillHorizontalLine(context, x - 1, y - 1, width + 2, z, color2);
        fillHorizontalLine(context, x - 1, y + height, width + 2, z, color2);
        fillVerticalLine(context, x - 1, y - 1, height + 2, z, color2);
        fillVerticalLine(context, x + width, y - 1, height + 2, z, color2);
        //
        fillHorizontalLine(context, x, y - 2, width, z, color);
        fillHorizontalLine(context, x, y + height + 1, width, z, color);
        fillVerticalLine(context, x - 2, y, height, z, color);
        fillVerticalLine(context, x + width + 1, y, height, z, color);
    }

    private static void fill(DrawContext context, int x, int y, int width, int height, int z, int color) {
        context.fill(x, y, x + width, y + height, z, color);
    }

    private static void fillVerticalLine(DrawContext context, int x, int y, int height, int z, int color) {
        context.fill(x, y, x + 1, y + height, z, color);
    }

    private static void fillHorizontalLine(DrawContext context, int x, int y, int width, int z, int color) {
        context.fill(x, y, x + width, y + 1, z, color);
    }
}
