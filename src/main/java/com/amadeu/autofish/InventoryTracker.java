package com.amadeu.autofish;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;

public class InventoryTracker {

    private static final Map<Item, Integer> snapshot = new HashMap<>();
    private static int pendingCheckTicks = -1;

    // Salva o inventário antes da puxada.
    // Comunicação:
    // - chamado por AutoFishController.java antes de recolher
    public static void captureBeforeReel(Minecraft client) {
        snapshot.clear();

        if (client == null || client.player == null) {
            return;
        }

        Inventory inv = client.player.getInventory();

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                snapshot.merge(stack.getItem(), stack.getCount(), Integer::sum);
            }
        }

        // aumentamos o tempo para dar chance do item realmente entrar no inventário
        pendingCheckTicks = 20;
    }

    // Tick do rastreador do inventário.
    // Comunicação:
    // - chamado por AutoFishController.java a cada tick
    // - atualiza StatsTracker.java
    public static void tick(Minecraft client) {
        if (pendingCheckTicks < 0) {
            return;
        }

        pendingCheckTicks--;

        if (pendingCheckTicks > 0) {
            return;
        }

        pendingCheckTicks = -1;

        if (client == null || client.player == null) {
            snapshot.clear();
            return;
        }

        Inventory inv = client.player.getInventory();

        Map<Item, Integer> current = new HashMap<>();

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                current.merge(stack.getItem(), stack.getCount(), Integer::sum);
            }
        }

        int totalFish = 0;
        int totalTreasure = 0;
        int totalJunk = 0;

        for (Map.Entry<Item, Integer> entry : current.entrySet()) {
            Item item = entry.getKey();
            int before = snapshot.getOrDefault(item, 0);
            int after = entry.getValue();

            if (after > before) {
                int gained = after - before;

                LootClassifier.LootType type = LootClassifier.classify(item);

                switch (type) {
                    case TREASURE -> {
                        StatsTracker.onTreasureCaught(gained);
                        totalTreasure += gained;
                    }
                    case JUNK -> {
                        StatsTracker.onJunkCaught(gained);
                        totalJunk += gained;
                    }
                    case FISH -> {
                        StatsTracker.onFishCaught(gained);
                        totalFish += gained;
                    }
                }
            }
        }

        if (AutoFishConfig.get().showActionbarMessages &&
                client.player != null &&
                (totalFish > 0 || totalTreasure > 0 || totalJunk > 0)) {
            client.player.sendOverlayMessage(
                    Component.literal("AutoFish loot -> peixe: " + totalFish +
                            " raro: " + totalTreasure +
                            " junk: " + totalJunk));
        }

        snapshot.clear();
    }

    public static void reset() {
        snapshot.clear();
        pendingCheckTicks = -1;
    }
}