package kiwiapollo.cobblemontrainerbattle.battlefactory;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveSet;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.PokemonStats;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleparticipants.*;
import kiwiapollo.cobblemontrainerbattle.exceptions.*;
import kiwiapollo.cobblemontrainerbattle.resulthandler.DummyResultHandler;
import kiwiapollo.cobblemontrainerbattle.resulthandler.ResultHandler;
import kiwiapollo.cobblemontrainerbattle.sessions.PokemonShowFeature;
import kiwiapollo.cobblemontrainerbattle.sessions.PokemonTradeFeature;
import kiwiapollo.cobblemontrainerbattle.sessions.RentalPokemonFeature;
import kiwiapollo.cobblemontrainerbattle.sessions.Session;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Objects;

public class BattleFactorySession implements Session, PokemonTradeFeature, PokemonShowFeature, RentalPokemonFeature {
    private final PlayerBattleParticipant player;
    private final List<Identifier> trainersToDefeat;
    private final ResultHandler resultHandler;

    private TrainerBattle lastTrainerBattle;
    private TrainerBattleParticipant lastDefeatedTrainer;
    private int defeatedTrainersCount;
    private boolean isPlayerDefeated;
    private boolean isTradedPokemon;

    public BattleFactorySession(
            ServerPlayerEntity player,
            List<Identifier> trainersToDefeat,
            ResultHandler resultHandler
    ) {
        this.player = new BattleFactoryPlayer(player);
        this.trainersToDefeat = trainersToDefeat;
        this.defeatedTrainersCount = 0;
        this.isPlayerDefeated = false;
        this.isTradedPokemon = false;
        this.resultHandler = resultHandler;
    }

    @Override
    public void startBattle() throws BattleStartException {
        try {
            assertNotPlayerDefeated();
            assertNotDefeatedAllTrainers();

            TrainerBattleParticipant trainer = new BattleFactoryTrainer(
                    CobblemonTrainerBattle.trainers.get(trainersToDefeat.get(defeatedTrainersCount)),
                    player.getPlayerEntity(),
                    BattleFactory.LEVEL
            );
            ResultHandler resultHandler = new DummyResultHandler();
            TrainerBattle trainerBattle = new VirtualTrainerBattle(player, trainer, resultHandler);
            trainerBattle.start();

            CobblemonTrainerBattle.trainerBattles.put(player.getUuid(), trainerBattle);

            this.lastTrainerBattle = trainerBattle;
            this.lastDefeatedTrainer = null;

        } catch (DefeatedToTrainerException e) {
            player.sendErrorMessage(Text.translatable("command.cobblemontrainerbattle.battlefactory.tradepokemon.defeated_to_trainer"));
            CobblemonTrainerBattle.LOGGER.error("Player is defeated: {}", player.getName());

        } catch (DefeatedAllTrainersException e) {
            player.sendErrorMessage(Text.translatable("command.cobblemontrainerbattle.battlefactory.startbattle.defeated_all_trainers"));
            CobblemonTrainerBattle.LOGGER.error("Player has defeated all trainers: {}", player.getName());
            throw new BattleStartException();
        }
    }

    @Override
    public void tradePokemon(int playerSlot, int trainerSlot) {
        try {
            assertNotPlayerDefeated();
            assertExistDefeatedTrainer();
            assertNotPlayerTradedPokemon();

            Pokemon trainerPokemon = lastDefeatedTrainer.getParty().get(trainerSlot - 1);
            Pokemon playerPokemon = player.getParty().get(playerSlot - 1);

            lastDefeatedTrainer.getParty().set(trainerSlot, playerPokemon);
            player.getParty().set(playerSlot, trainerPokemon);

            isTradedPokemon = true;

            player.sendInfoMessage(Text.translatable("command.cobblemontrainerbattle.battlefactory.tradepokemon.success", playerPokemon.getDisplayName(), trainerPokemon.getDisplayName()));
            CobblemonTrainerBattle.LOGGER.info("{} traded {} for {}", player.getName(), playerPokemon.getDisplayName(), trainerPokemon.getDisplayName());

        } catch (DefeatedToTrainerException e) {
            player.sendErrorMessage(Text.translatable("command.cobblemontrainerbattle.battlefactory.tradepokemon.defeated_to_trainer"));
            CobblemonTrainerBattle.LOGGER.error("Player is defeated: {}", player.getName());

        } catch (NotExistDefeatedTrainerException e) {
            player.sendErrorMessage(Text.translatable("command.cobblemontrainerbattle.battlefactory.tradepokemon.defeated_trainer_not_exist"));
            CobblemonTrainerBattle.LOGGER.error("Player has no defeated trainers: {}", player.getName());

        } catch (TradedPokemonException e) {
            player.sendErrorMessage(Text.translatable("command.cobblemontrainerbattle.battlefactory.tradepokemon.already_traded_pokemon"));
            CobblemonTrainerBattle.LOGGER.error("Player has already traded a Pokemon: {}", player.getName());
        }
    }

