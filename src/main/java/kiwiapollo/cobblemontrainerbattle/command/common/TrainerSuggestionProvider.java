package kiwiapollo.cobblemontrainerbattle.command.common;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.preset.TrainerTemplateStorage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class TrainerSuggestionProvider implements SuggestionProvider<ServerCommandSource> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        String input = getIncompleteInput(context);

        TrainerTemplateStorage.getInstance().keySet().stream()
                .filter(identifier -> !Objects.equals(identifier.getNamespace(), CobblemonTrainerBattle.MOD_ID))
                .filter(identifier -> identifier.toString().startsWith(input))
                .map(Identifier::toString).forEach(builder::suggest);

        TrainerTemplateStorage.getInstance().keySet().stream()
                .filter(identifier -> Objects.equals(identifier.getNamespace(), CobblemonTrainerBattle.MOD_ID))
                .filter(identifier -> identifier.getPath().startsWith(input))
                .map(Identifier::toString).forEach(builder::suggest);

        return builder.buildFuture();
    }

    private String getIncompleteInput(CommandContext<ServerCommandSource> context) {
        String input = context.getInput();
        List<String> parts = Arrays.stream(input.split(" ")).toList();
        return parts.get(parts.size() - 1);
    }
}
