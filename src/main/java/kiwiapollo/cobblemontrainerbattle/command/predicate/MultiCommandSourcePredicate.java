package kiwiapollo.cobblemontrainerbattle.command.predicate;

import net.minecraft.server.command.ServerCommandSource;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class MultiCommandSourcePredicate implements Predicate<ServerCommandSource> {
    private static final int OP_LEVEL = 2;
    private final List<String> permissions;

    public MultiCommandSourcePredicate(String... permissions) {
        this.permissions = Arrays.asList(permissions);
    }

    @Override
    public boolean test(ServerCommandSource source) {
        if (source.isExecutedByPlayer()) {
            return new PlayerCommandSourcePredicate(permissions.toArray(String[]::new)).test(source);
        } else {
            return new PermissionLevelPredicate(OP_LEVEL).test(source);
        }
    }
}
