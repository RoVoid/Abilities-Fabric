package robot.abilities.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import robot.abilities.network.ModMessages;

public class KetInputHandler {
    public static final String KEY_CATEGORY = "key.category.abilities";
    public static final String KEY_EX = "key.abilities.ex";
    public static KeyBinding exKey = new KeyBinding(KEY_EX, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, KEY_CATEGORY);

    public static void registerKeyInputs() {
//        ClientTickEvents.END_CLIENT_TICK.register(client -> {
//            if (exKey.wasPressed()) {
//                ClientPlayNetworking.send(ModMessages.WALK_SPEED_SYNC, PacketByteBufs.create());
//            }
//        });
    }

    public static void register() {
        KeyBindingHelper.registerKeyBinding(exKey);
        registerKeyInputs();
    }
}
