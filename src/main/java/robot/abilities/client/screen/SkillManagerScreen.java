package robot.abilities.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import robot.abilities.client.screen.handler.SkillManagerScreenHandler;
import robot.abilities.client.widget.SkillIconWidget;

@Environment(EnvType.CLIENT)
public class SkillManagerScreen extends HandledScreen<SkillManagerScreenHandler> {
    public SkillIconWidget button1 = SkillIconWidget.build(Text.literal("Button 1"), button -> System.out.println("You clicked button1!")).position(width / 2 - 205, 20).tooltip(Tooltip.of(Text.literal("Tooltip of button1"))).build();
    public SkillIconWidget button2 = SkillIconWidget.build(Text.literal("Button 2"), button -> System.out.println("You clicked button2!")).position(width / 2 + 5, 20).tooltip(Tooltip.of(Text.literal("Tooltip of button2"))).build();

    public SkillManagerScreen(SkillManagerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, Text.of(""));
    }

    @Override
    protected void init() {
        button1.setPosition(width / 2 - 205, 20);
        button2.setPosition(width / 2 + 5, 20);
        addDrawableChild(button1);
        addDrawableChild(button2);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawItem(new ItemStack(Items.IRON_SWORD), width / 2, height / 2);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("You must see me"), width / 2, height / 2, 0xffffff);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {

    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {

    }
}
