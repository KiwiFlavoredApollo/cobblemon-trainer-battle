package kiwiapollo.cobblemontrainerbattle.item.vsseeker;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.template.RandomTrainerFactory;
import net.minecraft.util.Identifier;

public class RedVsSeeker extends VsSeeker {
    public RedVsSeeker() {
        super("item.cobblemontrainerbattle.red_vs_seeker.trainers", new Factory());
    }

    private static class Factory implements SimpleFactory<Identifier> {
        @Override
        public Identifier create() {
            return new RandomTrainerFactory(template -> {
                boolean result = true;

                result &= !template.getTeam().isEmpty();
                result &= template.getIdentifier().getPath().matches("radicalred/.+");

                return result;
            }).create();
        }
    }
}
