package kiwiapollo.cobblemontrainerbattle.predicates;

import kiwiapollo.cobblemontrainerbattle.session.Session;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class AnyTrainerDefeatedPredicate implements MessagePredicate<Session> {
    @Override
    public MutableText getMessage() {
        return Text.translatable("command.cobblemontrainerbattle.battlefactory.rerollpokemon.defeated_trainer_exist");
    }

    @Override
    public boolean test(Session session) {
        return session.isDefeatedAnyTrainers();
    }
}
