package kiwiapollo.cobblemontrainerbattle.item.vsseeker;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.template.RandomTrainerSelector;
import net.minecraft.text.Text;

public class GreenVsSeeker extends VsSeeker {
    public GreenVsSeeker() {
        super(Text.translatable("trainer_group.cobblemontrainerbattle.inclementemerald"), new RandomTrainerSelector(template -> {
            boolean result = true;

            result &= template.isSpawnAllowed();
            result &= !template.getTeam().isEmpty();
            result &= template.getIdentifier().getNamespace().equals(CobblemonTrainerBattle.MOD_ID);
            result &= template.getIdentifier().getPath().matches("inclementemerald/.+");

            return result;
        }));
    }
}
