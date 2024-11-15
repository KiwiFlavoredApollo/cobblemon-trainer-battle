package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import kiwiapollo.cobblemontrainerbattle.battle.session.Session;
import kiwiapollo.cobblemontrainerbattle.battle.session.SessionRegistry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class SessionNotExistPredicate implements MessagePredicate<ServerPlayerEntity> {
    private final SessionRegistry<? extends Session> registry;

    public SessionNotExistPredicate(SessionRegistry<? extends Session> registry) {
        this.registry = registry;
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.session_not_exist");
    }

    @Override
    public boolean test(ServerPlayerEntity player) {
        return !registry.containsKey(player.getUuid());
    }
}
