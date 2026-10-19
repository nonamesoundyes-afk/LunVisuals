package dev.lunvisuals;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import java.util.*;

public final class ClickGuiScreen extends Screen {
    private ModuleRegistry.Category selected = ModuleRegistry.Category.VISUAL;
    private final List<ButtonWidget> moduleButtons = new ArrayList<>();
    private int panelX, panelY, panelW, panelH;

    public ClickGuiScreen() { super(Text.literal("LunVisuals")); }

    @Override protected void init() { rebuildButtons(); }

    private void rebuildButtons() {
        clearChildren(); moduleButtons.clear();
        panelW = Math.min(540, width - 24); panelH = Math.min(330, height - 24);
        panelX = (width - panelW) / 2; panelY = (height - panelH) / 2;
        int catX = panelX + 14, catY = panelY + 56;
        for (ModuleRegistry.Category cat : ModuleRegistry.Category.values()) {
            final ModuleRegistry.Category chosen = cat;
            addDrawableChild(ButtonWidget.builder(Text.literal(label(cat)), b -> { selected = chosen; rebuildButtons(); })
                    .dimensions(catX, catY, 122, 25).build());
            catY += 31;
        }
        int x = panelX + 152, y = panelY + 55, colW = Math.max(120, (panelW - 176) / 2);
        int i = 0;
        for (ModuleRegistry.Module m : LunVisualsClient.MODULES.in(selected)) {
            int bx = x + (i % 2) * (colW + 8), by = y + (i / 2) * 34;
            ButtonWidget button = ButtonWidget.builder(moduleLabel(m), b -> {
                m.enabled = !m.enabled; b.setMessage(moduleLabel(m)); ConfigStore.save(LunVisualsClient.MODULES);
            }).dimensions(bx, by, colW, 28).build();
            addDrawableChild(button); moduleButtons.add(button); i++;
        }
    }
    private static String label(ModuleRegistry.Category c) {
        return switch (c) { case VISUAL -> "Visual"; case HANDS -> "Hands & Animations"; case INTERFACE -> "Interface"; case UTILITY -> "Utilities"; };
    }
    private static Text moduleLabel(ModuleRegistry.Module m) { return Text.literal((m.enabled ? "●  " : "○  ") + m.name); }

    @Override public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx, mouseX, mouseY, delta);
        ctx.fill(0, 0, width, height, 0x99070A10);
        ctx.fill(panelX, panelY, panelX + panelW, panelY + panelH, 0xFF11141D);
        ctx.fill(panelX, panelY, panelX + panelW, panelY + 2, 0xFF9B8CFF);
        ctx.fill(panelX + 12, panelY + 42, panelX + panelW - 12, panelY + 43, 0xFF2A2F3D);
        ctx.drawText(textRenderer, "LUNVISUALS", panelX + 16, panelY + 15, 0xFFF3F3FF, false);
        ctx.drawText(textRenderer, "CLIENT SETTINGS  /  0.1.0", panelX + 128, panelY + 16, 0xFF8F96AA, false);
        ctx.drawText(textRenderer, label(selected).toUpperCase(Locale.ROOT), panelX + 152, panelY + 37, 0xFFC9C2FF, false);
        super.render(ctx, mouseX, mouseY, delta);
        ctx.drawText(textRenderer, "RIGHT SHIFT  •  CLOSE WITH ESC", panelX + 16, panelY + panelH - 18, 0xFF777F93, false);
    }
    @Override public boolean shouldPause() { return false; }
}
