package dev.lunvisuals;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public final class LunVisualsClient implements ClientModInitializer {
    public static final ModuleRegistry MODULES = new ModuleRegistry();
    private static KeyBinding openMenu;
    private static KeyBinding zoomKey;
    private static double previousGamma = Double.NaN;
    private static int previousFov = -1;

    @Override
    public void onInitializeClient() {
        ConfigStore.load(MODULES);
        openMenu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.lunvisuals.open_menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT,
                KeyBinding.Category.MISC));
        zoomKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.lunvisuals.zoom", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C,
                KeyBinding.Category.MISC));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenu.wasPressed()) {
                if (client.currentScreen == null) client.setScreen(new ClickGuiScreen());
            }
            applyFullbright(client);
            applyZoom(client);
        });

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> renderHud(drawContext));
    }

    private static void applyFullbright(MinecraftClient client) {
        if (client.options == null) return;
        boolean enabled = MODULES.byName("Fullbright").map(m -> m.enabled).orElse(false);
        if (enabled) {
            if (Double.isNaN(previousGamma)) previousGamma = client.options.getGamma().getValue();
            client.options.getGamma().setValue(16.0);
        } else if (!Double.isNaN(previousGamma)) {
            client.options.getGamma().setValue(previousGamma);
            previousGamma = Double.NaN;
        }
    }

    private static void applyZoom(MinecraftClient client) {
        if (client.options == null) return;
        boolean enabled = MODULES.byName("Zoom").map(m -> m.enabled).orElse(false);
        boolean active = enabled && zoomKey.isPressed();
        if (active) {
            if (previousFov < 0) previousFov = client.options.getFov().getValue();
            client.options.getFov().setValue(Math.max(30, Math.min(previousFov, 30)));
        } else if (previousFov >= 0) {
            client.options.getFov().setValue(previousFov);
            previousFov = -1;
        }
    }

    private static void renderHud(DrawContext ctx) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.options.hudHidden) return;
        boolean enabled = MODULES.byName("HUD").map(m -> m.enabled).orElse(false);
        if (!enabled) return;
        int x = 8, y = 8, color = 0xFFE8E6FF;
        ctx.fill(x - 4, y - 4, x + 146, y + 43, 0x990B0E17);
        ctx.fill(x - 4, y - 4, x + 142, y - 2, 0xFF9B8CFF);
        ctx.drawText(client.textRenderer, Text.literal("LunVisuals"), x, y, 0xFFB9ADFF, false);
        y += 12;
        ctx.drawText(client.textRenderer, Text.literal(String.format(java.util.Locale.ROOT,
                "XYZ: %.1f / %.1f / %.1f", client.player.getX(), client.player.getY(), client.player.getZ())), x, y, color, false);
        y += 12;
        ctx.drawText(client.textRenderer, Text.literal("FPS: " + client.getCurrentFps()), x, y, color, false);
    }
}
