package robot.abilities.magic.skill.water;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import robot.abilities.AbilitiesMod;
import robot.abilities.magic.skill.Skill;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.text.DecimalFormat;

public class FertilitySkill extends Skill {
    public FertilitySkill() {
        super(AbilitiesMod.ID + ".fertility", Type.SUPPORT, new Property(0.1, 0.2), new Property(5), new Property(1));
        add("radius", new Property(0, 0.5));
    }

    @Override
    public boolean use(LivingEntity user, int level) {
        World world = user.getWorld();
        if (world.isClient) return false;
        int r = (int) Math.floor(get("radius", level));
        BlockPos pos = new BlockPos((int) Math.floor(user.getX()), (int) Math.round(user.getY()), (int) Math.floor(user.getZ()));
        Fertilizable fertilizable;
        boolean used = false;
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos p = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    BlockState state = world.getBlockState(p);
                    Block block = state.getBlock();
                    if (block instanceof Fertilizable && (fertilizable = (Fertilizable) block).isFertilizable(world, pos, state)) {
                        if (fertilizable.canGrow(world, world.random, pos, state)) {
                            fertilizable.grow((ServerWorld) world, world.random, pos, state);
                            used = true;
                        }
                    }
                }
            }
        }
        return used;
    }

    @Override
    public void usePlayer(PlayerEntity player, int level) {
        if (!canUse(player, level)|| player.getWorld().isClient) return;
        if (!use(player, level)) return;
        double mp = get("mp", level);
        IPlayerMixin cap = (IPlayerMixin) player;
        SkillHelper.addPoints(cap, 5);
        cap.add(DataKeys.MP, -mp);
        cap.sync(false);
    }

    @Override
    public boolean canUse(PlayerEntity player, int level) {
        return super.canUse(player, level) && ((IPlayerMixin) player).get(DataKeys.MP) >= get("mp", level);
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip", Text.literal(new DecimalFormat("#.#").format(get("radius", level) + 1)).formatted(Formatting.GOLD), Text.literal(new DecimalFormat("#.#").format(get("mp", level))).formatted(Formatting.GOLD));
    }
}
