package kiwiapollo.cobblemontrainerbattle.command;

import net.minecraft.server.command.ServerCommandSource;

import java.util.List;
import java.util.function.Predicate;

public class PlayerCommandSourcePredicate implements Predicate<ServerCommandSource> {
    private static final int OP_LEVEL = 2;

    private final LuckPermsPredicate luckperms;
    private final PermissionLevelPredicate level;

    public PlayerCommandSourcePredicate(String permission) {
        this(List.of(permission));
    }

    public PlayerCommandSourcePredicate(List<String> permissions) {
        this.luckperms = new LuckPermsPredicate(permissions);
        this.level = new PermissionLevelPredicate(OP_LEVEL);
    }

    @Override
    public boolean test(ServerCommandSource source) {
        return luckperms.test(source) || level.test(source);
    }
}
