package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.entities.TrainerEntityPreset;
import net.minecraft.item.Item;

public class TrainerTicket extends Item {
    private final TrainerEntityPreset preset;

    public TrainerTicket(Settings settings, TrainerEntityPreset preset) {
        super(settings);
        this.preset = preset;
    }

    public TrainerEntityPreset getTrainerEntityPreset() {
        return preset;
    }
}
