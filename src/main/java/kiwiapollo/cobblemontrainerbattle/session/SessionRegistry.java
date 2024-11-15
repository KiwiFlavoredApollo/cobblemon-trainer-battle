package kiwiapollo.cobblemontrainerbattle.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionRegistry<T extends Session> {
    private final Map<UUID, T> sessions = new HashMap<>();

    public T get(UUID uuid) {
        return sessions.get(uuid);
    }

    public T put(UUID uuid, T session) {
        return sessions.put(uuid, session);
    }

    public T remove(UUID uuid) {
        return sessions.remove(uuid);
    }

    public boolean containsKey(UUID uuid) {
        return sessions.containsKey(uuid);
    }
}
