package kiwiapollo.cobblemontrainerbattle.item.vsseeker;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.template.RandomTrainerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BlueVsSeeker extends VsSeeker {
    public BlueVsSeeker() {
        super(Text.translatable("trainer_group.cobblemontrainerbattle.all"), new Factory());
    }

    private static class Factory implements SimpleFactory<Identifier> {
        @Override
        public Identifier create() {
            return new RandomTrainerFactory(template -> {
                boolean result = true;

                result &= !template.getTeam().isEmpty();

                return result;
            }).create();
        }
    }
}
