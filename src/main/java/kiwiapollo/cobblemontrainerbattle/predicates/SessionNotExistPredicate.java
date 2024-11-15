package kiwiapollo.cobblemontrainerbattle.predicates;

import kiwiapollo.cobblemontrainerbattle.session.Session;
import kiwiapollo.cobblemontrainerbattle.session.SessionStorage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class SessionNotExistPredicate implements MessagePredicate<ServerPlayerEntity> {
    private final SessionStorage<? extends Session> storage;

    public SessionNotExistPredicate(SessionStorage<? extends Session> storage) {
        this.storage = storage;
    }

    @Override
    public MutableText getMessage() {
        return Text.translatable("command.cobblemontrainerbattle.groupbattle.common.valid_session_not_exist");
    }

    @Override
    public boolean test(ServerPlayerEntity player) {
        return !storage.containsKey(player.getUuid());
    }
}
