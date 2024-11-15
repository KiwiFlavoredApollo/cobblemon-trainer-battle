package kiwiapollo.cobblemontrainerbattle.predicates;

import kiwiapollo.cobblemontrainerbattle.session.Session;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class AllTrainerDefeatedPredicate<T extends Session> implements MessagePredicate<T> {
    @Override
    public MutableText getMessage() {
        return Text.translatable("command.cobblemontrainerbattle.groupbattle.startbattle.defeated_all_trainers");
    }

    @Override
    public boolean test(T session) {
        return session.isAllTrainerDefeated();
    }
}
