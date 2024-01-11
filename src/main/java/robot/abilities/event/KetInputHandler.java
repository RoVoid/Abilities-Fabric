package robot.abilities.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import robot.abilities.AbilitiesMod;
import robot.abilities.client.screen.ModScreens;
import robot.abilities.network.ModMessages;

public class KetInputHandler {
    public static final String KEY_CATEGORY = "key.category.abilities";
    public static final KeyBinding KEY_USE = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.abilities.skill_use", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, KEY_CATEGORY));
    public static final KeyBinding KEY_MANAGE = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.abilities.skill_manage", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, KEY_CATEGORY));

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (KEY_USE.wasPressed() && !KEY_USE.isUnbound()) {
                ClientPlayNetworking.send(ModMessages.SKILL_USE, PacketByteBufs.create());
            }
            if (KEY_MANAGE.wasPressed() && !KEY_MANAGE.isUnbound()) {
                ClientPlayNetworking.send(ModMessages.SKILL_MANAGER, PacketByteBufs.create());
            }
        });
    }

    public static void register() {
        registerKeyInputs();
    }
}
