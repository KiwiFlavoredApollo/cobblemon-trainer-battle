package kiwiapollo.cobblemontrainerbattle.command.predicate;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.loader.api.FabricLoader;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedPermissionData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.util.Tristate;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.function.Predicate;

public class LuckPermsPredicate implements Predicate<ServerCommandSource> {
    private final List<String> permissions;

    public LuckPermsPredicate(List<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean test(ServerCommandSource source) {
        if (!FabricLoader.getInstance().isModLoaded("luckperms")) {
            return false;
        }

        if (!source.isExecutedByPlayer()) {
            return false;
        }

        ServerPlayerEntity player = source.getPlayer();
        User user = LuckPermsProvider.get().getUserManager().getUser(player.getUuid());
        CachedPermissionData userCachedPermissionData = user.getCachedData().getPermissionData();

        return permissions.stream().map(userCachedPermissionData::checkPermission).anyMatch(Tristate::asBoolean);
    }
}
