package com.amadeu.autofish;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class AutoFishClient implements ClientModInitializer {
    private static final KeyMapping.Category AUTO_FISH_CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("autofish", "main"));

    private static KeyMapping toggleKey;
    private static KeyMapping toggleHudKey;
    private static KeyMapping toggleMessagesKey;
    private static KeyMapping openMenuKey;

    @Override
    public void onInitializeClient() {
        AutoFishConfig.load();

        openMenuKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.autofish.open_menu",
                GLFW.GLFW_KEY_O,
                AUTO_FISH_CATEGORY));

        toggleKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.autofish.toggle",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                AUTO_FISH_CATEGORY));

        toggleHudKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.autofish.toggle_hud",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                AUTO_FISH_CATEGORY));

        toggleMessagesKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.autofish.toggle_messages",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                AUTO_FISH_CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.consumeClick()) {
                AutoFishController.toggle(client);
            }

            while (openMenuKey.consumeClick()) {
                if (client.player != null) {
                    client.setScreen(new AutoFishConfigScreen(client.screen));
                }
            }

            while (toggleHudKey.consumeClick()) {
                AutoFishConfig.get().showHud = !AutoFishConfig.get().showHud;
                AutoFishConfig.save();

                if (client.player != null) {
                    client.player.sendSystemMessage(
                            Component.literal("AutoFish HUD: " + (AutoFishConfig.get().showHud ? "ON" : "OFF")));
                }
            }

            while (toggleMessagesKey.consumeClick()) {
                AutoFishConfig.get().showActionbarMessages = !AutoFishConfig.get().showActionbarMessages;
                AutoFishConfig.save();

                if (client.player != null) {
                    client.player.sendSystemMessage(
                            Component.literal("AutoFish mensagens: "
                                    + (AutoFishConfig.get().showActionbarMessages ? "ON" : "OFF")));
                }
            }

            AutoFishController.tick(client);
        });

        HudRenderer.register();
    }
}