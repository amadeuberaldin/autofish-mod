package com.amadeu.autofish;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;

public class InputHelper {
    public static void useFishingRod(Minecraft client, InteractionHand hand) {
        if (client == null || client.player == null || client.gameMode == null) {
            return;
        }

        InteractionResult result = client.gameMode.useItem(client.player, hand);

        if (result.consumesAction()) {
            client.player.swing(hand);
        }
    }
}