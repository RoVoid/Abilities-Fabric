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
import org.jetbrains.annotations.NotNull;
import robot.abilities.AbilitiesMod;
import robot.abilities.client.screen.handler.PlayerSkillsScreenHandler;
import robot.abilities.client.widget.SkillIconWidget;
import robot.abilities.magic.ModMagics;
import robot.abilities.magic.skill.ActiveSkills;
import robot.abilities.magic.skill.Skill;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.network.ModMessages;
import robot.abilities.util.Constants;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class PlayerSkillsScreen extends HandledScreen<PlayerSkillsScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(AbilitiesMod.ID, "textures/gui/container/skills.png");
    private final List<SkillIconWidget> skillWidgets = new ArrayList<>();
    private final List<SkillIconWidget> activeSkillWidgets = new ArrayList<>();
    private SkillIconWidget lastSkill = null, lastActiveSkill = null;
    private int activeSkillIndex;
    private boolean firstInit = true;
    private int left, top;

    public PlayerSkillsScreen(PlayerSkillsScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        left = (width - 162) / 2;
        top = (height - 173) / 2;
        initSkills(firstInit);
        initActiveSkills(firstInit);
        if (firstInit) firstInit = false;
    }

    private void initSkills(boolean firstInit) {
        skillWidgets.clear();
        IPlayerMixin cap = getPlayerMixin();
        if (cap == null) return;
        ModMagics.get(cap.get(DataKeys.MAGIC)).getAll(true).forEach(skill -> skillWidgets.add(new SkillIconWidget(skill, this::onSkill)));
        positionSkillWidgets();
    }

    private void initActiveSkills(boolean firstInit) {
        activeSkillWidgets.clear();
        IPlayerMixin cap = getPlayerMixin();
        if (cap == null) return;
        ActiveSkills.getSkillsID(cap).forEach((skillID) -> activeSkillWidgets.add(new SkillIconWidget(SkillHelper.get(skillID), this::onActiveSkill, true)));
        positionActiveSkillWidgets();
    }

    private IPlayerMixin getPlayerMixin() {
        return client != null ? (IPlayerMixin) client.player : null;
    }

    private void positionSkillWidgets() {
        int kx = 0, ky = 0;
        for (SkillIconWidget widget : skillWidgets) {
            widget.setPosition(left + 43 + kx * 29, top + 40 + ky * 29);
            widget.selected = widget == lastSkill;
            addDrawableChild(widget);
            if (++kx >= 3) {
                kx = 0;
                if (++ky >= 4) {
                    break;
                }
            }
        }
    }

    private void positionActiveSkillWidgets() {
        IPlayerMixin cap = getPlayerMixin();
        int ky = 0, i = cap.get(DataKeys.SKILL);
        for (SkillIconWidget widget : activeSkillWidgets) {
            widget.setPosition(left + 15, top + 45 + ky * 27);
            widget.selected = ky == i;
            if (widget.selected) lastActiveSkill = widget;
            addDrawableChild(widget);
            if (++ky >= 4) {
                break;
            }
        }
    }

    private void onSkill(SkillIconWidget skill) {
        if (client == null || skill.getSkill() == null) return;
        IPlayerMixin cap = getPlayerMixin();
        int level = SkillHelper.getData(cap, skill.getSkill().id(), SkillHelper.Keys.LEVEL);
        int price = Constants.getRarityPrice(skill.getSkill().getRarity());

        if (skill.selected) {
            if (cap.get(DataKeys.POINTS) >= price) {
                cap.add(DataKeys.POINTS, -price);
                SkillHelper.upLevel(cap, skill.getSkill().id(), 1);
                ClientPlayNetworking.send(ModMessages.SKILL_MANAGER_UP, PacketByteBufs.create().writeString(skill.getSkill().id()));
            }
            skill.clearTooltip();
            skill.selected = false;
        } else {
            setSkillTooltip(skill, cap, level, price);
            skill.selected = true;
        }
        updateLastSkill(skill);
    }

    private void setSkillTooltip(SkillIconWidget skill, IPlayerMixin cap, int level, int price) {
        Text tooltipText = cap.get(DataKeys.POINTS) >= price
                ? Text.translatable("container.abilities.magic_beacon.upgrade", skill.getSkill().getDisplayName().formatted(Formatting.GOLD), Text.literal(String.valueOf(level + 1)).formatted(Formatting.GOLD))
                : Text.translatable("container.abilities.magic_beacon.upgrade_fail", skill.getSkill().getDisplayName().formatted(Formatting.GOLD), Text.literal(String.valueOf(level + 1)).formatted(Formatting.GOLD));
        skill.tooltip(Tooltip.of(tooltipText));
    }

    private void updateLastSkill(SkillIconWidget skill) {
        if (lastSkill != null && lastSkill != skill) {
            lastSkill.clearTooltip();
            lastSkill.selected = false;
        }
        lastSkill = skill;
    }

    private void onActiveSkill(SkillIconWidget skill) {
        if (client == null) return;
        IPlayerMixin cap = (IPlayerMixin) client.player;
        if (skill.getSkill() != null) {
            if (lastActiveSkill != null) lastActiveSkill.selected = false;
            skill.selected = true;
            lastActiveSkill = skill;
            ClientPlayNetworking.send(ModMessages.SKILL_MANAGER_CHANGE, PacketByteBufs.create().writeNbt(cap.get(DataKeys.ACTIVE_SKILLS)).writeString(skill.getSkill().id()));
        }
    }

    private void handleMouseRelease(double mouseX, double mouseY) {
        IPlayerMixin cap = getPlayerMixin();
        boolean skillChanged = false;

        for (SkillIconWidget s : activeSkillWidgets) {
            if (s.isHovered()) {
                handleSkillSwapOrSet(cap, s);
                skillChanged = true;
                break;
            }
        }

        if (skillChanged) {
            updateActiveSkills(cap);
        } else if (activeSkillIndex >= 0) {
            ActiveSkills.clear(cap, activeSkillIndex);
            ClientPlayNetworking.send(ModMessages.SKILL_MANAGER_CHANGE, PacketByteBufs.create().writeNbt(cap.get(DataKeys.ACTIVE_SKILLS)).writeString(""));
            updateActiveSkills(cap);
        }
    }

    private void handleSkillSwapOrSet(IPlayerMixin cap, SkillIconWidget s) {
        //if (activeSkillIndex < 0) {
        //     ActiveSkills.set(cap, overlaySkill.getSkill().id(), activeSkillWidgets.indexOf(s), true);
        // } else {
        //     ActiveSkills.swap(cap, activeSkillIndex, activeSkillWidgets.indexOf(s));
        //}
        //ClientPlayNetworking.send(ModMessages.SKILL_MANAGER_CHANGE, PacketByteBuf.create().writeNbt(ActiveSkills.toNbt(cap)).writeString(overlaySkill.getSkill().id()));
    }

    private void updateActiveSkills(IPlayerMixin cap) {
        int i = 0;
        for (String skillID : ActiveSkills.getSkillsID(cap)) {
            if (activeSkillWidgets.size() <= i) break;
            Skill skill = SkillHelper.get(skillID);
            SkillIconWidget widget = activeSkillWidgets.get(i);
            widget.setSkill(skill);
            widget.selected = i == cap.get(DataKeys.SKILL);
            if (widget.selected) {
                lastActiveSkill = widget;
            }
            i++;
        }
    }

    @Override
    public void render(@NotNull DrawContext context, int mouseX, int mouseY, float delta) {
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void drawBackground(@NotNull DrawContext context, float delta, int mouseX, int mouseY) {
        IPlayerMixin cap = getPlayerMixin();
        context.drawTexture(TEXTURE, (width - 162) / 2, (height - 173) / 2, 0, 0, 162, 173, 256, 256);

        if (Math.abs(width / 2 - mouseX) <= 81 && Math.abs(top + 7 - mouseY) <= 7) {
            context.drawTooltip(textRenderer, Text.literal("Point: " + cap.get(DataKeys.POINTS) + "\nLevel: " + cap.get(DataKeys.LEVEL)), mouseX, mouseY);
        }

        int expWidth = (int) Math.floor((double) 129 * cap.get(DataKeys.EXPERIENCE) / Constants.getExperienceLimit(cap));
        context.drawTexture(TEXTURE, left + 8, top + 5, 5, 174, expWidth, 4, 256, 256);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        // Intentionally left empty
    }
}
