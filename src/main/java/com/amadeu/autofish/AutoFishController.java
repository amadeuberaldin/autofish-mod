package com.amadeu.autofish;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

import java.util.Random;

public class AutoFishController {
    private static boolean enabled = false;

    private static int actionCooldownTicks = 0;
    private static int recastDelayTicks = 0;
    private static int noWaterMessageCooldownTicks = 0;

    private static final Random random = new Random();

    public static void toggle(MinecraftClient client) {
        enabled = !enabled;

        if (client.player != null && AutoFishConfig.get().showActionbarMessages) {
            client.player.sendMessage(
                    Text.literal(enabled ? "AutoFish: ON" : "AutoFish: OFF"),
                    true
            );
        }

        if (enabled) {
            StatsTracker.start();
            InventoryTracker.reset();
        } else {
            StatsTracker.stop();
            InventoryTracker.reset();
        }

        if (!enabled) {
            actionCooldownTicks = 0;
            recastDelayTicks = 0;
            noWaterMessageCooldownTicks = 0;
            FishingStateTracker.reset();
        }
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void tick(MinecraftClient client) {
        if (!enabled) return;

        if (client == null || client.player == null || client.world == null || client.interactionManager == null) {
            return;
        }

        InventoryTracker.tick(client);

        if (actionCooldownTicks > 0) actionCooldownTicks--;
        if (recastDelayTicks > 0) recastDelayTicks--;
        if (noWaterMessageCooldownTicks > 0) noWaterMessageCooldownTicks--;

        Hand rodHand = getFishingRodHand(client);
        if (rodHand == null) return;

        FishingBobberEntity bobber = client.player.fishHook;

        if (bobber != null) {
            if (FishingStateTracker.shouldReel(bobber)) {
                if (actionCooldownTicks <= 0) {
                    InventoryTracker.captureBeforeReel(client);
                    InputHelper.useFishingRod(client, rodHand);

                    actionCooldownTicks = 6;
                    recastDelayTicks = randomBetween(
                            AutoFishConfig.get().recastDelayMin,
                            AutoFishConfig.get().recastDelayMax
                    );

                    FishingStateTracker.reset();
                }
            }
            return;
        }

        if (recastDelayTicks <= 0 && actionCooldownTicks <= 0) {
            if (!WaterCheckHelper.hasDirectWaterTarget(client)) {
                if (AutoFishConfig.get().showActionbarMessages &&
                        noWaterMessageCooldownTicks <= 0 &&
                        client.player != null) {
                    client.player.sendMessage(Text.literal("AutoFish: sem água válida à frente"), true);
                    noWaterMessageCooldownTicks = 40;
                }
                return;
            }

            InputHelper.useFishingRod(client, rodHand);
            actionCooldownTicks = 8;
            FishingStateTracker.reset();
        }
    }

    private static int randomBetween(int min, int max) {
        if (max < min) {
            max = min;
        }
        return random.nextInt(max - min + 1) + min;
    }

    private static Hand getFishingRodHand(MinecraftClient client) {
        ItemStack mainHand = client.player.getMainHandStack();
        if (mainHand.getItem() instanceof FishingRodItem) return Hand.MAIN_HAND;

        ItemStack offHand = client.player.getOffHandStack();
        if (offHand.getItem() instanceof FishingRodItem) return Hand.OFF_HAND;

        return null;
    }
}