    @Override
    public void showTradeablePokemon() {
        try {
            assertExistDefeatedTrainer();
            assertNotPlayerTradedPokemon();

            printPokemons(player, lastDefeatedTrainer.getParty());

        } catch (NotExistDefeatedTrainerException e) {
            player.sendErrorMessage(Text.translatable("command.cobblemontrainerbattle.battlefactory.tradepokemon.defeated_trainer_not_exist"));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player has no defeated trainers: %s", player.getName()));

        } catch (TradedPokemonException e) {
            player.sendErrorMessage(Text.translatable("command.cobblemontrainerbattle.battlefactory.tradepokemon.already_traded_pokemon"));
            CobblemonTrainerBattle.LOGGER.error("Player has already traded a Pokemon: {}", player.getName());
        }
    }

    @Override
    public void showPartyPokemon() {
        printPokemons(player, player.getParty());
    }

    @Override
    public void rerollPokemon() {
        try {
            assertNotExistDefeatedTrainer();
            player.setParty(RandomPartyFactory.create(player.getPlayerEntity()));
            printPokemons(player, player.getParty());

            player.sendInfoMessage(Text.translatable("command.cobblemontrainerbattle.battlefactory.rerollpokemon.success"));

        } catch (ExistDefeatedTrainerException e) {
            player.sendErrorMessage(Text.translatable("command.cobblemontrainerbattle.battlefactory.rerollpokemon.defeated_trainer_exist"));
            CobblemonTrainerBattle.LOGGER.error("Player has defeated trainers: {}", player.getName());
        }
    }

    private static void printPokemons(PlayerBattleParticipant player, PartyStore party) {
        List<Pokemon> pokemons = party.toGappyList().stream().filter(Objects::nonNull).toList();
        for (int i = 0; i < pokemons.size(); i++) {
            Pokemon pokemon = pokemons.get(i);

            player.sendInfoMessage(Text.literal("[" + (i + 1) + "] ").append(pokemon.getDisplayName()));
            player.sendInfoMessage(Text.literal("Ability ").append(Text.translatable(pokemon.getAbility().getDisplayName())));
            player.sendInfoMessage(Text.literal("Nature ").append(Text.translatable(pokemon.getNature().getDisplayName())));
            player.sendInfoMessage(Text.literal("MoveSet ").append(getPokemonMoveSetReport(pokemon.getMoveSet())));
            player.sendInfoMessage(Text.literal("EVs ").append(Text.literal(getPokemonStatsReport(pokemon.getEvs()))));
            player.sendInfoMessage(Text.literal("IVs ").append(Text.literal(getPokemonStatsReport(pokemon.getIvs()))));
        }
    }

    private static Text getPokemonMoveSetReport(MoveSet moveSet) {
        MutableText moveSetReport = Text.literal("");
        for (Move move : moveSet.getMoves()) {
            if (moveSetReport.equals(Text.literal(""))) {
                moveSetReport.append(move.getDisplayName());
            } else {
                moveSetReport.append(Text.literal(" / ")).append(move.getDisplayName());
            }
        }
        return moveSetReport;
    }

    private static String getPokemonStatsReport(PokemonStats stats) {
        return String.format("HP %d / ATK %d / DEF %d / SPA %d / SPD %d / SPE %d",
                stats.get(Stats.HP), stats.get(Stats.ATTACK), stats.get(Stats.DEFENCE),
                stats.get(Stats.SPECIAL_ATTACK), stats.get(Stats.SPECIAL_DEFENCE), stats.get(Stats.SPEED));
    }

    @Override
    public void onBattleVictory() {
        lastDefeatedTrainer = lastTrainerBattle.getTrainer();
        defeatedTrainersCount += 1;
        isTradedPokemon = false;
    }

    @Override
    public void onBattleDefeat() {
        lastDefeatedTrainer = null;
        isPlayerDefeated = true;
    }

    @Override
    public void onSessionStop() {
        if (isDefeatedAllTrainers()) {
            resultHandler.onVictory();
        } else {
            resultHandler.onDefeat();
        }
    }

    private void assertNotPlayerDefeated() throws DefeatedToTrainerException {
        if (isPlayerDefeated) {
            throw new DefeatedToTrainerException();
        }
    }

    private void assertNotDefeatedAllTrainers() throws DefeatedAllTrainersException {
        if (isDefeatedAllTrainers()) {
            throw new DefeatedAllTrainersException();
        }
    }

    private boolean isDefeatedAllTrainers() {
        return defeatedTrainersCount == trainersToDefeat.size();
    }

    private void assertNotPlayerTradedPokemon() throws TradedPokemonException {
        if (isTradedPokemon) {
            throw new TradedPokemonException();
        }
    }

    private void assertExistDefeatedTrainer() throws NotExistDefeatedTrainerException {
        if (defeatedTrainersCount == 0) {
            throw new NotExistDefeatedTrainerException();
        }
    }

    private void assertNotExistDefeatedTrainer() throws ExistDefeatedTrainerException {
        if (defeatedTrainersCount != 0) {
            throw new ExistDefeatedTrainerException();
        }
    }
}
