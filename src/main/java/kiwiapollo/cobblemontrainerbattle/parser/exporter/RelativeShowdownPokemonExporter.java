package kiwiapollo.cobblemontrainerbattle.parser.exporter;

import net.minecraft.server.network.ServerPlayerEntity;

import java.io.IOException;

public class RelativeShowdownPokemonExporter implements ShowdownPokemonExporter {
    private final ShowdownPokemonExporter exporter;

    public RelativeShowdownPokemonExporter(ServerPlayerEntity player) {
        this.exporter = new BaseShowdownPokemonExporter(player, level -> 0);
    }

    @Override
    public void toJson() throws IOException {
        exporter.toJson();
    }
}
