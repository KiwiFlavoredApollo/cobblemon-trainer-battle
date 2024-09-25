package kiwiapollo.trainerbattle.commands;

import kiwiapollo.trainerbattle.exceptions.LuckPermsNotLoadedException;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class TrainerCommandPredicate implements Predicate<ServerCommandSource> {
    private static final int OP_LEVEL = 2;
    private final List<String> permissions;

    public TrainerCommandPredicate(String... permissions) {
        this.permissions = Arrays.asList(permissions);
    }

    @Override
    public boolean test(ServerCommandSource source) {
        try {
            assertLoadedLuckPerms();

            return isExistLuckPermsPermission(source);

        } catch (LuckPermsNotLoadedException e) {
            return isExistOpPermission(source);
        }
    }

    private void assertLoadedLuckPerms() throws LuckPermsNotLoadedException {
        try {
            Class.forName("net.luckperms.api.LuckPerms");
        } catch (ClassNotFoundException e) {
            throw new LuckPermsNotLoadedException();
        }
    }

    protected boolean isExistLuckPermsPermission(ServerCommandSource source) {
        ServerPlayerEntity player = source.getPlayer();
        User user = LuckPermsProvider.get().getUserManager().getUser(player.getUuid());

        return permissions.stream().anyMatch(permission ->
                user.getCachedData().getPermissionData().checkPermission(permission).asBoolean()
        );
    }

    private boolean isExistOpPermission(ServerCommandSource source) {
        ServerPlayerEntity player = source.getPlayer();
        return player.hasPermissionLevel(OP_LEVEL);
    }
}
