package com.amadeu.autofish;

import com.amadeu.autofish.mixin.FishingBobberEntityAccessor;
import net.minecraft.entity.projectile.FishingBobberEntity;

public class FishingStateTracker {
    private static boolean lastCaughtFish = false;
    private static int ignoreTicks = 0;

    public static boolean shouldReel(FishingBobberEntity bobber) {
        if (bobber == null) {
            reset();
            return false;
        }

        if (ignoreTicks > 0) {
            ignoreTicks--;
        }

        if (ignoreTicks > 0) {
            return false;
        }

        boolean caughtFish = ((FishingBobberEntityAccessor) bobber).autofish$isCaughtFish();

        if (caughtFish && !lastCaughtFish) {
            lastCaughtFish = true;
            ignoreTicks = 12;
            return true;
        }

        lastCaughtFish = caughtFish;
        return false;
    }

    public static void reset() {
        lastCaughtFish = false;
        ignoreTicks = 0;
    }
}