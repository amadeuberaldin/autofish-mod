package com.amadeu.autofish;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class AutoFishConfigScreen extends Screen {

    private final Screen parent;

    private ButtonWidget hudButton;
    private ButtonWidget messagesButton;
    private ButtonWidget minDelayButton;
    private ButtonWidget maxDelayButton;

    protected AutoFishConfigScreen(Screen parent) {
        super(Text.literal("AutoFish Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int y = this.height / 4;

        hudButton = this.addDrawableChild(ButtonWidget.builder(
                getHudText(),
                button -> {
                    AutoFishConfig.get().showHud = !AutoFishConfig.get().showHud;
                    refreshTexts();
                }
        ).dimensions(centerX - 100, y, 200, 20).build());

        y += 24;

        messagesButton = this.addDrawableChild(ButtonWidget.builder(
                getMessagesText(),
                button -> {
                    AutoFishConfig.get().showActionbarMessages = !AutoFishConfig.get().showActionbarMessages;
                    refreshTexts();
                }
        ).dimensions(centerX - 100, y, 200, 20).build());

        y += 30;

        // Delay mínimo
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("-"),
                button -> {
                    if (AutoFishConfig.get().recastDelayMin > 0) {
                        AutoFishConfig.get().recastDelayMin--;
                    }

                    if (AutoFishConfig.get().recastDelayMax < AutoFishConfig.get().recastDelayMin) {
                        AutoFishConfig.get().recastDelayMax = AutoFishConfig.get().recastDelayMin;
                    }

                    refreshTexts();
                }
        ).dimensions(centerX - 100, y, 20, 20).build());

        minDelayButton = this.addDrawableChild(ButtonWidget.builder(
                getMinDelayText(),
                button -> {
                }
        ).dimensions(centerX - 75, y, 150, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("+"),
                button -> {
                    AutoFishConfig.get().recastDelayMin++;

                    if (AutoFishConfig.get().recastDelayMax < AutoFishConfig.get().recastDelayMin) {
                        AutoFishConfig.get().recastDelayMax = AutoFishConfig.get().recastDelayMin;
                    }

                    refreshTexts();
                }
        ).dimensions(centerX + 80, y, 20, 20).build());

        y += 24;

        // Delay máximo
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("-"),
                button -> {
                    if (AutoFishConfig.get().recastDelayMax > AutoFishConfig.get().recastDelayMin) {
                        AutoFishConfig.get().recastDelayMax--;
                    }

                    refreshTexts();
                }
        ).dimensions(centerX - 100, y, 20, 20).build());

        maxDelayButton = this.addDrawableChild(ButtonWidget.builder(
                getMaxDelayText(),
                button -> {
                }
        ).dimensions(centerX - 75, y, 150, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("+"),
                button -> {
                    AutoFishConfig.get().recastDelayMax++;
                    refreshTexts();
                }
        ).dimensions(centerX + 80, y, 20, 20).build());

        y += 40;

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Salvar e voltar"),
                button -> {
                    AutoFishConfig.get().recastDelayMin = Math.max(0, AutoFishConfig.get().recastDelayMin);

                    if (AutoFishConfig.get().recastDelayMax < AutoFishConfig.get().recastDelayMin) {
                        AutoFishConfig.get().recastDelayMax = AutoFishConfig.get().recastDelayMin;
                    }

                    AutoFishConfig.save();

                    assert client != null;
                    client.setScreen(parent);
                }
        ).dimensions(centerX - 100, y, 200, 20).build());

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

    private Text getHudText() {
        return Text.literal("HUD: " + (AutoFishConfig.get().showHud ? "ON" : "OFF"));
    }

    private Text getMessagesText() {
        return Text.literal("Mensagens: " + (AutoFishConfig.get().showActionbarMessages ? "ON" : "OFF"));
    }

    private Text getMinDelayText() {
        return Text.literal("Delay mínimo: " + AutoFishConfig.get().recastDelayMin);
    }

    private Text getMaxDelayText() {
        return Text.literal("Delay máximo: " + AutoFishConfig.get().recastDelayMax);
    }

    @Override
    public void close() {
        AutoFishConfig.save();

        assert client != null;
        client.setScreen(parent);
    }
}