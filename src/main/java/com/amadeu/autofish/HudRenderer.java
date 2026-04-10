package com.amadeu.autofish;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class HudRenderer {

    public static void register() {
        HudElementRegistry.attachElementAfter(
                VanillaHudElements.CROSSHAIR,
                Identifier.fromNamespaceAndPath("autofish", "hud"),
                HudRenderer::extractRenderState
        );
    }

    private static void extractRenderState(net.minecraft.client.gui.GuiGraphicsExtractor context, net.minecraft.client.DeltaTracker tickCounter) {
        Minecraft client = Minecraft.getInstance();

        if (client == null || client.player == null) return;
        if (!AutoFishController.isEnabled()) return;
        if (!AutoFishConfig.get().showHud) return;

        int screenWidth = context.guiWidth();
        int screenHeight = context.guiHeight();

        int padding = 10;
        int lineHeight = 12;

        String line1 = "AutoFish: ON";
        String line2 = "Peixes: " + StatsTracker.getFishCaught();
        String line3 = "Raros: " + StatsTracker.getTreasureCaught();
        String line4 = "Junk: " + StatsTracker.getJunkCaught();
        String line5 = "Tempo: " + StatsTracker.getElapsedSeconds() + "s";

        int maxWidth = Math.max(
                Math.max(client.font.width(line1), client.font.width(line2)),
                Math.max(
                        Math.max(client.font.width(line3), client.font.width(line4)),
                        client.font.width(line5)
                )
        );

        int boxWidth = maxWidth + 10;
        int boxHeight = lineHeight * 5 + 8;

        int x = screenWidth - boxWidth - padding;
        int y = screenHeight - boxHeight - padding - 30;

        context.fill(x, y, x + boxWidth, y + boxHeight, 0xCC000000);

        int textX = x + 5;
        int textY = y + 4;

        context.text(client.font, Component.literal(line1), textX, textY, 0xFF00FF00, true);
        textY += lineHeight;

        context.text(client.font, Component.literal(line2), textX, textY, 0xFFFFFFFF, true);
        textY += lineHeight;

        context.text(client.font, Component.literal(line3), textX, textY, 0xFFFFFF55, true);
        textY += lineHeight;

        context.text(client.font, Component.literal(line4), textX, textY, 0xFFFFAA55, true);
        textY += lineHeight;

        context.text(client.font, Component.literal(line5), textX, textY, 0xFFFFFFFF, true);
    }
}