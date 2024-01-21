package robot.abilities.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

public class EntityEvents implements ServerLivingEntityEvents.AfterDeath {

    @Override
    public void afterDeath(LivingEntity entity, DamageSource damageSource) {
        if (!(entity instanceof PlayerEntity player)) return;
        IPlayerMixin cap = (IPlayerMixin) player;
        cap.put(DataKeys.MP, 0d);
        cap.sync(DataKeys.MP);
        //if(cap.get(DataKeys.MP) <= 0)
    }
}
