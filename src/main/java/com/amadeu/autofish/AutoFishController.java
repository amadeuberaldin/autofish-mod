package com.amadeu.autofish;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;

import java.util.Random;

public class AutoFishController {
    private static boolean enabled = false;

    private static int actionCooldownTicks = 0;
    private static int recastDelayTicks = 0;
    private static int noWaterMessageCooldownTicks = 0;

    private static final Random random = new Random();

    public static void toggle(Minecraft client) {
        enabled = !enabled;

        if (client.player != null && AutoFishConfig.get().showActionbarMessages) {
            client.player.sendSystemMessage(
                    Component.literal(enabled ? "AutoFish: ON" : "AutoFish: OFF")
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

    public static void tick(Minecraft client) {
        if (!enabled) return;

        if (client == null || client.player == null || client.level == null || client.gameMode == null) {
            return;
        }

        InventoryTracker.tick(client);

        if (actionCooldownTicks > 0) actionCooldownTicks--;
        if (recastDelayTicks > 0) recastDelayTicks--;
        if (noWaterMessageCooldownTicks > 0) noWaterMessageCooldownTicks--;

        InteractionHand rodHand = getFishingRodHand(client);
        if (rodHand == null) return;

        FishingHook bobber = client.player.fishing;

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
                    client.player.sendSystemMessage(Component.literal("AutoFish: sem água válida à frente"));
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

    private static InteractionHand getFishingRodHand(Minecraft client) {
        ItemStack mainHand = client.player.getMainHandItem();
        if (mainHand.getItem() instanceof FishingRodItem) return InteractionHand.MAIN_HAND;

        ItemStack offHand = client.player.getOffhandItem();
        if (offHand.getItem() instanceof FishingRodItem) return InteractionHand.OFF_HAND;

        return null;
    }
}