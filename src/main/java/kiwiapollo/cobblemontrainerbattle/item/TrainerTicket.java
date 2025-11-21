package kiwiapollo.cobblemontrainerbattle.item;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class TrainerTicket extends Item {
    private final Identifier trainer;

    public TrainerTicket(Settings settings, Identifier trainer) {
        super(settings);
        this.trainer = trainer;
    }

    public Identifier getTrainer() {
        return trainer;
    }
}
