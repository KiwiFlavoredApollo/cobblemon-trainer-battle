package kiwiapollo.cobblemontrainerbattle.battle.battlefactory;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveSet;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.PokemonStats;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.BattleFactoryPlayer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.BattleFactoryTrainer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.*;
import kiwiapollo.cobblemontrainerbattle.common.RandomTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.parser.history.BattleRecord;
import kiwiapollo.cobblemontrainerbattle.parser.history.PlayerHistoryManager;
import kiwiapollo.cobblemontrainerbattle.parser.history.MaximumStreakRecord;
import kiwiapollo.cobblemontrainerbattle.parser.profile.MiniGameProfileStorage;
import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerProfileStorage;
import kiwiapollo.cobblemontrainerbattle.battle.postbattle.DefeatActionSetHandler;
import kiwiapollo.cobblemontrainerbattle.battle.postbattle.VictoryActionSetHandler;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.session.SessionTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exception.*;
import kiwiapollo.cobblemontrainerbattle.battle.session.PokemonTradeFeature;
import kiwiapollo.cobblemontrainerbattle.battle.session.RentalPokemonFeature;
import kiwiapollo.cobblemontrainerbattle.battle.session.Session;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattleStorage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BattleFactorySession implements Session, PokemonTradeFeature, RentalPokemonFeature {
    private static final int LEVEL = 100;
    private static final int POKEMON_COUNT = 3;
    private static final int ROUND_COUNT = 7;

    private final List<Identifier> trainersToDefeat;
    private final VictoryActionSetHandler sessionVictoryHandler;
    private final DefeatActionSetHandler sessionDefeatHandler;
    private final SoundEvent battleTheme;
    private final List<MessagePredicate<PlayerBattleParticipant>> predicates;

    private PlayerBattleParticipant player;
    private TrainerBattle lastTrainerBattle;
    private int defeatedTrainersCount;
    private boolean isPlayerDefeated;
    private boolean isTradedPokemon;

    public BattleFactorySession(ServerPlayerEntity player) {
        this.player = new BattleFactoryPlayer(player, LEVEL);

        this.trainersToDefeat = createTrainersToDefeat();
        BattleFactoryProfile profile = MiniGameProfileStorage.getBattleFactoryProfile();
        this.sessionVictoryHandler = new VictoryActionSetHandler(player, profile.onVictory);
        this.sessionDefeatHandler = new DefeatActionSetHandler(player, profile.onDefeat);
        this.battleTheme = profile.battleTheme;
        this.predicates = List.of(
                new RequiredLabelExistPredicate(profile.requiredLabel),
                new RequiredPokemonExistPredicate(profile.requiredPokemon),
                new RequiredHeldItemExistPredicate(profile.requiredHeldItem),
                new RequiredAbilityExistPredicate(profile.requiredAbility),
                new RequiredMoveExistPredicate(profile.requiredMove),
                new ForbiddenLabelNotExistPredicate(profile.forbiddenLabel),
                new ForbiddenPokemonNotExistPredicate(profile.forbiddenPokemon),
                new ForbiddenHeldItemNotExistPredicate(profile.forbiddenHeldItem),
                new ForbiddenAbilityNotExistPredicate(profile.forbiddenAbility),
                new ForbiddenMoveNotExistPredicate(profile.forbiddenMove)
        );

        this.defeatedTrainersCount = 0;
        this.isPlayerDefeated = false;
        this.isTradedPokemon = false;
    }

    private static List<Identifier> createTrainersToDefeat() {
        List<Identifier> trainers = new ArrayList<>();
        for (int i = 0; i < ROUND_COUNT; i++) {
            trainers.add(new RandomTrainerFactory(BattleFactorySession::hasMinimumPokemon).create());
        }
        return trainers;
    }

    private static boolean hasMinimumPokemon(Identifier trainer) {
        return TrainerProfileStorage.getProfileRegistry().get(trainer).team().size() > POKEMON_COUNT;
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

        TrainerBattleParticipant trainer = new BattleFactoryTrainer(getNextTrainer(), player.getPlayerEntity(), LEVEL);
        TrainerBattle trainerBattle = new SessionTrainerBattle(player, trainer, this);
        trainerBattle.start();

        TrainerBattleStorage.getTrainerBattleRegistry().put(player.getUuid(), trainerBattle);

        this.lastTrainerBattle = trainerBattle;
    }

    private Identifier getNextTrainer() {
        return trainersToDefeat.get(defeatedTrainersCount);
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

        printPokemons(player, lastTrainerBattle.getTrainer().getParty());
    }

    @Override
    public boolean isTradedPokemon() {
        return isTradedPokemon;
    }

    @Override
    public void showPartyPokemon() {
        printPokemons(player, player.getParty());
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
        printPokemons(player, player.getParty());

        CobblemonTrainerBattle.LOGGER.info("{} rerolled Pokemon", player.getName());
    }

    private static void printPokemons(PlayerBattleParticipant player, PartyStore party) {
        List<Pokemon> pokemons = party.toGappyList().stream().filter(Objects::nonNull).toList();
        for (int i = 0; i < pokemons.size(); i++) {
            Pokemon pokemon = pokemons.get(i);

            player.getPlayerEntity().sendMessage(Text.literal("[" + (i + 1) + "] ").append(pokemon.getDisplayName()).formatted(Formatting.YELLOW));
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

    public void onBattleVictory() {
        defeatedTrainersCount += 1;
        isTradedPokemon = false;
    }

    public void onBattleDefeat() {
        isPlayerDefeated = true;
    }

    @Override
    public void onSessionStop() {
        if (isAllTrainerDefeated()) {
            sessionVictoryHandler.run();
            updateVictoryRecord();

        } else {
            sessionDefeatHandler.run();
            updateDefeatRecord();
        }
    }

    private void updateVictoryRecord() {
        BattleRecord record = (BattleRecord) PlayerHistoryManager.get(player.getUuid()).get(Identifier.tryParse("minigame:battlefactory"));
        record.setVictoryCount(record.getVictoryCount() + 1);
    }

    private void updateDefeatRecord() {
        BattleRecord record = (BattleRecord) PlayerHistoryManager.get(player.getUuid()).get(Identifier.tryParse("minigame:battlefactory"));
        record.setDefeatCount(record.getDefeatCount() + 1);
    }

    // TODO do I need this for NORMAL BattleFactorySession
    private void updateStreakRecord() {
        MaximumStreakRecord record = (MaximumStreakRecord) PlayerHistoryManager.get(player.getUuid()).get(Identifier.tryParse("minigame:battlefactory"));

        int oldStreak = record.getMaximumStreak();
        int newStreak = getDefeatedTrainersCount();

        if (newStreak > oldStreak) {
            record.setMaximumStreak(newStreak);
        }
    }

    @Override
    public int getDefeatedTrainersCount() {
        return defeatedTrainersCount;
    }

    @Override
    public List<MessagePredicate<PlayerBattleParticipant>> getBattlePredicates() {
        return predicates;
    }

    @Override
    public boolean isPlayerDefeated() {
        return isPlayerDefeated;
    }

    @Override
    public boolean isAllTrainerDefeated() {
        return defeatedTrainersCount == trainersToDefeat.size();
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
