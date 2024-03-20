package robot.abilities.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import robot.abilities.network.ModMessages;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;
import robot.abilities.util.Utils;

public class KeyInputHandler {
    public static final String KEY_CATEGORY = "key.category.abilities";
    public static final KeyBinding KEY_USE = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.abilities.skill_use", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, KEY_CATEGORY));
    public static final KeyBinding KEY_CHANGE = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.abilities.skill_change", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_H, KEY_CATEGORY));
    public static final KeyBinding KEY_MANAGE = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.abilities.skill_manage", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, KEY_CATEGORY));
    public static final KeyBinding KEY_F = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.abilities.f", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_J, KEY_CATEGORY));
    public static int pressed = 0;
    public static boolean isBlack = false;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!KEY_USE.isUnbound()) {
                if (KEY_USE.isPressed()) {
                    pressed += 1;
                } else if (pressed > 0) {
                    ClientPlayNetworking.send(ModMessages.SKILL_USE, PacketByteBufs.create().writeString("").writeInt(pressed));
                    pressed = 0;
                }
            }
            if (!KEY_CHANGE.isUnbound() && KEY_CHANGE.wasPressed()) {
                ClientPlayNetworking.send(ModMessages.SKILL_CHANGE, PacketByteBufs.create().writeBoolean(Utils.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT)));
            }
            if (!KEY_MANAGE.isUnbound() && KEY_MANAGE.wasPressed()) {
                if (!((IPlayerMixin) client.player).get(DataKeys.MAGIC).isEmpty())
                    ClientPlayNetworking.send(ModMessages.SKILL_MANAGER, PacketByteBufs.create().writeBoolean(false));
            }
            if (!KEY_F.isUnbound() && KEY_F.wasPressed()) {
                isBlack = !isBlack;
            }
        });
    }

    public static void register() {
        registerKeyInputs();
    }
}
