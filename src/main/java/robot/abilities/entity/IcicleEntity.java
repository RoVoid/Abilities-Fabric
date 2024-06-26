package robot.abilities.entity;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import robot.abilities.effect.ModEffects;
import robot.abilities.util.Utils;

public class IcicleEntity extends PersistentProjectileEntity {
    private double power = 0;

    public IcicleEntity(EntityType<? extends IcicleEntity> entityType, World world) {
        super(entityType, world, ItemStack.EMPTY);
    }

    public IcicleEntity(EntityType<? extends IcicleEntity> entityType, double x, double y, double z, World world, ItemStack stack) {
        super(ModEntities.ICICLE, x, y, z, world, ItemStack.EMPTY);
    }

    public IcicleEntity(EntityType<? extends IcicleEntity> entityType, LivingEntity owner, World world, ItemStack stack) {
        super(ModEntities.ICICLE, owner, world, ItemStack.EMPTY);
    }

    public IcicleEntity(World world, LivingEntity owner, double velX, double velY, double velZ, double power) {
        super(ModEntities.ICICLE, owner.getX(), owner.getY(), owner.getZ(), world, ItemStack.EMPTY);
        this.setOwner(owner);
        this.setVelocity(velX, velY, velZ);
        this.power = power;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        BlockPos pos = blockHitResult.getBlockPos();
        spreadIce(pos, power);
        discard();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        if (entity instanceof LivingEntity target) {
            target.addStatusEffect(new StatusEffectInstance(ModEffects.FREEZE, 200, 1));
            target.setVelocity(Vec3d.ZERO);
            if (!target.getWorld().isClient) {
                spreadIce(target.getBlockPos(), power * target.getWidth() * 0.5);
            }
            discard();
        }
    }

    @Override
    public void tick() {
        Vec3d vec3d = this.getVelocity();
        double nx = this.getX() + vec3d.x;
        double ny = this.getY() + vec3d.y;
        double nz = this.getZ() + vec3d.z;
        super.tick();
        float m = 0.999f;
        this.setVelocity(vec3d.multiply(m));
        if (!this.hasNoGravity() && !this.isNoClip()) {
            vec3d = this.getVelocity();
            this.setVelocity(vec3d.x, vec3d.y - 0.001f, vec3d.z);
        }
        this.setPosition(nx, ny, nz);
        this.checkBlockCollision();
        if (this.getWorld().isClient) {
            if (this.inGround) {
                if (this.inGroundTime % 5 == 0) {
                    this.spawnParticles(1);
                }
            } else {
                this.spawnParticles(2);
            }
        }
        if(this.getVelocity().length() < 0.05){
            spreadIce(this.getBlockPos(), power * 0.25);
            discard();
        }
    }

    private void spreadIce(BlockPos centerPos, double power) {
        if (!this.getWorld().isClient) {
            ServerWorld world = (ServerWorld) this.getWorld();
            int radius = (int) Math.ceil(power); // Adjusting the radius based on power
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        BlockPos pos = centerPos.add(x, y, z);
                        double distance = centerPos.getSquaredDistance(pos.getX(), pos.getY(), pos.getZ());
                        if (distance <= radius * radius && world.getBlockState(pos).isAir()) {
                            world.setBlockState(pos, Blocks.ICE.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    private void spawnParticles(int amount) {
        if (amount <= 0) {
            return;
        }
        if (this.getWorld() instanceof ServerWorld world) {
            Utils.addParticles(world, ParticleTypes.DRIPPING_WATER, false, this.getParticleX(0.5), this.getRandomBodyY() + 0.5, this.getParticleZ(0.5), 0, 0, 0, 0.1f, amount);
        }
    }
}
