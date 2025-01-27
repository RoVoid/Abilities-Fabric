package robot.abilities.magic.skill.air;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import robot.abilities.AbilitiesMod;
import robot.abilities.event.PlayerTakeDamageCallback;
import robot.abilities.magic.property.DoubleProperty;
import robot.abilities.magic.property.IntProperty;
import robot.abilities.magic.skill.Skill;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;
import robot.abilities.util.Utils;

public class EvasionSkill extends Skill {
    public final IntProperty DISTANCE = new IntProperty(1, 0.2);

    public EvasionSkill() {
        super(AbilitiesMod.ID, "evasion", Type.DEFEND, Rarity.RARE, new DoubleProperty(1.0), new IntProperty(10));
    }

    @Override
    public boolean use(LivingEntity user, int level) {
        return false;
    }

    @Override
    public void usePlayer(PlayerEntity player, int level) {
        if (!canPlayerUse(player, level) || player.getWorld().isClient) return;
        IPlayerMixin cap = (IPlayerMixin) player;
        cap.put(DataKeys.ARGS, id() + ":count", 2);
        afterUsing(cap, level);
    }


    @Override
    public void applyEventsHandler() {
        PlayerTakeDamageCallback.EVENT.register(((player, source, amount) -> {
            if (player.getWorld().isClient()) return true;
            IPlayerMixin cap = (IPlayerMixin) player;
            if (!SkillHelper.hasSkill(cap, id())) return true;
            if (cap.get(DataKeys.ARGS).getInt(id() + ":count") <= 0) return true;
            cap.add(DataKeys.ARGS, id() + ":count", -1);
            cap.sync();
            return !tryTeleport(player, source, DISTANCE.get(SkillHelper.getData(cap, id(), SkillHelper.Keys.LEVEL)));
        }));
    }

    public static boolean tryTeleport(LivingEntity entity, DamageSource source, double distance) {
        if (!(entity.getWorld() instanceof ServerWorld world)) return false;

        Vec3d direction = source.getPosition() != null
                ? source.getPosition().subtract(entity.getPos()).normalize()
                : entity.getRotationVec(1.0F).normalize();

        Vec3d teleportDirection = Math.random() < 0.5
                ? direction.crossProduct(new Vec3d(0, 1, 0)).normalize()
                : direction.multiply(-1);

        for (int attempts = 0; attempts < 10; attempts++) {
            double offset = (attempts + 1) * 0.5;
            int x = MathHelper.floor(entity.getX() + teleportDirection.x * distance * offset);
            int z = MathHelper.floor(entity.getZ() + teleportDirection.z * distance * offset);
            int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z);

            BlockPos pos = new BlockPos(x, y, z);
            if (world.isAir(pos) && world.isAir(pos.up()) && world.getBlockState(pos.down()).isSolidBlock(world, pos.down())) {
                entity.teleport(x + 0.5, y, z + 0.5);
                return true;
            }
        }
        return false;
    }


    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip",
                Text.literal(Utils.decimal("#", DISTANCE.get(level))).formatted(Formatting.GOLD),
                Text.literal(Utils.decimal("#.#", MP.get(level))).formatted(Formatting.GOLD));
    }
}
