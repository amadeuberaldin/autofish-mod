package com.amadeu.autofish;

public class StatsTracker {

    private static int fishCaught = 0;
    private static int treasureCaught = 0;
    private static int junkCaught = 0;

    private static long startTime = 0;
    private static boolean running = false;

    public static void start() {
        fishCaught = 0;
        treasureCaught = 0;
        junkCaught = 0;
        startTime = System.currentTimeMillis();
        running = true;
    }

    public static void stop() {
        running = false;
    }

    public static void onFishCaught(int amount) {
        fishCaught += amount;
    }

    public static void onTreasureCaught(int amount) {
        treasureCaught += amount;
    }

    public static void onJunkCaught(int amount) {
        junkCaught += amount;
    }

    public static int getFishCaught() {
        return fishCaught;
    }

    public static int getTreasureCaught() {
        return treasureCaught;
    }

    public static int getJunkCaught() {
        return junkCaught;
    }

    public static long getElapsedSeconds() {
        if (!running) return 0;
        return (System.currentTimeMillis() - startTime) / 1000;
    }

    private static double perMinute(int amount) {
        long seconds = getElapsedSeconds();
        if (seconds == 0) return 0.0;
        return (amount * 60.0) / seconds;
    }

    public static double getFishPerMinute() {
        return perMinute(fishCaught);
    }

    public static double getTreasurePerMinute() {
        return perMinute(treasureCaught);
    }

    public static double getJunkPerMinute() {
        return perMinute(junkCaught);
    }

    public static boolean isRunning() {
        return running;
    }
}