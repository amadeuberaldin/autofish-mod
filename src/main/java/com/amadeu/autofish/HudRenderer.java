package com.amadeu.autofish;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class HudRenderer {

    public static void register() {
        HudElementRegistry.attachElementAfter(
                VanillaHudElements.CROSSHAIR,
                Identifier.of("autofish", "hud"),
                HudRenderer::render
        );
    }

    private static void render(DrawContext context, Object tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client == null || client.player == null) return;
        if (!AutoFishController.isEnabled()) return;
        if (!AutoFishConfig.get().showHud) return;

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        int padding = 10;
        int lineHeight = 12;

        String line1 = "AutoFish: ON";
        String line2 = "Peixes: " + StatsTracker.getFishCaught();
        String line3 = "Raros: " + StatsTracker.getTreasureCaught();
        String line4 = "Junk: " + StatsTracker.getJunkCaught();
        String line5 = "Tempo: " + StatsTracker.getElapsedSeconds() + "s";

        int maxWidth = Math.max(
                Math.max(client.textRenderer.getWidth(line1), client.textRenderer.getWidth(line2)),
                Math.max(
                        Math.max(client.textRenderer.getWidth(line3), client.textRenderer.getWidth(line4)),
                        client.textRenderer.getWidth(line5)
                )
        );

        int boxWidth = maxWidth + 10;
        int boxHeight = lineHeight * 5 + 8;

        int x = screenWidth - boxWidth - padding;
        int y = screenHeight - boxHeight - padding - 30;

        context.fill(x, y, x + boxWidth, y + boxHeight, 0xCC000000);

        int textX = x + 5;
        int textY = y + 4;

        context.drawText(client.textRenderer, Text.literal(line1), textX, textY, 0xFF00FF00, true);
        textY += lineHeight;

        context.drawText(client.textRenderer, Text.literal(line2), textX, textY, 0xFFFFFFFF, true);
        textY += lineHeight;

        context.drawText(client.textRenderer, Text.literal(line3), textX, textY, 0xFFFFFF55, true);
        textY += lineHeight;

        context.drawText(client.textRenderer, Text.literal(line4), textX, textY, 0xFFFFAA55, true);
        textY += lineHeight;

        context.drawText(client.textRenderer, Text.literal(line5), textX, textY, 0xFFFFFFFF, true);
    }
}