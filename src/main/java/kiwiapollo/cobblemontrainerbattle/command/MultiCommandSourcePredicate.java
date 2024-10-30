package kiwiapollo.cobblemontrainerbattle.command;

import net.minecraft.server.command.ServerCommandSource;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class MultiCommandSourcePredicate implements Predicate<ServerCommandSource> {
    private final List<String> permissions;

    public MultiCommandSourcePredicate(String... permissions) {
        this.permissions = Arrays.asList(permissions);
    }

    @Override
    public boolean test(ServerCommandSource source) {
        if (source.getEntity() == null) {
            return true;
        } else {
            return new PlayerCommandSourcePredicate(permissions.toArray(String[]::new)).test(source);
        }
    }
}
