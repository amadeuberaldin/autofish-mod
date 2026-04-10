package com.amadeu.autofish;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Set;

public class LootClassifier {

    private static final Set<Item> TREASURE_ITEMS = Set.of(
            Items.BOW,
            Items.ENCHANTED_BOOK,
            Items.FISHING_ROD,
            Items.NAME_TAG,
            Items.NAUTILUS_SHELL,
            Items.SADDLE
    );

    private static final Set<Item> JUNK_ITEMS = Set.of(
            Items.LILY_PAD,
            Items.LEATHER,
            Items.LEATHER_BOOTS,
            Items.ROTTEN_FLESH,
            Items.STICK,
            Items.STRING,
            Items.POTION,
            Items.BONE,
            Items.INK_SAC,
            Items.TRIPWIRE_HOOK,
            Items.BOWL
    );

    // Este método classifica o item pescado.
    // Comunicação:
    // - usado por InventoryTracker.java
    // - atualiza categorias no StatsTracker.java
    public static LootType classify(Item item) {
        if (TREASURE_ITEMS.contains(item)) {
            return LootType.TREASURE;
        }

        if (JUNK_ITEMS.contains(item)) {
            return LootType.JUNK;
        }

        return LootType.FISH;
    }

    public enum LootType {
        FISH,
        TREASURE,
        JUNK
    }
}