package com.amadeu.autofish;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AutoFishConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = Path.of("config", "autofish.json");

    public boolean showHud = true;
    public boolean showActionbarMessages = true;
    public int recastDelayMin = 8;
    public int recastDelayMax = 18;

    private static AutoFishConfig instance;

    // Carrega o arquivo de configuração do disco.
    // Comunicação:
    // - chamado por AutoFishClient.java ao iniciar o mod
    // - usado por HudRenderer.java e AutoFishController.java
    public static void load() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());

            if (Files.exists(CONFIG_PATH)) {
                String json = Files.readString(CONFIG_PATH);
                instance = GSON.fromJson(json, AutoFishConfig.class);
            } else {
                instance = new AutoFishConfig();
                save();
            }

            if (instance == null) {
                instance = new AutoFishConfig();
                save();
            }

            instance.validate();
        } catch (IOException e) {
            instance = new AutoFishConfig();
        }
    }

    // Salva a configuração atual em disco.
    // Comunicação:
    // - chamado por AutoFishClient.java ao alterar opções
    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(get()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static AutoFishConfig get() {
        if (instance == null) {
            instance = new AutoFishConfig();
        }
        return instance;
    }

    private void validate() {
        if (recastDelayMin < 0) recastDelayMin = 0;
        if (recastDelayMax < recastDelayMin) recastDelayMax = recastDelayMin;
    }
}