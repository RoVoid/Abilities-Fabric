package robot.abilities.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;
import robot.abilities.client.screen.handler.WildMagicBeaconScreenHandler;
import robot.abilities.client.widget.ImageButtonWidget;
import robot.abilities.client.widget.SkillIconWidget;
import robot.abilities.client.widget.TypeCategoryWidget;
import robot.abilities.magic.skill.ActiveSkills;
import robot.abilities.magic.skill.Skill;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.network.ModMessages;
import robot.abilities.util.Constants;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class WildMagicBeaconScreen extends HandledScreen<WildMagicBeaconScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(AbilitiesMod.ID, "textures/gui/container/skill/container.png");
    private static final Identifier EXPERIENCE_BARS = new Identifier(AbilitiesMod.ID, "textures/gui/container/skill/point_bars.png");
    private final List<TypeCategoryWidget> categoryList = new ArrayList<>();
    private final Map<Skill.Type, List<SkillIconWidget>> skillList = new HashMap<>();
    private final List<SkillIconWidget> mainSkillList = new ArrayList<>();
    private final ImageButtonWidget changeButton = new ImageButtonWidget(new Identifier(AbilitiesMod.ID, "textures/gui/container/skill/change_button.png"), 0, 0, 28, 28, this::change).wh(28, 14).hover(0, 0).enabled(0, 0).disabled(0, 14);
    private final ImageButtonWidget pointsTrigger = new ImageButtonWidget(null, 0, 0, 102, 5, null);
    private TypeCategoryWidget lastCategory = null;
    private SkillIconWidget lastSkill = null, lastMainSkill = null;
    private boolean firstInit = true;

    public WildMagicBeaconScreen(WildMagicBeaconScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        initCategories(firstInit);
        initSkills(firstInit);
        initMainSkills(firstInit);
        if (firstInit) {
            firstInit = false;
            onCategory(categoryList.get(0));
        }
        changeButton.setPosition(width / 2 + 55, height / 2 - 84);
        pointsTrigger.setPosition(width / 2 - 51, height / 2 - 80);
        addDrawableChild(changeButton);
        addDrawableChild(pointsTrigger);
    }

    protected void initCategories(boolean flag) {
        if (flag) {
            this.categoryList.clear();
            this.categoryList.add(TypeCategoryWidget.builder(Skill.Type.ATTACK, this::onCategory).build());
            this.categoryList.add(TypeCategoryWidget.builder(Skill.Type.DEFEND, this::onCategory).build());
            this.categoryList.add(TypeCategoryWidget.builder(Skill.Type.SUPPORT, this::onCategory).build());
        }
        int ky = 0;
        for (TypeCategoryWidget category : categoryList) {
            category.setPosition(width / 2 - 71, height / 2 - 60 + ky * 26);
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
                List<Skill> skills = SkillHelper.getSkillsWithType(cap.get(DataKeys.MAGIC), category.getSkillType());
                skills.forEach((skill) -> skillWidgets.add(SkillIconWidget.builder(skill, this::onSkill).build()));
                skillList.put(category.getSkillType(), skillWidgets);
            }
            int kx = 0, ky = 0;
            for (SkillIconWidget widget : skillList.get(category.getSkillType())) {
                widget.setPosition(width / 2 - 41 + kx * 30, height / 2 - 64 + ky * 30);
                widget.visible = category == lastCategory;
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

    protected void initMainSkills(boolean flag) {
        if (flag) this.mainSkillList.clear();
        if (client == null) return;
        IPlayerMixin cap = (IPlayerMixin) client.player;
        if (cap == null) return;
        List<SkillIconWidget> skillWidgets = flag ? new ArrayList<>() : mainSkillList;
        if (flag) {
            ActiveSkills.getSkillsID(cap).stream().map(SkillHelper::get).forEach(skill -> mainSkillList.add(SkillIconWidget.builder(skill, this::onMainSkill).build()));
        }
        int ky = 0;
        for (SkillIconWidget widget : mainSkillList) {
            widget.setPosition(width / 2 + 52, height / 2 - 60 + ky * 26);
            widget.selected = lastMainSkill == widget;
            addDrawableChild(widget);
            ky++;
        }

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1);
        super.render(context, mouseX, mouseY, delta);
    }

    private void change(ImageButtonWidget imageButtonWidget) {
        client.player.closeHandledScreen();
        ClientPlayNetworking.send(ModMessages.SKILL_MANAGER, PacketByteBufs.create().writeBoolean(true));
    }

    private void onCategory(TypeCategoryWidget category) {
        if (lastCategory == category && lastCategory.selected) return;
        if (lastCategory != null) {
            if (lastCategory.selected) {
                skillList.get(lastCategory.getSkillType()).forEach((widget) -> widget.visible = false);
                lastCategory.selected = false;
            }
        }
        skillList.get(category.getSkillType()).forEach((widget) -> widget.visible = true);
        category.selected = true;
        this.lastCategory = category;
    }

    private void onSkill(SkillIconWidget skill) {
        if (client == null) return;
        IPlayerMixin cap = (IPlayerMixin) client.player;
        int level = SkillHelper.getData(cap, skill.getSkill().id(), SkillHelper.Keys.LEVEL);
        int price = (int) Math.floor(skill.getSkill().get("price", Math.max(level, 1)));
        if (skill.selected) {
            if (cap.get(DataKeys.POINTS) >= price) {
                cap.add(DataKeys.POINTS, -price);
                SkillHelper.upLevel(cap, skill.getSkill().id(), 1);
                ClientPlayNetworking.send(ModMessages.SKILL_MANAGER_UP, PacketByteBufs.create().writeString(skill.getSkill().id()));
                //  if (level <= 0) skill.canUse = true;
                level++;
            }
            skill.selected = false;
        } else {
            Text text;
            if (cap.get(DataKeys.POINTS) >= price)
                text = Text.translatable("container.abilities.magic_beacon.upgrade", skill.getSkill().getDisplayName().formatted(Formatting.GOLD), Text.literal(String.valueOf(level + 1)).formatted(Formatting.GOLD));
            else
                text = Text.translatable("container.abilities.magic_beacon.upgrade_fail", skill.getSkill().getDisplayName().formatted(Formatting.GOLD), Text.literal(String.valueOf(level + 1)).formatted(Formatting.GOLD));
            skill.setTooltip(Tooltip.of(text));
            skill.selected = true;
        }
        if (lastSkill != null && lastSkill != skill) {
            lastSkill.selected = false;
        }
        if (lastMainSkill != null && lastMainSkill.getSkill() != skill.getSkill()) {
            lastMainSkill.selected = false;
        }
        this.lastSkill = skill;
    }

    private void onMainSkill(SkillIconWidget skill) {
        if (client == null || skill.getSkill() == null) return;
        IPlayerMixin cap = (IPlayerMixin) client.player;
        int level = SkillHelper.getData(cap, skill.getSkill().id(), SkillHelper.Keys.LEVEL);
        int price = (int) Math.floor(skill.getSkill().get("price", Math.max(level, 1)));
        if (skill.selected) {
            if (cap.get(DataKeys.POINTS) >= price) {
                cap.add(DataKeys.POINTS, -price);
                SkillHelper.upLevel(cap, skill.getSkill().id(), 1);
                ClientPlayNetworking.send(ModMessages.SKILL_MANAGER_UP, PacketByteBufs.create().writeString(skill.getSkill().id()));
            }
            skill.selected = false;
        } else {
            Text text;
            if (cap.get(DataKeys.POINTS) >= price)
                text = Text.translatable("container.abilities.magic_beacon.upgrade", skill.getSkill().getDisplayName().formatted(Formatting.GOLD), Text.literal(String.valueOf(level + 1)).formatted(Formatting.GOLD));
            else
                text = Text.translatable("container.abilities.magic_beacon.upgrade_fail", skill.getSkill().getDisplayName().formatted(Formatting.GOLD), Text.literal(String.valueOf(level + 1)).formatted(Formatting.GOLD));
            skill.setTooltip(Tooltip.of(text));
            skill.selected = true;
        }
        if (lastMainSkill != null && lastMainSkill != skill) {
            lastMainSkill.selected = false;
        }
        if (lastSkill != null && lastSkill.getSkill() != skill.getSkill()) {
            lastSkill.selected = false;
        }
        this.lastMainSkill = skill;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        IPlayerMixin cap = (IPlayerMixin) client.player;
        context.drawTexture(TEXTURE, width / 2 - 49, height / 2 - 72, 0, 0, 98, 144, 116, 144);
        context.drawTexture(EXPERIENCE_BARS, width / 2 - 51, height / 2 - 80, 0, 5, 102, 5, 102, 10);
        double d = cap.get(DataKeys.LEVEL) <= 1 ? 0 : Constants.getExperienceLimit(cap.get(DataKeys.LEVEL) - 1);
        double experience = cap.get(DataKeys.EXPERIENCE), experienceLimit = Constants.getExperienceLimit(cap), experienceRatio = Math.min(1, (experience - d) / (experienceLimit - d));
        int experienceWidth = (int) Math.floor(102 * experienceRatio);
        context.drawTexture(EXPERIENCE_BARS, width / 2 - 51, height / 2 - 80, 0, 0, experienceWidth, 5, 102, 10);
        context.drawText(textRenderer, String.valueOf(cap.get(DataKeys.POINTS)), width / 2 - 51, height / 2 - 82 - textRenderer.fontHeight, 0xFFFFFF, false);
        if (pointsTrigger.isHovered()) {
            context.drawTooltip(textRenderer, Text.literal(experience + " / " + experienceLimit), mouseX, mouseY);
        }
    }


    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {

    }
}
