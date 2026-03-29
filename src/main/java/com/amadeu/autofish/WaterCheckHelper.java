package com.amadeu.autofish;

import net.minecraft.client.MinecraftClient;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class WaterCheckHelper {

    // Este método verifica se o ponto EXATO onde o jogador está mirando é água
    // Comunicação:
    // - usado por AutoFishController antes de lançar a linha
    // - não depende de outros arquivos do mod
    public static boolean hasDirectWaterTarget(MinecraftClient client) {
        if (client == null || client.player == null || client.world == null) {
            return false;
        }

        HitResult hitResult = client.player.raycast(8.0, 0.0F, false);

        if (hitResult.getType() != HitResult.Type.BLOCK) {
            return false;
        }

        BlockPos hitPos = ((BlockHitResult) hitResult).getBlockPos();

        FluidState fluidState = client.world.getFluidState(hitPos);

        return fluidState.isIn(FluidTags.WATER);
    }
}