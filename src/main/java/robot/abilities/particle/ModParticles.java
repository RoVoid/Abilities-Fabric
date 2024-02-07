package robot.abilities.particle;

import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleRenderEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.mixin.renderer.client.SpriteAtlasTextureMixin;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.render.model.SpriteAtlasManager;
import net.minecraft.client.texture.SpriteAtlasHolder;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;

public class ModParticles {

    public static final DefaultParticleType RED_FLAME = particle("red_flame", false);

    public static DefaultParticleType particle(String name, boolean alwaysShow) {
        return Registry.register(Registries.PARTICLE_TYPE, new Identifier(AbilitiesMod.ID, name), FabricParticleTypes.simple(alwaysShow));
    }

    public static void register() {
    }

    public static void registerRender() {
        ParticleFactoryRegistry.getInstance().register(RED_FLAME, FlameParticle.Factory::new);
    }
}
