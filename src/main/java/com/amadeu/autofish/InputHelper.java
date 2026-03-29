package com.amadeu.autofish;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class InputHelper {
    public static void useFishingRod(MinecraftClient client, Hand hand) {
        if (client == null || client.player == null || client.interactionManager == null) {
            return;
        }

        ActionResult result = client.interactionManager.interactItem(client.player, hand);

        if (result.isAccepted()) {
            client.player.swingHand(hand);
        }
    }
}
