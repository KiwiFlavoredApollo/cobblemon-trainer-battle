package kiwiapollo.cobblemontrainerbattle.item;

import net.minecraft.item.Item;

public class TrainerTicket extends Item {
    private final String trainer;

    public TrainerTicket(Settings settings, String trainer) {
        super(settings);
        this.trainer = trainer;
    }

    public String getTrainer() {
        return trainer;
    }
}
