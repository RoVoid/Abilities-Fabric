package robot.abilities.entity;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WaterBallEntity extends PersistentProjectileEntity {

    public WaterBallEntity(EntityType<? extends WaterBallEntity> entityType, World world) {
        super(entityType, world, ItemStack.EMPTY);
    }

    public WaterBallEntity(EntityType<? extends WaterBallEntity> entityType, double x, double y, double z, World world, ItemStack stack) {
        super(ModEntities.WATER_BALL, x, y, z, world, ItemStack.EMPTY);
    }

    public WaterBallEntity(EntityType<? extends WaterBallEntity> entityType, LivingEntity owner, World world, ItemStack stack) {
        super(ModEntities.WATER_BALL, owner, world, ItemStack.EMPTY);
    }

    public WaterBallEntity(World world, LivingEntity owner, double velX, double velY, double velZ) {
        super(ModEntities.WATER_BALL, owner.getX(), owner.getY(), owner.getZ(), world, ItemStack.EMPTY);
        this.setOwner(owner);
        this.setVelocity(velX, velY, velZ);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        if (!entity.isOnFire())
            entity.damage(this.getDamageSources().explosion(this, this.getOwner()), (float) getDamage());
        spill(entity.getBlockPos());
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        BlockPos pos = blockHitResult.getBlockPos();
        if (getWorld().getBlockState(pos).getBlock() instanceof AbstractCauldronBlock) {
            if(fillCauldron(pos)) return;
        }
        Direction side = blockHitResult.getSide();
        spill(pos.offset(side));
    }

    private void spill(BlockPos pos) {
        getWorld().setBlockState(pos, Fluids.WATER.getDefaultState().getBlockState(), 3);
        discard();
    }

    private boolean fillCauldron(BlockPos pos) {
        BlockState state = getWorld().getBlockState(pos);
        if (state.getBlock() instanceof LavaCauldronBlock) return false;
        if (((AbstractCauldronBlock)state.getBlock()).isFull(state)) return false;
        getWorld().setBlockState(pos, Blocks.WATER_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, LeveledCauldronBlock.MAX_LEVEL), 3);
        discard();
        return true;
    }

    @Override
    public void tick() {
        Vec3d vec3d = this.getVelocity();
        double nx = this.getX() + vec3d.x;
        double ny = this.getY() + vec3d.y;
        double nz = this.getZ() + vec3d.z;
        super.tick();
        float m = 0.999f;
        if (this.isTouchingWater()) {

            m = this.getDragInWater();
        }
        this.setVelocity(vec3d.multiply(m));
        if (!this.hasNoGravity() && !this.isNoClip()) {
            vec3d = this.getVelocity();
            this.setVelocity(vec3d.x, vec3d.y - (double) 0.001f, vec3d.z);
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
    }

    private void spawnParticles(int amount) {
        if (amount <= 0) {
            return;
        }
        for (int j = 0; j < amount; ++j) {
            this.getWorld().addParticle(ParticleTypes.FLAME, this.getParticleX(0.5), this.getRandomBodyY() + 0.5, this.getParticleZ(0.5), 0, 0, 0);
        }
    }
}
