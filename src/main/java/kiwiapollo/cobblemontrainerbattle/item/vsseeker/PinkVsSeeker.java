package kiwiapollo.cobblemontrainerbattle.item.vsseeker;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.template.RandomTrainerFactory;
import net.minecraft.util.Identifier;

public class PinkVsSeeker extends VsSeeker {
    public PinkVsSeeker() {
        super("item.cobblemontrainerbattle.pink_vs_seeker.trainers", new Factory());
    }

    private static class Factory implements SimpleFactory<Identifier> {
        @Override
        public Identifier create() {
            return new RandomTrainerFactory(template -> template.getIdentifier().getPath().matches("xy/.+")).create();
        }
    }
}
