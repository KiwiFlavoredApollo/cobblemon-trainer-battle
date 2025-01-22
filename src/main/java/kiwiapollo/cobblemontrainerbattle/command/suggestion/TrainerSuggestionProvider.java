package kiwiapollo.cobblemontrainerbattle.command.suggestion;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerStorage;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TrainerSuggestionProvider implements SuggestionProvider<ServerCommandSource> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        String input = getIncompleteInput(context);
        TrainerStorage.getInstance().keySet().stream().filter(s -> s.startsWith(input)).forEach(builder::suggest);
        return builder.buildFuture();
    }

    private String getIncompleteInput(CommandContext<ServerCommandSource> context) {
        String input = context.getInput();
        List<String> parts = Arrays.stream(input.split(" ")).toList();
        return parts.get(parts.size() - 1);
    }
}
