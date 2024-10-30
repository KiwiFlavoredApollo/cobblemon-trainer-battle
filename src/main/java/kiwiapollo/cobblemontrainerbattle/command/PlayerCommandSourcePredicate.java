package kiwiapollo.cobblemontrainerbattle.command;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedPermissionData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.util.Tristate;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

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
        try {
            assertPlayerCommandSource(source);
            assertLoadedLuckPerms();
            return isExistLuckPermsPermission(source);

        } catch (IllegalStateException e) {
            return false;

        } catch (ClassNotFoundException e) {
            return isExistOpPermission(source);
        }
    }

    private void assertPlayerCommandSource(ServerCommandSource source) throws IllegalStateException {
        if (source.getPlayer() == null) {
            throw new IllegalStateException();
        }
    }

    private void assertLoadedLuckPerms() throws ClassNotFoundException {
        Class.forName("net.luckperms.api.LuckPerms");
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
