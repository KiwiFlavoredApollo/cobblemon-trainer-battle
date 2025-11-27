package kiwiapollo.cobblemontrainerbattle.item.vsseeker;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.template.RandomTrainerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class RedVsSeeker extends VsSeeker {
    public RedVsSeeker() {
        super(Text.translatable("trainer_group.cobblemontrainerbattle.radicalred"), new Factory());
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
