package kiwiapollo.cobblemontrainerbattle.battle.battlefactory;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exception.AllTrainerDefeatedException;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.factory.SessionBattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.BattleFactoryPlayer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.*;
import kiwiapollo.cobblemontrainerbattle.parser.profile.MiniGameProfileStorage;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.session.SessionTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exception.*;
import kiwiapollo.cobblemontrainerbattle.battle.session.PokemonTradeFeature;
import kiwiapollo.cobblemontrainerbattle.battle.session.RentalPokemonFeature;
import kiwiapollo.cobblemontrainerbattle.battle.session.Session;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattleStorage;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;

public class BattleFactorySession implements Session, PokemonTradeFeature, RentalPokemonFeature {
    private static final int LEVEL = 100;

    private final SessionBattleParticipantFactory factory;
    protected PlayerBattleParticipant player;

    private final SoundEvent battleTheme;
    private TrainerBattle lastTrainerBattle;
    private int defeatedTrainersCount;
    private boolean isPlayerDefeated;
    private boolean isTradedPokemon;

    public BattleFactorySession(SessionBattleParticipantFactory factory) {
        this.factory = factory;
        this.player = factory.createPlayer(this);

        this.battleTheme = MiniGameProfileStorage.getBattleFactoryProfile().battleTheme;
        this.defeatedTrainersCount = 0;
        this.isPlayerDefeated = false;
        this.isTradedPokemon = false;
    }

    @Override
    public void startBattle() throws BattleStartException {
        List<MessagePredicate<BattleFactorySession>> predicates = List.of(
                new PlayerNotDefeatedPredicate<>(),
                new AnyTrainerNotDefeatedPredicate<>()
        );

        for (MessagePredicate<BattleFactorySession> predicate: predicates) {
            if (!predicate.test(this)) {
                player.sendErrorMessage(predicate.getErrorMessage());
                throw new BattleStartException();
            }
        }

        TrainerBattleParticipant trainer = factory.createTrainer(this);
        TrainerBattle trainerBattle = new SessionTrainerBattle(player, trainer, this);
        trainerBattle.start();

        TrainerBattleStorage.getTrainerBattleRegistry().put(player.getUuid(), trainerBattle);

        this.lastTrainerBattle = trainerBattle;
    }

    @Override
    public void tradePokemon(int playerSlot, int trainerSlot) throws SessionOperationException {
        List<MessagePredicate<BattleFactorySession>> predicates = List.of(
                new PlayerNotDefeatedPredicate<>(),
                new AnyTrainerDefeatedPredicate<>(),
                new PlayerNotTradedPokemonPredicate<>()
        );

        for (MessagePredicate<BattleFactorySession> predicate : predicates) {
            if (!predicate.test(this)) {
                player.sendErrorMessage(predicate.getErrorMessage());
                throw new SessionOperationException();
            }
        }

        TrainerBattleParticipant trainer = lastTrainerBattle.getTrainer();

        Pokemon trainerPokemon = trainer.getParty().get(trainerSlot - 1);
        Pokemon playerPokemon = player.getParty().get(playerSlot - 1);

        trainer.getParty().set(trainerSlot, playerPokemon);
        player.getParty().set(playerSlot, trainerPokemon);

        isTradedPokemon = true;

        player.sendInfoMessage(Text.translatable("command.cobblemontrainerbattle.battlefactory.tradepokemon.success", playerPokemon.getDisplayName(), trainerPokemon.getDisplayName()));
        CobblemonTrainerBattle.LOGGER.info("{} traded {} for {}", player.getName(), playerPokemon.getDisplayName(), trainerPokemon.getDisplayName());
    }

    @Override
    public void showTradeablePokemon() throws SessionOperationException {
        List<MessagePredicate<BattleFactorySession>> predicates = List.of(
                new PlayerNotDefeatedPredicate<>(),
                new AnyTrainerDefeatedPredicate<>(),
                new PlayerNotTradedPokemonPredicate<>()
        );

        for (MessagePredicate<BattleFactorySession> predicate : predicates) {
            if (!predicate.test(this)) {
                player.sendErrorMessage(predicate.getErrorMessage());
                throw new SessionOperationException();
            }
        }

        new PokemonStatusPrinter(player.getPlayerEntity()).print(lastTrainerBattle.getTrainer().getParty());
    }

    @Override
    public boolean isTradedPokemon() {
        return isTradedPokemon;
    }

    @Override
    public void showPartyPokemon() {
        new PokemonStatusPrinter(player.getPlayerEntity()).print(player.getParty());
    }

    @Override
    public void rerollPartyPokemon() throws SessionOperationException {
        List<MessagePredicate<BattleFactorySession>> predicates = List.of(
                new PlayerNotDefeatedPredicate<>(),
                new AllTrainerNotDefeatedPredicate<>()
        );

        for (MessagePredicate<BattleFactorySession> predicate : predicates) {
            if (!predicate.test(this)) {
                player.sendErrorMessage(predicate.getErrorMessage());
                throw new SessionOperationException();
            }
        }

        player = new BattleFactoryPlayer(player.getPlayerEntity(), LEVEL);
        new PokemonStatusPrinter(player.getPlayerEntity()).print(player.getParty());

        CobblemonTrainerBattle.LOGGER.info("{} rerolled Pokemon", player.getName());
    }

    public void onBattleVictory() {
        defeatedTrainersCount += 1;
        isTradedPokemon = false;
    }

    public void onBattleDefeat() {
        isPlayerDefeated = true;
    }

    @Override
    public void onSessionStop() {

    }

    @Override
    public int getDefeatedTrainersCount() {
        return defeatedTrainersCount;
    }

    @Override
    public List<MessagePredicate<PlayerBattleParticipant>> getBattlePredicates() {
        return List.of();
    }

    @Override
    public boolean isPlayerDefeated() {
        return isPlayerDefeated;
    }

    @Override
    public boolean isAllTrainerDefeated() {
        try {
            factory.createTrainer(this);
            return false;

        } catch (AllTrainerDefeatedException e) {
            return true;
        }
    }

    @Override
    public boolean isAnyTrainerDefeated() {
        return defeatedTrainersCount > 0;
    }

    @Override
    public Optional<SoundEvent> getBattleTheme() {
        return Optional.ofNullable(battleTheme);
    }
}
