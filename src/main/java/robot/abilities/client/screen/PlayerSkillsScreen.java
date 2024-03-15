package robot.abilities.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;
import robot.abilities.client.screen.handler.PlayerSkillsScreenHandler;
import robot.abilities.client.widget.SkillIconWidget;
import robot.abilities.client.widget.TypeCategoryWidget;
import robot.abilities.magic.ModMagics;
import robot.abilities.magic.skill.ActiveSkills;
import robot.abilities.magic.skill.Skill;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.network.ModMessages;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;
import robot.abilities.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class PlayerSkillsScreen extends HandledScreen<PlayerSkillsScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(AbilitiesMod.ID, "textures/gui/container/skill/ncontainer.png");
    private static final Identifier LABEL_TEXTURE = new Identifier(AbilitiesMod.ID, "textures/gui/container/skill/label_border.png");
    private static final Identifier SKILL_LIST_TEXTURE = new Identifier(AbilitiesMod.ID, "textures/gui/container/skill/skill_list.png");
    private final List<TypeCategoryWidget> categoryList = new ArrayList<>();
    private final List<SkillIconWidget> activeSkillList = new ArrayList<>();
    private final Map<Skill.Type, List<SkillIconWidget>> skillList = new HashMap<>();
    private final boolean upgradeable;
    private boolean firstInit = true;
    private TypeCategoryWidget lastCategory = null;
    private SkillIconWidget lastSkill = null, lastActiveSkill = null;

    public PlayerSkillsScreen(PlayerSkillsScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        upgradeable = handler.isUpgradeable();
    }

    @Override
    protected void init() {
        initCategories(firstInit);
        initSkills(firstInit);
        initActiveSkills(firstInit);
        if (firstInit) firstInit = false;
    }

    protected void initCategories(boolean flag) {
        if (flag) {
            categoryList.clear();
            categoryList.add(TypeCategoryWidget.builder(Skill.Type.ATTACK, this::onCategory).build());
            categoryList.add(TypeCategoryWidget.builder(Skill.Type.DEFEND, this::onCategory).build());
            categoryList.add(TypeCategoryWidget.builder(Skill.Type.SUPPORT, this::onCategory).build());
            lastCategory = categoryList.get(0);
            lastCategory.selected = true;
        }
        int ky = 0;
        for (TypeCategoryWidget category : categoryList) {
            category.setPosition(width / 2 + 142, height / 2 - 48 + ky * 25);
            addDrawableChild(category);
            ky++;
        }
    }

    protected void initSkills(boolean flag) {
        if (flag) skillList.clear();
        if (client == null) return;
        IPlayerMixin cap = (IPlayerMixin) client.player;
        if (cap == null) return;
        for (Skill.Type type : Skill.Type.values()) {
            List<SkillIconWidget> skillWidgets = flag ? new ArrayList<>() : skillList.get(type);
            if (flag) {
                List<Skill> skills = SkillHelper.getSkillsWithType(cap.get(DataKeys.MAGIC), type);
                skills.forEach((skill) -> skillWidgets.add(SkillIconWidget.builder(skill, this::onSkill).build()));
                skillList.put(type, skillWidgets);
            }
            int kx = 0, ky = 0;
            for (SkillIconWidget widget : skillList.get(type)) {
                widget.setPosition(width / 2 + 52 + kx * 28, height / 2 - 46 + ky * 28);
                widget.selected = widget == lastSkill;
                addDrawableChild(widget);
                kx++;
                if (kx >= 3) {
                    kx = 0;
                    ky = (ky + 1) % 6;
                }
            }
        }
    }

    protected void initActiveSkills(boolean flag) {
        if (flag) activeSkillList.clear();
        if (client == null) return;
        IPlayerMixin cap = (IPlayerMixin) client.player;
        if (cap == null) return;
        List<SkillIconWidget> skillWidgets = flag ? new ArrayList<>() : activeSkillList;
        if (flag) {
            ActiveSkills.getSkillIDs(cap).stream().map(SkillHelper::get).forEach(skill -> activeSkillList.add(SkillIconWidget.builder(skill, this::onActiveSkill).build()));
        }
        int i = 0;
        for (SkillIconWidget widget : activeSkillList) {
            double angle = (double) i / activeSkillList.size() * 2 * Math.PI;
            widget.setPosition(width / 2 + (int) (Math.cos(angle) * 24) - 12 - 88, height / 2 + (int) (Math.sin(angle) * 24) - 12);
            widget.selected = i == cap.get(DataKeys.SKILL);
            if (widget.selected) lastActiveSkill = widget;
            addDrawableChild(widget);
            i++;
        }
    }

    private void onCategory(TypeCategoryWidget category) {
        if (lastCategory == category && lastCategory.selected) return;
        if (lastCategory != null && lastCategory.selected) {
            skillList.get(lastCategory.getSkillType()).forEach((widget) -> widget.visible = false);
            lastCategory.selected = false;
        }
        skillList.get(category.getSkillType()).forEach((widget) -> widget.visible = true);
        category.selected = true;
        this.lastCategory = category;
    }

    private void onSkill(SkillIconWidget skill) {
        if (lastSkill != null) lastSkill.selected = false;
        if ((skill == lastSkill && skill.selected) || client == null) return;
        skill.selected = true;
        this.lastSkill = skill;
    }

    private void onActiveSkill(SkillIconWidget skill) {
        if (client == null) return;
        IPlayerMixin cap = (IPlayerMixin) client.player;
        if (lastSkill != null && lastSkill.selected) {
            lastSkill.selected = false;
            if (skill.getSkill() != lastSkill.getSkill()) {
                skill.setSkill(lastSkill.getSkill());
                ActiveSkills.set(cap, lastSkill.getSkill().id(), activeSkillList.indexOf(skill), true);
                ClientPlayNetworking.send(ModMessages.SKILL_MANAGER_CHANGE, PacketByteBufs.create().writeNbt(ActiveSkills.toNbt(cap)).writeString(lastSkill.getSkill().id()));
            }
        } else if (lastActiveSkill != null && !skill.selected && skill.getSkill() != null) {
            ActiveSkills.set(cap, skill.getSkill().id(), activeSkillList.indexOf(skill), true);
            ClientPlayNetworking.send(ModMessages.SKILL_MANAGER_CHANGE, PacketByteBufs.create().writeNbt(ActiveSkills.toNbt(cap)).writeString(skill.getSkill().id()));
        }
        if (skill.getSkill() != null) {
            if (lastActiveSkill != null) lastActiveSkill.selected = false;
            skill.selected = true;
            this.lastActiveSkill = skill;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        IPlayerMixin cap = (IPlayerMixin) client.player;
        context.drawTexture(TEXTURE, width / 2 - 42, height / 2 - 52, 0, 0, 84, 104, 84, 104);
        context.drawTexture(LABEL_TEXTURE, width / 2 - 17, height / 2 - 44, 0, 0, 34, 8, 34, 9);
        context.drawText(textRenderer, cap.get(DataKeys.LEVEL).toString(), (width - textRenderer.getWidth(cap.get(DataKeys.LEVEL).toString())) / 2, height / 2 - 34, 0xFFFFFFFF, true);
        context.drawTexture(LABEL_TEXTURE, width / 2 - 17, height / 2 - 34 + textRenderer.fontHeight, 0, 8, 34, 1, 34, 9);
        MutableText magicName = Text.translatable(ModMagics.getMagic(cap.get(DataKeys.MAGIC)).getTranslateKey());
        context.drawText(textRenderer, magicName, (width - textRenderer.getWidth(magicName)) / 2, height / 2 - 26 + textRenderer.fontHeight, 0xFFFFFFFF, true);
        double mana = cap.get(DataKeys.MANA), max_mana = cap.get(DataKeys.MAX_MANA);
        MutableText manaText = Text.literal((mana > max_mana ? Utils.decimal("#", max_mana) + "+" : Utils.decimal("#.#", mana)) + " /");
        MutableText manaFullText = Text.empty().append(manaText).append(" " + Utils.decimal("#", max_mana));
        context.drawText(textRenderer, manaFullText, width / 2 - textRenderer.getWidth(manaText) + textRenderer.getWidth(" ") / 2, height / 2 - 18 + textRenderer.fontHeight * 2, 0xFFFFFFFF, true);
        MutableText pointText = Text.literal(cap.get(DataKeys.POINTS).toString());
        context.drawText(textRenderer, pointText, (width - textRenderer.getWidth(pointText)) / 2, height / 2 - 12 + textRenderer.fontHeight * 3, 0xFFFFFFFF, true);
        context.drawTexture(SKILL_LIST_TEXTURE, width / 2 + 46, height / 2 - 52, 0, 0, 98, 78, 116, 78);
//        context.drawTexture(EXPERIENCE_BARS, width / 2 - 51, height / 2 - 80, 0, 5, 102, 5, 102, 10);
//        double m = Math.min(1, ((IPlayerMixin) client.player).get(DataKeys.EXPERIENCE) / Constants.getExperienceLimit((IPlayerMixin) client.player));
//        context.drawTexture(EXPERIENCE_BARS, width / 2 - 51, height / 2 - 80, 0, 0, (int) Math.floor(102 * m), 5, 102, 10);
//        context.drawText(textRenderer, ((IPlayerMixin) client.player).get(DataKeys.POINTS) + "", width / 2 - 51, height / 2 - 82 - textRenderer.fontHeight, 0xFFFFFF, false);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {

    }
}
