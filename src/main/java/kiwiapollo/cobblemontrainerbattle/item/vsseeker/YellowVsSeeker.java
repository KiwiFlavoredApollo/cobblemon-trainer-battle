package kiwiapollo.cobblemontrainerbattle.item.vsseeker;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.TrainerSelector;
import kiwiapollo.cobblemontrainerbattle.template.RandomTrainerSelector;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class YellowVsSeeker extends VsSeeker {
    public YellowVsSeeker() {
        super(Text.translatable("trainer_group.cobblemontrainerbattle.bdsp"), new RandomTrainerSelector(template -> {
            boolean result = true;

            result &= !template.getTeam().isEmpty();
            result &= template.getIdentifier().getNamespace().equals(CobblemonTrainerBattle.MOD_ID);
            result &= template.getIdentifier().getPath().matches("bdsp/.+");

            return result;
        }));
    }
}
