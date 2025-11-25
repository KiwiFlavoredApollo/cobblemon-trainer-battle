package kiwiapollo.cobblemontrainerbattle.item.vsseeker;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.template.RandomTrainerFactory;
import net.minecraft.util.Identifier;

public class BlueVsSeeker extends VsSeeker {
    public BlueVsSeeker() {
        super("item.cobblemontrainerbattle.blue_vs_seeker.trainers", new Factory());
    }

    private static class Factory implements SimpleFactory<Identifier> {
        @Override
        public Identifier create() {
            return new RandomTrainerFactory(template -> true).create();
        }
    }
}
