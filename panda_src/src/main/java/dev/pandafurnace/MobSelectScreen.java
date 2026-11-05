package dev.pandafurnace;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class MobSelectScreen extends Screen {
    public MobSelectScreen() { super(Text.literal("Furnace Spawner")); }

    @Override
    protected void init() {
        int w = 190, h = 22, gap = 6;
        int x = (width - w) / 2;
        int y = height / 2 - 78;
        for (int i = 0; i < 6; i++) {
            final int index = i;
            String label = PandaFurnaceConfig.name(i) + (PandaFurnaceConfig.selected() == i ? "  ✓" : "");
            addDrawableChild(ButtonWidget.builder(Text.literal(label), b -> {
                PandaFurnaceConfig.select(index);
                close();
            }).dimensions(x, y + i * (h + gap), w, h).build());
        }
        addDrawableChild(ButtonWidget.builder(Text.literal("Закрыть"), b -> close())
            .dimensions(x, y + 6 * (h + gap), w, h).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta); // draws the background itself
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 2 - 100, 0xFFFFFF);
    }

    @Override public boolean shouldPause() { return false; }
}
