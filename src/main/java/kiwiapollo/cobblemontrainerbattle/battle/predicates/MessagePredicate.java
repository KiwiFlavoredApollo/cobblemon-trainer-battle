package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import net.minecraft.text.MutableText;

import java.util.function.Predicate;

public interface MessagePredicate<T> extends Predicate<T> {
    MutableText getErrorMessage();
}
