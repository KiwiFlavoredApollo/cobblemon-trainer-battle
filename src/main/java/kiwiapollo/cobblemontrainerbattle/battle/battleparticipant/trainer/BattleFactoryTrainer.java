package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor;
import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.DisposableBattlePokemonFactory;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.PlayerBackedTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.BattleAIFactory;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.BattleFormatFactory;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerProfile;
import kiwiapollo.cobblemontrainerbattle.exception.PokemonParseException;
import kiwiapollo.cobblemontrainerbattle.parser.pokemon.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.parser.pokemon.ShowdownPokemonParser;
import kiwiapollo.cobblemontrainerbattle.parser.profile.MiniGameProfileStorage;
import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerProfileStorage;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.MessagePredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.nio.file.Paths;
import java.util.*;

public class BattleFactoryTrainer implements TrainerBattleParticipant {
    private static final String BATTLE_FORMAT = "single";

    private final Identifier identifier;
    private final UUID uuid;
    private final ServerPlayerEntity player;
    private final String name;
    private final PartyStore party;
    private final BattleAI battleAI;

    public BattleFactoryTrainer(Identifier identifier, ServerPlayerEntity player, int level) {
        this.identifier = identifier;
        this.uuid = UUID.randomUUID();
        this.player = player;
        TrainerProfile profile = TrainerProfileStorage.getProfileRegistry().get(identifier);
        this.name = Text.translatable(Optional.ofNullable(profile.displayName).orElse(Paths.get(identifier.getPath()).getFileName().toString())).getString();
        this.party = showdownTeamToFlatLevelParty(profile.team, player, level);
        this.battleAI = new BattleAIFactory(BATTLE_FORMAT, MiniGameProfileStorage.getBattleFactoryProfile().battleAI).create();
    }

    private PartyStore showdownTeamToFlatLevelParty(List<ShowdownPokemon> pokemons, ServerPlayerEntity player, int level) {
        ShowdownPokemonParser parser = new ShowdownPokemonParser(player);

        List<ShowdownPokemon> randomParty = new ArrayList<>(pokemons);
        Collections.shuffle(randomParty);

        PartyStore party = new PartyStore(UUID.randomUUID());
        for (ShowdownPokemon showdownPokemon : randomParty.subList(0, 3)) {
            try {
                party.add(parser.toCobblemonPokemon(showdownPokemon));
            } catch (PokemonParseException ignored) {

            }
        }

        party.toGappyList().stream().filter(Objects::nonNull).forEach(Pokemon::heal);
        party.toGappyList().stream().filter(Objects::nonNull).forEach(pokemon -> pokemon.setLevel(level));

        return party;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public Identifier getIdentifier() {
        return identifier;
    }

    @Override
    public BattleFormat getBattleFormat() {
        return new BattleFormatFactory().create(BATTLE_FORMAT);
    }

    @Override
    public BattleAI getBattleAI() {
        return battleAI;
    }

    @Override
    public List<MessagePredicate<PlayerBattleParticipant>> getPredicates() {
        return List.of();
    }

    @Override
    public Optional<SoundEvent> getBattleTheme() {
        return Optional.empty();
    }

    @Override
    public AIBattleActor createBattleActor() {
        return new PlayerBackedTrainerBattleActor(
                getName(),
                getUuid(),
                getBattleTeam(),
                getBattleAI(),
                player
        );
    }

    @Override
    public void onVictory() {

    }

    @Override
    public void onDefeat() {

    }

    @Override
    public PartyStore getParty() {
        return party;
    }

    @Override
    public List<BattlePokemon> getBattleTeam() {
        return party.toGappyList().stream().filter(Objects::nonNull).map(DisposableBattlePokemonFactory::create).toList();
    }
}
