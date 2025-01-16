package kiwiapollo.cobblemontrainerbattle.exporter;

import net.minecraft.server.network.ServerPlayerEntity;

import java.io.IOException;

public class FlatShowdownPokemonExporter implements ShowdownPokemonExporter {
    private final ShowdownPokemonExporter exporter;

    public FlatShowdownPokemonExporter(ServerPlayerEntity player, int level) {
        this.exporter = new BaseShowdownPokemonExporter(player, l -> level);
    }

    @Override
    public void toJson() throws IOException {
        exporter.toJson();
    }
}
