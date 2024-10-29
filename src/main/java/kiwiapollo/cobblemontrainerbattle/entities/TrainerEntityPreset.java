package kiwiapollo.cobblemontrainerbattle.entities;

import net.minecraft.util.Identifier;

public record TrainerEntityPreset(
        Identifier trainer,
        Identifier texture
) {}
