/*
 * Decompiled with CFR 0.2.1 (FabricMC 53fa44c9).
 */
package robot.abilities.entity.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;
import robot.abilities.entity.MithrilArrowEntity;

@Environment(value = EnvType.CLIENT)
public class MithrilArrowEntityRenderer
        extends ProjectileEntityRenderer<MithrilArrowEntity> {
    public static final Identifier MITHRIL_TEXTURE = new Identifier(AbilitiesMod.ID, "textures/entities/projectiles/mithril_arrow.png");

    public MithrilArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(MithrilArrowEntity arrowEntity) {
        return MITHRIL_TEXTURE;
    }
}

