package kiwiapollo.cobblemontrainerbattle.common;

import net.minecraft.util.Identifier;

public class GlobalRandomTrainerFactory implements SimpleFactory<Identifier> {
    private final RandomTrainerFactory factory;

    public GlobalRandomTrainerFactory() {
        this.factory = new RandomTrainerFactory.Builder().addAllTrainers().build();
    }

    @Override
    public Identifier create() {
        return factory.create();
    }
}
