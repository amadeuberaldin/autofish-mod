package com.amadeu.autofish;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.core.BlockPos;

public class WaterCheckHelper {

    // Este método verifica se o ponto EXATO onde o jogador está mirando é água
    // Comunicação:
    // - usado por AutoFishController antes de lançar a linha
    // - não depende de outros arquivos do mod
    public static boolean hasDirectWaterTarget(Minecraft client) {
        if (client == null || client.player == null || client.level == null) {
            return false;
        }

        HitResult hitResult = client.player.pick(8.0, 0.0F, true);

        if (hitResult.getType() != HitResult.Type.BLOCK) {
            return false;
        }

        BlockPos hitPos = ((BlockHitResult) hitResult).getBlockPos();

        FluidState fluidState = client.level.getFluidState(hitPos);

        return fluidState.is(FluidTags.WATER);
    }
}