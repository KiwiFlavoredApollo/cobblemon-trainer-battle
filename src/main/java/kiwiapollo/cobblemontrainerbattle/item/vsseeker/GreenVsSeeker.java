package kiwiapollo.cobblemontrainerbattle.item.vsseeker;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.entity.RandomTrainerIdentifierFactory;
import net.minecraft.util.Identifier;

public class GreenVsSeeker extends VsSeeker {
    public GreenVsSeeker() {
        super("item.cobblemontrainerbattle.green_vs_seeker.trainers", new Factory());
    }

    private static class Factory implements SimpleFactory<Identifier> {
        @Override
        public Identifier create() {
            return new RandomTrainerIdentifierFactory(template -> template.getIdentifier().getPath().matches("inclementemerald/.+")).create();
        }
    }
}
