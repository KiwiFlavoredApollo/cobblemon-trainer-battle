package kiwiapollo.cobblemontrainerbattle.battle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.abilities.Ability;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveSet;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.history.PlayerHistoryStorage;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemonParser;
import kotlin.Unit;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public abstract class CustomPokemonBattle implements PokemonBattleBehavior {
    private final PlayerBattleActor player;
    private final TrainerBattleActor trainer;
    private UUID battleId;

    public CustomPokemonBattle(PlayerBattleActor player, TrainerBattleActor trainer) {
        this.player = player;
        this.trainer = trainer;
        this.battleId = null;
    }

    @Override
    public void start() throws BattleStartException {
        recallSentOutPokemon(player);

        Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                trainer.getBattleFormat(),
                new BattleSide(player),
                new BattleSide(trainer),
                false

        ).ifSuccessful(battle -> {
            this.battleId = battle.getBattleId();
            player.setBattleTheme(trainer.getBattleTheme());

            player.sendMessage(Text.translatable("command.cobblemontrainerbattle.success.trainerbattle", trainer.getName()));
            CobblemonTrainerBattle.LOGGER.info("Started trainer battle : {} versus {}", player.getName().getString(), trainer.getIdentifier());

            return Unit.INSTANCE;
        });
    }

    @Override
    public UUID getBattleId() {
        return this.battleId;
    }

    private void recallSentOutPokemon(PlayerBattleActor player) {
        Cobblemon.INSTANCE.getStorage()
                .getParty(player.getEntity()).toGappyList().stream()
                .filter(Objects::nonNull)
                .forEach(Pokemon::recall);
    }

    protected boolean isPlayerBusyWithPokemonBattle() {
        return Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(player.getEntity()) != null;
    }

    protected boolean isTrainerRematchAllowed() {
        return trainer.isRematchAllowed();
    }

    protected boolean isTrainerCooldownElapsed() {
        return getRemainingCooldownInSeconds() == 0;
    }

    protected long getRemainingCooldownInSeconds() {
        Instant timestamp = PlayerHistoryStorage.getInstance().get(player.getEntity()).get(trainer.getIdentifier()).getTimestamp();
        long remains = trainer.getCooldownInSeconds() - Duration.between(timestamp, Instant.now()).toSeconds();

        if (remains > 0) {
            return remains;

        } else {
            return 0;
        }
    }

    protected boolean isPlayerPokemonReady() {
        return !player.getPokemonList().stream()
                .map(BattlePokemon::getEffectedPokemon)
                .filter(pokemon -> !pokemon.isFainted()).toList()
                .isEmpty();
    }

    protected boolean isTrainerPokemonReady() {
        return !trainer.getPokemonList().stream()
                .map(BattlePokemon::getEffectedPokemon)
                .filter(pokemon -> !pokemon.isFainted()).toList()
                .isEmpty();
    }

    protected boolean isAtMostMaximumPartyLevel() {
        int maximum = trainer.getMaximumPartyLevel();
        int player = this.player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getLevel)
                .reduce(Integer.MIN_VALUE, Integer::max);

        return player <= maximum;
    }

    protected boolean isAtLeastMinimumPartyLevel() {
        int minimum = trainer.getMinimumPartyLevel();
        int player = this.player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getLevel)
                .reduce(Integer.MAX_VALUE, Integer::min);
        
        return player >= minimum;
    }
    
    protected boolean isAtMostMaximumPartySize() {
        int maximum = trainer.getMaximumPartySize();
        int player = this.player.getPokemonList().size();

        return player <= maximum;
    }
    
    protected boolean isAtLeastMinimumPartySize() {
        int minimum = trainer.getMinimumPartySize();
        int player = this.player.getPokemonList().size();

        return player >= minimum;
    }

    private boolean isEqualSpecies(Pokemon party, ShowdownPokemon showdown) {
        try {
            Identifier p = party.getSpecies().getResourceIdentifier();
            Identifier r = ShowdownPokemonParser.toSpecies(showdown).getResourceIdentifier();
            return p.equals(r);

        } catch (NullPointerException e) {
            return false;
        }
    }

    private boolean isEqualForm(Pokemon party, ShowdownPokemon showdown) {
        if (showdown.form == null) {
            return true;
        } else {
            return party.getForm().getName().equals(showdown.form);
        }
    }

    private boolean isEqualPokemon(Pokemon pokemon, ShowdownPokemon showdown) {
        return isEqualSpecies(pokemon, showdown) && isEqualForm(pokemon, showdown);
    }

    // TODO better name
    protected Text toPokemonDescriptor(ShowdownPokemon pokemon) {
        try {
            if (pokemon.form == null) {
                Species species = ShowdownPokemonParser.toSpecies(pokemon);
                return species.getTranslatedName();

            } else {
                Species species = ShowdownPokemonParser.toSpecies(pokemon);
                return species.getTranslatedName().append(" ").append(pokemon.form);
            }

        } catch (NullPointerException e) {
            CobblemonTrainerBattle.LOGGER.error("Unknown Pokemon species: {}", pokemon);
            throw new IllegalArgumentException(e);
        }
    }

    protected boolean hasAllRequiredPokemon(){
        List<ShowdownPokemon> required = trainer.getRequiredPokemon();
        List<Pokemon> pokemon = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .toList();

        for (ShowdownPokemon s : required) {
            for (Pokemon p : pokemon) {
                if (!isEqualPokemon(p, s)) {
                    return false;
                }
            }
        }

        return true;
    }

    protected boolean hasAllRequiredLabel(){
        Set<String> required = trainer.getRequiredLabel();
        Set<String> labels = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getSpecies)
                .map(Species::getLabels)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        for (String b : required) {
            if (!labels.contains(b)) {
                return false;
            }
        }

        return true;
    }

    protected boolean hasAllRequiredMove(){
        Set<String> required = trainer.getRequiredMove();
        Set<String> moves = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getMoveSet)
                .map(MoveSet::getMoves)
                .flatMap(List::stream)
                .map(Move::getName)
                .collect(Collectors.toSet());

        for (String m : required) {
            if (!moves.contains(m)) {
                return false;
            }
        }

        return true;
    }

    protected boolean hasAllRequiredHeldItem(){
        Set<Item> required = trainer.getRequiredHeldItem();
        Set<Item> items = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::heldItem)
                .map(ItemStack::getItem)
                .collect(Collectors.toSet());

        for (Item i : required) {
            if (!items.contains(i)) {
                return false;
            }
        }
        
        return true;
    }

    protected boolean hasAllRequiredAbility(){
        Set<String> required = trainer.getRequiredAbility();
        Set<String> abilities = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getAbility)
                .map(Ability::getName)
                .collect(Collectors.toSet());

        for (String a : required) {
            if (!abilities.contains(a)) {
                return false;
            }
        }
        
        return true;
    }

    protected boolean hasAnyForbiddenPokemon(){
        List<ShowdownPokemon> forbidden = trainer.getForbiddenPokemon();
        List<Pokemon> pokemon = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .toList();

        for (ShowdownPokemon s : forbidden) {
            for (Pokemon p : pokemon) {
                if (isEqualPokemon(p, s)) {
                    return true;
                }
            }
        }

        return false;
    }

    protected boolean hasAnyForbiddenLabel(){
        Set<String> forbidden = trainer.getForbiddenLabel();
        Set<String> labels = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getSpecies)
                .map(Species::getLabels)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        for (String b : forbidden) {
            if (labels.contains(b)) {
                return true;
            }
        }

        return false;
    }

    protected boolean hasAnyForbiddenMove(){
        Set<String> forbidden = trainer.getForbiddenMove();
        Set<String> moves = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getMoveSet)
                .map(MoveSet::getMoves)
                .flatMap(List::stream)
                .map(Move::getName)
                .collect(Collectors.toSet());

        for (String m : forbidden) {
            if (moves.contains(m)) {
                return true;
            }
        }

        return false;
    }

    protected boolean hasAnyForbiddenHeldItem(){
        Set<Item> forbidden = trainer.getForbiddenHeldItem();
        Set<Item> items = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::heldItem)
                .map(ItemStack::getItem)
                .collect(Collectors.toSet());

        for (Item i : forbidden) {
            if (items.contains(i)) {
                return true;
            }
        }

        return false;
    }

    protected boolean hasAnyForbiddenAbility(){
        Set<String> forbidden = trainer.getForbiddenAbility();
        Set<String> abilities = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getAbility)
                .map(Ability::getName)
                .collect(Collectors.toSet());

        for (String a : forbidden) {
            if (abilities.contains(a)) {
                return true;
            }
        }

        return false;
    }

    protected Text getPlayerBusyErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.player_not_busy").formatted(Formatting.RED);
    }

    protected Text getRematchNotAllowedErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.is_rematch_allowed").formatted(Formatting.RED);
    }

    protected Text getCooldownNotElapsedErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.cooldown_elapsed", getRemainingCooldownInSeconds()).formatted(Formatting.RED);
    }

    protected Text getPlayerPokemonNotReadyErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.player_party_not_empty").formatted(Formatting.RED);
    }

    protected Text getTrainerPokemonNotReadyErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.trainer_party_not_empty").formatted(Formatting.RED);
    }

    protected Text getMaximumPartyLevelErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.maximum_party_level", trainer.getMaximumPartyLevel()).formatted(Formatting.RED);
    }

    protected Text getMinimumPartyLevelErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.minimum_party_level", trainer.getMinimumPartyLevel()).formatted(Formatting.RED);
    }

    protected Text getMaximumPartySizeErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.maximum_party_size", trainer.getMaximumPartySize()).formatted(Formatting.RED);
    }

    protected Text getMinimumPartySizeErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.minimum_party_size", trainer.getMinimumPartySize()).formatted(Formatting.RED);
    }
    
    protected Text getRequiredAbilityErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.required_ability", trainer.getRequiredAbility()).formatted(Formatting.RED);
    }

    protected Text getRequiredHeldItemErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.required_held_item", trainer.getRequiredHeldItem()).formatted(Formatting.RED);
    }

    protected Text getRequiredLabelErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.required_label", trainer.getRequiredLabel()).formatted(Formatting.RED);
    }

    protected Text getRequiredMoveErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.required_move", trainer.getRequiredMove()).formatted(Formatting.RED);
    }

    protected Text getRequiredPokemonErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.required_pokemon", toPokemonDescriptor(getMissingRequiredPokemon())).formatted(Formatting.RED);
    }

    private ShowdownPokemon getMissingRequiredPokemon() {
        List<ShowdownPokemon> required = trainer.getRequiredPokemon();
        List<Pokemon> pokemon = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .toList();

        for (ShowdownPokemon s : required) {
            for (Pokemon p : pokemon) {
                if (!isEqualPokemon(p, s)) {
                    return s;
                }
            }
        }

        throw new NoSuchElementException();
    }

    protected Text getForbiddenAbilityErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.forbidden_ability", trainer.getForbiddenAbility()).formatted(Formatting.RED);
    }

    protected Text getForbiddenHeldItemErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.forbidden_held_item", trainer.getForbiddenHeldItem()).formatted(Formatting.RED);
    }

    protected Text getForbiddenLabelErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.forbidden_label", trainer.getForbiddenLabel()).formatted(Formatting.RED);
    }

    protected Text getForbiddenMoveErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.forbidden_move", trainer.getForbiddenMove()).formatted(Formatting.RED);
    }

    protected Text getForbiddenPokemonErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.forbidden_pokemon", toPokemonDescriptor(getExistingForbiddenPokemon())).formatted(Formatting.RED);
    }

    private ShowdownPokemon getExistingForbiddenPokemon() {
        List<ShowdownPokemon> forbidden = trainer.getForbiddenPokemon();
        List<Pokemon> pokemon = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .toList();

        for (ShowdownPokemon s : forbidden) {
            for (Pokemon p : pokemon) {
                if (isEqualPokemon(p, s)) {
                    return s;
                }
            }
        }

        throw new NoSuchElementException();
    }

    protected boolean isPlayerPokemonCount(int count) {
        return player.getPokemonList().size() == count;
    }

    protected boolean isTrainerPokemonCount(int count) {
        return trainer.getPokemonList().size() == count;
    }
}
