package kiwiapollo.cobblemontrainerbattle.command.predicate;

import net.minecraft.server.command.ServerCommandSource;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class PlayerCommandSourcePredicate implements Predicate<ServerCommandSource> {
    private static final int OP_LEVEL = 2;
    private final List<String> permissions;

    public PlayerCommandSourcePredicate(String... permissions) {
        this.permissions = Arrays.asList(permissions);
    }

    @Override
    public boolean test(ServerCommandSource source) {
        boolean isLuckPermsPermissionExist = new LuckPermsPredicate(permissions.toArray(String[]::new)).test(source);
        boolean isPermissionLevelExist = new PermissionLevelPredicate(OP_LEVEL).test(source);

        return isLuckPermsPermissionExist || isPermissionLevelExist;
    }
}
