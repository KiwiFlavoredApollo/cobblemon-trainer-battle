package kiwiapollo.cobblemontrainerbattle.command.common;

import net.minecraft.server.command.ServerCommandSource;

import java.util.List;
import java.util.function.Predicate;

public class MultiCommandSourcePredicate implements Predicate<ServerCommandSource> {
    private static final int OP_LEVEL = 2;
    private final List<String> permissions;

    public MultiCommandSourcePredicate(String permission) {
        this(List.of(permission));
    }

    public MultiCommandSourcePredicate(List<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean test(ServerCommandSource source) {
        if (source.isExecutedByPlayer()) {
            return new PlayerCommandSourcePredicate(permissions).test(source);

        } else {
            return new PermissionLevelPredicate(OP_LEVEL).test(source);
        }
    }
}
