package robot.abilities.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;
import robot.abilities.client.screen.handler.SkillManagerScreenHandler;
import robot.abilities.client.widget.SkillIconWidget;
import robot.abilities.client.widget.TypeCategoryWidget;
import robot.abilities.magic.ModMagics;
import robot.abilities.magic.skill.AbstractSkill;
import robot.abilities.network.ModMessages;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class SkillManagerScreen extends HandledScreen<SkillManagerScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(AbilitiesMod.ID, "textures/gui/container/skill/skill_container.png");
    private final List<TypeCategoryWidget> categoryList = new ArrayList<>();
    private final Map<AbstractSkill.Type, List<SkillIconWidget>> skillList = new HashMap<>();
    private TypeCategoryWidget openedCategory = null;
    private SkillIconWidget openedSkill = null;
    private boolean firstInit = true;

    public SkillManagerScreen(SkillManagerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        initCategories(firstInit);
        initSkills(firstInit);
        if (firstInit) {
            firstInit = false;
            openCategory(categoryList.get(0));
        }
    }

    protected void initCategories(boolean flag) {
        if (flag) {
            this.categoryList.clear();
            this.categoryList.add(TypeCategoryWidget.builder(AbstractSkill.Type.ATTACK, this::openCategory).item(Items.IRON_SWORD).build());
            this.categoryList.add(TypeCategoryWidget.builder(AbstractSkill.Type.DEFEND, this::openCategory).item(Items.SHIELD).build());
            this.categoryList.add(TypeCategoryWidget.builder(AbstractSkill.Type.SUPPORT, this::openCategory).item(Items.POTION).build());
        }
        int ky = 0;
        for (TypeCategoryWidget category : categoryList) {
            category.setPosition(width / 2 - 71, height / 2 - 60 + ky * 30);
            addDrawableChild(category);
            ky++;
        }
    }

    protected void initSkills(boolean flag) {
        if (flag) this.skillList.clear();
        if (client == null) return;
        IPlayerMixin cap = (IPlayerMixin) client.player;
        if (cap == null) return;
        for (TypeCategoryWidget category : categoryList) {
            List<SkillIconWidget> skillWidgets = flag ? new ArrayList<>() : skillList.get(category.getSkillType());
            if (flag) {
                List<AbstractSkill> skills = ModMagics.getSkillsWithType(cap.get(DataKeys.MAGIC), category.getSkillType());
                skills.forEach((skill) -> skillWidgets.add(SkillIconWidget.builder(skill, this::openSkill).build()));
                skillList.put(category.getSkillType(), skillWidgets);
            }
            int kx = 0, ky = 0;
            for (SkillIconWidget widget : skillList.get(category.getSkillType())) {
                widget.setPosition(width / 2 - 41 + kx * 30, height / 2 - 64 + ky * 30);
                widget.visible = category == openedCategory;
                if (openedSkill == null && cap.get(DataKeys.SKILL).equals(widget.getSkill().getName()))
                    openedSkill = widget;
                widget.selected = widget == openedSkill;
                addDrawableChild(widget);
                kx++;
                if (kx >= 3) {
                    kx = 0;
                    ky = (ky + 1) % 6;
                }
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1);
        super.render(context, mouseX, mouseY, delta);
    }

    private void openCategory(TypeCategoryWidget category) {
        if (openedCategory == category) return;
        if (openedCategory != null) {
            skillList.get(openedCategory.getSkillType()).forEach((widget) -> widget.visible = false);
            openedCategory.selected = false;
        }
        skillList.get(category.getSkillType()).forEach((widget) -> widget.visible = true);
        category.selected = true;
        this.openedCategory = category;
    }

    private void openSkill(SkillIconWidget skill) {
        AbilitiesMod.LOGGER.info(skill.getSkill().getName());
        if (openedSkill == skill || client == null) return;
        AbilitiesMod.LOGGER.info("1");
        IPlayerMixin cap = (IPlayerMixin) client.player;
        if (!skill.canUse) return;
        AbilitiesMod.LOGGER.info("2");
        if (openedSkill != null) {
            openedSkill.selected = false;
        }
        AbilitiesMod.LOGGER.info("3");
        ClientPlayNetworking.send(ModMessages.SKILL_MANAGER_CHANGE, PacketByteBufs.create().writeString(skill.getSkill().getName()));
        AbilitiesMod.LOGGER.info("4");
        skill.selected = true;
        this.openedSkill = skill;
        AbilitiesMod.LOGGER.info("5");
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(TEXTURE, width / 2 - 49, height / 2 - 72, 0, 0, 98, 144, 156, 144);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {

    }
}
