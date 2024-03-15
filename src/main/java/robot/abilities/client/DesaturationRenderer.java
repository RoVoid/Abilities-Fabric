package robot.abilities.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;

public class DesaturationRenderer {
    private static ShaderProgram desaturationShader;

    public static void desaturateWorld() {
        MinecraftClient client = MinecraftClient.getInstance();
        Framebuffer framebuffer = client.getFramebuffer();

        if (desaturationShader == null) {
          //  Shader
       //     desaturationShader = new ShaderProgram(client.getResourceManager(), "your_shader_folder/desaturation");
        }

        //desaturationShader.enable();

        // Set any additional shader uniforms here if needed

        MatrixStack matrixStack = new MatrixStack();
        framebuffer.beginWrite(false);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        framebuffer.endWrite();

     //   desaturationShader.disable();
    }

}