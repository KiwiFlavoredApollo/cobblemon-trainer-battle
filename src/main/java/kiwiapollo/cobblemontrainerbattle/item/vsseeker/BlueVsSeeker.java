package kiwiapollo.cobblemontrainerbattle.item.vsseeker;

import kiwiapollo.cobblemontrainerbattle.common.TrainerSelector;
import kiwiapollo.cobblemontrainerbattle.template.RandomTrainerSelector;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BlueVsSeeker extends VsSeeker {
    public BlueVsSeeker() {
        super(Text.translatable("trainer_group.cobblemontrainerbattle.all"), new RandomTrainerSelector(template -> {
            boolean result = true;

            result &= template.isSpawnAllowed();
            result &= !template.getTeam().isEmpty();

            return result;
        }));
    }
}
