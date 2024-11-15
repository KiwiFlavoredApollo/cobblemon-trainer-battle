package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import kiwiapollo.cobblemontrainerbattle.battle.session.Session;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class AllTrainerNotDefeatedPredicate<T extends Session> implements MessagePredicate<T> {
    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.all_trainer_not_defeated");
    }

    @Override
    public boolean test(T session) {
        return !session.isAnyTrainerDefeated();
    }
}
