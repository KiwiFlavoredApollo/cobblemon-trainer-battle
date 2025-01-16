package kiwiapollo.cobblemontrainerbattle.command.predicate;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedPermissionData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.util.Tristate;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class LuckPermsPredicate implements Predicate<ServerCommandSource> {
    private final List<String> permissions;

    public LuckPermsPredicate(String... permissions) {
        this.permissions = Arrays.asList(permissions);
    }

    @Override
    public boolean test(ServerCommandSource source) {
        try {
            assertLuckPermsLoaded();

            ServerPlayerEntity player = source.getPlayerOrThrow();
            User user = LuckPermsProvider.get().getUserManager().getUser(player.getUuid());
            CachedPermissionData userCachedPermissionData = user.getCachedData().getPermissionData();

            return permissions.stream().map(userCachedPermissionData::checkPermission).anyMatch(Tristate::asBoolean);

        } catch (ClassNotFoundException | CommandSyntaxException e) {
            return false;
        }
    }

    private void assertLuckPermsLoaded() throws ClassNotFoundException {
        Class.forName("net.luckperms.api.LuckPerms");
    }
}
