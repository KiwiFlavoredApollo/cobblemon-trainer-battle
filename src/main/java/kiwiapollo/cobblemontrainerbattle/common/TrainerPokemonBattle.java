package kiwiapollo.cobblemontrainerbattle.common;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;

public class TrainerPokemonBattle extends PokemonBattle {
    private final Object session;

    public TrainerPokemonBattle(PokemonBattle pokemonBattle, Object session) {
        super(pokemonBattle.getFormat(), pokemonBattle.getSide1(), pokemonBattle.getSide2());
        this.session = session;
    }

    public Object getSession() {
        return session;
    }
}
