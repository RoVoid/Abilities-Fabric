package robot.abilities;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import robot.abilities.block.ModBlockEntities;
import robot.abilities.block.ModBlocks;
import robot.abilities.client.screen.ModScreens;
import robot.abilities.entity.ModEntities;
import robot.abilities.event.KeyInputHandler;
import robot.abilities.event.ModEvents;
import robot.abilities.item.ModItems;
import robot.abilities.item.SkillBook;
import robot.abilities.network.ModMessages;
import robot.abilities.particle.ModParticles;

@Environment(EnvType.CLIENT)
public class AbilitiesModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
        ModBlocks.registerRender();
        ModBlockEntities.registerRender();
        ModEntities.registerRender();
        ModEvents.registerClient();
        ModMessages.registerS2CPackets();
        ModScreens.register();
        ModParticles.registerRender();
        ModelPredicateProviderRegistry.register(ModItems.SKILL_BOOK, new Identifier("rarity"), ((stack, world, entity, seed) -> SkillBook.getSkillRarity(stack) / 10f));
    }
}