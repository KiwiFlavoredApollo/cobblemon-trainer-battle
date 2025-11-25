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
import net.minecraft.util.Identifier;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
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

    protected boolean isEqualToOrLessThanMaximumPartyLevel() {
        int maximum = trainer.getMaximumPartyLevel();
        int player = this.player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getLevel)
                .reduce(Integer.MIN_VALUE, Integer::max);

        return player <= maximum;
    }

    protected boolean isEqualToOrGreaterThanMinimumPartyLevel() {
        int minimum = trainer.getMinimumPartyLevel();
        int player = this.player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getLevel)
                .reduce(Integer.MAX_VALUE, Integer::min);
        
        return player >= minimum;
    }
    
    protected boolean isEqualToOrLessThanMaximumPartySize() {
        int maximum = trainer.getMaximumPartySize();
        int player = this.player.getPokemonList().size();

        return player <= maximum;
    }
    
    protected boolean isEqualToOrGreaterThanMinimumPartySize() {
        int minimum = trainer.getMinimumPartySize();
        int player = this.player.getPokemonList().size();

        return player >= minimum;
    }

    private boolean hasPokemon(List<Pokemon> pokemon, ShowdownPokemon required) {
        for (Pokemon p : pokemon) {
            if (isSpeciesEqual(p, required) && isFormEqual(p, required)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSpeciesEqual(Pokemon party, ShowdownPokemon required) {
        try {
            Identifier p = party.getSpecies().getResourceIdentifier();
            Identifier r = ShowdownPokemonParser.toSpecies(required).getResourceIdentifier();
            return p.equals(r);

        } catch (NullPointerException e) {
            return false;
        }
    }

    private boolean isFormEqual(Pokemon party, ShowdownPokemon required) {
        if (required.form == null) {
            return true;
        } else {
            return party.getForm().getName().equals(required.form);
        }
    }

    // TODO
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
            Species species = ShowdownPokemonParser.toSpecies(pokemon);
            CobblemonTrainerBattle.LOGGER.error("Unknown Pokemon species: {}", species.getResourceIdentifier());
            throw new IllegalStateException(e);
        }
    }

    protected boolean hasRequiredPokemon(){
        List<ShowdownPokemon> required = trainer.getRequiredPokemon();
        List<Pokemon> pokemon = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .toList();

        for (ShowdownPokemon p : required) {
            if (!hasPokemon(pokemon, p)) {
                return false;
            }
        }
        return true;
    }
    
    protected boolean hasRequiredLabel(){
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

    protected boolean hasRequiredMove(){
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

    protected boolean hasRequiredHeldItem(){
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

    protected boolean hasRequiredAbility(){
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

    protected boolean hasForbiddenPokemon(){
        List<ShowdownPokemon> forbidden = trainer.getForbiddenPokemon();
        List<Pokemon> pokemon = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .toList();

        for (ShowdownPokemon p : forbidden) {
            if (!hasPokemon(pokemon, p)) {
                return false;
            }
        }
        return true;
    }

    protected boolean hasForbiddenLabel(){
        Set<String> forbidden = trainer.getForbiddenLabel();
        Set<String> labels = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getSpecies)
                .map(Species::getLabels)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        for (String b : forbidden) {
            if (!labels.contains(b)) {
                return false;
            }
        }
        return true;
    }

    protected boolean hasForbiddenMove(){
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
            if (!moves.contains(m)) {
                return false;
            }
        }
        return true;
    }

    protected boolean hasForbiddenHeldItem(){
        Set<Item> forbidden = trainer.getForbiddenHeldItem();
        Set<Item> items = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::heldItem)
                .map(ItemStack::getItem)
                .collect(Collectors.toSet());

        for (Item i : forbidden) {
            if (!items.contains(i)) {
                return false;
            }
        }

        return true;
    }

    protected boolean hasForbiddenAbility(){
        Set<String> forbidden = trainer.getForbiddenAbility();
        Set<String> abilities = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getAbility)
                .map(Ability::getName)
                .collect(Collectors.toSet());

        for (String a : forbidden) {
            if (!abilities.contains(a)) {
                return false;
            }
        }

        return true;
    }

    protected boolean isPlayerBusyWithPokemonBattle() {
        return Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(player.getEntity()) != null;
    }

    protected boolean isCooldownElapsed() {
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

    protected boolean isRematchAllowed() {
        return trainer.isRematchAllowed();
    }

    protected Text getMaximumPartyLevelErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.maximum_party_level", trainer.getMaximumPartyLevel());
    }

    protected Text getMinimumPartyLevelErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.minimum_party_level", trainer.getMinimumPartyLevel());
    }

    protected Text getMaximumPartySizeErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.maximum_party_size", trainer.getMaximumPartySize());
    }

    protected Text getMinimumPartySizeErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.minimum_party_size", trainer.getMinimumPartySize());
    }
    
    protected Text getPlayerBusyErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.player_not_busy");
    }
    
    protected Text getRematchErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.is_rematch_allowed");
    }
    
    protected Text getCooldownErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.cooldown_elapsed", getRemainingCooldownInSeconds());
    }
    
    protected Text getRequiredAbilityErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.required_ability", trainer.getRequiredAbility());
    }

    protected Text getRequiredHeldItemErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.required_held_item", trainer.getRequiredHeldItem());
    }

    protected Text getRequiredLabelErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.required_label", trainer.getRequiredLabel());
    }

    protected Text getRequiredMoveErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.required_move", trainer.getRequiredMove());
    }

    protected Text getRequiredPokemonErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.required_pokemon", trainer.getRequiredPokemon());
    }

    protected Text getForbiddenAbilityErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.forbidden_ability", trainer.getForbiddenAbility());
    }

    protected Text getForbiddenHeldItemErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.forbidden_held_item", trainer.getForbiddenHeldItem());
    }

    protected Text getForbiddenLabelErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.forbidden_label", trainer.getForbiddenLabel());
    }

    protected Text getForbiddenMoveErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.forbidden_move", trainer.getForbiddenMove());
    }

    protected Text getForbiddenPokemonErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.forbidden_pokemon", trainer.getForbiddenPokemon());
    }
}
