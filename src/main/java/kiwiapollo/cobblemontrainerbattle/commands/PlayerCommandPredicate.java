package kiwiapollo.cobblemontrainerbattle.commands;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedPermissionData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.util.Tristate;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class PlayerCommandPredicate implements Predicate<ServerCommandSource> {
    private static final int OP_LEVEL = 2;
    private final List<String> permissions;

    public PlayerCommandPredicate(String... permissions) {
        this.permissions = Arrays.asList(permissions);
    }

    @Override
    public boolean test(ServerCommandSource source) {
        try {
            assertLoadedLuckPerms();
            return isExistLuckPermsPermission(source);

        } catch (AssertionError e) {
            return isExistOpPermission(source);
        }
    }

    private void assertLoadedLuckPerms() throws AssertionError {
        try {
            Class.forName("net.luckperms.api.LuckPerms");
        } catch (ClassNotFoundException e) {
            throw new AssertionError();
        }
    }

    protected boolean isExistLuckPermsPermission(ServerCommandSource source) {
        ServerPlayerEntity player = source.getPlayer();
        User user = LuckPermsProvider.get().getUserManager().getUser(player.getUuid());
        CachedPermissionData userCachedPermissionData = user.getCachedData().getPermissionData();

        return permissions.stream().map(userCachedPermissionData::checkPermission).anyMatch(Tristate::asBoolean);
    }

    private boolean isExistOpPermission(ServerCommandSource source) {
        ServerPlayerEntity player = source.getPlayer();
        return player.hasPermissionLevel(OP_LEVEL);
    }
}
