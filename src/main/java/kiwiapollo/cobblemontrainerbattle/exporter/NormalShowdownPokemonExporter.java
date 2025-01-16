package kiwiapollo.cobblemontrainerbattle.exporter;

import net.minecraft.server.network.ServerPlayerEntity;

import java.io.IOException;

public class NormalShowdownPokemonExporter implements ShowdownPokemonExporter {
    private final ShowdownPokemonExporter exporter;

    public NormalShowdownPokemonExporter(ServerPlayerEntity player) {
        this.exporter = new BaseShowdownPokemonExporter(player, level -> level);
    }

    @Override
    public void toJson() throws IOException {
        exporter.toJson();
    }
}
