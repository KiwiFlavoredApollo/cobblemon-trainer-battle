package kiwiapollo.cobblemontrainerbattle.command.common;

import net.minecraft.server.command.ServerCommandSource;

import java.util.function.Predicate;

public class PermissionLevelPredicate implements Predicate<ServerCommandSource> {
    private final int level;

    public PermissionLevelPredicate(int level) {
        this.level = level;
    }

    @Override
    public boolean test(ServerCommandSource source) {
        return source.hasPermissionLevel(level);
    }
}
