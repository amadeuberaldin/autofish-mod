package com.amadeu.autofish;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class AutoFishClient implements ClientModInitializer {
    private static final KeyBinding.Category AUTO_FISH_CATEGORY = KeyBinding.Category
            .create(Identifier.of("autofish", "main"));

    private static KeyBinding toggleKey;
    private static KeyBinding toggleHudKey;
    private static KeyBinding toggleMessagesKey;
    private static KeyBinding openMenuKey;

    @Override
    public void onInitializeClient() {
        AutoFishConfig.load();

        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.autofish.open_menu",
                GLFW.GLFW_KEY_O,
                AUTO_FISH_CATEGORY));

        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.autofish.toggle",
                GLFW.GLFW_KEY_R,
                AUTO_FISH_CATEGORY));

        toggleHudKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.autofish.toggle_hud",
                GLFW.GLFW_KEY_H,
                AUTO_FISH_CATEGORY));

        toggleMessagesKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.autofish.toggle_messages",
                GLFW.GLFW_KEY_M,
                AUTO_FISH_CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                AutoFishController.toggle(client);
            }

            while (openMenuKey.wasPressed()) {
                if (client.player != null) {
                    client.setScreen(new AutoFishConfigScreen(client.currentScreen));
                }
            }

            while (toggleHudKey.wasPressed()) {
                AutoFishConfig.get().showHud = !AutoFishConfig.get().showHud;
                AutoFishConfig.save();

                if (client.player != null) {
                    client.player.sendMessage(
                            Text.literal("AutoFish HUD: " + (AutoFishConfig.get().showHud ? "ON" : "OFF")),
                            true);
                }
            }

            while (toggleMessagesKey.wasPressed()) {
                AutoFishConfig.get().showActionbarMessages = !AutoFishConfig.get().showActionbarMessages;
                AutoFishConfig.save();

                if (client.player != null) {
                    client.player.sendMessage(
                            Text.literal("AutoFish mensagens: "
                                    + (AutoFishConfig.get().showActionbarMessages ? "ON" : "OFF")),
                            true);
                }
            }

            AutoFishController.tick(client);
        });

        HudRenderer.register();
    }
}