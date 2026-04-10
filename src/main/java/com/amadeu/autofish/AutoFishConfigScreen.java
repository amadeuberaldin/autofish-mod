package com.amadeu.autofish;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class AutoFishConfigScreen extends Screen {

    private final Screen parent;

    private Button hudButton;
    private Button messagesButton;
    private Button minDelayButton;
    private Button maxDelayButton;

    protected AutoFishConfigScreen(Screen parent) {
        super(Component.literal("AutoFish Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int y = this.height / 4;

        hudButton = this.addRenderableWidget(Button.builder(
                getHudText(),
                button -> {
                    AutoFishConfig.get().showHud = !AutoFishConfig.get().showHud;
                    refreshTexts();
                }
        ).bounds(centerX - 100, y, 200, 20).build());

        y += 24;

        messagesButton = this.addRenderableWidget(Button.builder(
                getMessagesText(),
                button -> {
                    AutoFishConfig.get().showActionbarMessages = !AutoFishConfig.get().showActionbarMessages;
                    refreshTexts();
                }
        ).bounds(centerX - 100, y, 200, 20).build());

        y += 30;

        // Delay mínimo
        this.addRenderableWidget(Button.builder(
                Component.literal("-"),
                button -> {
                    if (AutoFishConfig.get().recastDelayMin > 0) {
                        AutoFishConfig.get().recastDelayMin--;
                    }

                    if (AutoFishConfig.get().recastDelayMax < AutoFishConfig.get().recastDelayMin) {
                        AutoFishConfig.get().recastDelayMax = AutoFishConfig.get().recastDelayMin;
                    }

                    refreshTexts();
                }
        ).bounds(centerX - 100, y, 20, 20).build());

        minDelayButton = this.addRenderableWidget(Button.builder(
                getMinDelayText(),
                button -> {}
        ).bounds(centerX - 75, y, 150, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("+"),
                button -> {
                    AutoFishConfig.get().recastDelayMin++;

                    if (AutoFishConfig.get().recastDelayMax < AutoFishConfig.get().recastDelayMin) {
                        AutoFishConfig.get().recastDelayMax = AutoFishConfig.get().recastDelayMin;
                    }

                    refreshTexts();
                }
        ).bounds(centerX + 80, y, 20, 20).build());

        y += 24;

        // Delay máximo
        this.addRenderableWidget(Button.builder(
                Component.literal("-"),
                button -> {
                    if (AutoFishConfig.get().recastDelayMax > AutoFishConfig.get().recastDelayMin) {
                        AutoFishConfig.get().recastDelayMax--;
                    }

                    refreshTexts();
                }
        ).bounds(centerX - 100, y, 20, 20).build());

        maxDelayButton = this.addRenderableWidget(Button.builder(
                getMaxDelayText(),
                button -> {}
        ).bounds(centerX - 75, y, 150, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("+"),
                button -> {
                    AutoFishConfig.get().recastDelayMax++;
                    refreshTexts();
                }
        ).bounds(centerX + 80, y, 20, 20).build());

        y += 40;

        this.addRenderableWidget(Button.builder(
                Component.literal("Salvar e voltar"),
                button -> {
                    AutoFishConfig.get().recastDelayMin = Math.max(0, AutoFishConfig.get().recastDelayMin);

                    if (AutoFishConfig.get().recastDelayMax < AutoFishConfig.get().recastDelayMin) {
                        AutoFishConfig.get().recastDelayMax = AutoFishConfig.get().recastDelayMin;
                    }

                    AutoFishConfig.save();

                    if (this.minecraft != null) {
                        this.minecraft.setScreen(parent);
                    }
                }
        ).bounds(centerX - 100, y, 200, 20).build());

        refreshTexts();
    }

    private void refreshTexts() {
        if (hudButton != null) {
            hudButton.setMessage(getHudText());
        }

        if (messagesButton != null) {
            messagesButton.setMessage(getMessagesText());
        }

        if (minDelayButton != null) {
            minDelayButton.setMessage(getMinDelayText());
        }

        if (maxDelayButton != null) {
            maxDelayButton.setMessage(getMaxDelayText());
        }
    }

    private Component getHudText() {
        return Component.literal("HUD: " + (AutoFishConfig.get().showHud ? "ON" : "OFF"));
    }

    private Component getMessagesText() {
        return Component.literal("Mensagens: " + (AutoFishConfig.get().showActionbarMessages ? "ON" : "OFF"));
    }

    private Component getMinDelayText() {
        return Component.literal("Delay mínimo: " + AutoFishConfig.get().recastDelayMin);
    }

    private Component getMaxDelayText() {
        return Component.literal("Delay máximo: " + AutoFishConfig.get().recastDelayMax);
    }

    @Override
    public void onClose() {
        AutoFishConfig.save();

        if (this.minecraft != null) {
            this.minecraft.setScreen(parent);
        }
    }
}