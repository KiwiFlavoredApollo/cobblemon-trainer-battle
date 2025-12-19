package kiwiapollo.cobblemontrainerbattle.battle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.abilities.Abilities;
import com.cobblemon.mod.common.api.abilities.Ability;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveSet;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemonParser;
import kiwiapollo.cobblemontrainerbattle.template.PokemonType;
import kotlin.Unit;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

            CobblemonTrainerBattle.LOGGER.info("Started trainer battle: {} versus {}", player.getName().getString(), trainer.getIdentifier());

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
        Instant timestamp = BattleHistoryStorage.getInstance().get(player.getUuid(), trainer.getIdentifier()).getTimestamp();
        long remains = trainer.getCooldownInSeconds() - Duration.between(timestamp, Instant.now()).toSeconds();

        if (remains > 0) {
            return remains;

        } else {
            return 0;
        }
    }

    protected boolean isPlayerPokemonExist() {
        return !player.getPokemonList().stream()
                .map(BattlePokemon::getEffectedPokemon)
                .filter(pokemon -> !pokemon.isFainted()).toList()
                .isEmpty();
    }

    protected boolean isTrainerPokemonExist() {
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

    private String toPokemonSpecies(ShowdownPokemon pokemon) {
        try {
            if (pokemon.form == null) {
                Species species = ShowdownPokemonParser.toSpecies(pokemon);
                return species.getTranslatedName().getString();

            } else {
                Species species = ShowdownPokemonParser.toSpecies(pokemon);
                return species.getTranslatedName().append(" ").append(pokemon.form).getString();
            }

        } catch (NullPointerException e) {
            CobblemonTrainerBattle.LOGGER.error("Unknown Pokemon species: {}", pokemon);
            throw new IllegalArgumentException(e);
        }
    }

    protected boolean hasAllRequiredType(){
        Set<PokemonType> required = trainer.getRequiredType();
        Set<PokemonType> types = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getTypes)
                .map(this::toPokemonType)
                .collect(Collectors.toSet());

        for (PokemonType r : required) {
            if (types.stream().noneMatch(r::equals)) {
                return false;
            }
        }

        return true;
    }

    private PokemonType toPokemonType(Iterable<ElementalType> iterable) {
        List<String> type = StreamSupport.stream(iterable.spliterator(), false)
                .map(ElementalType::getName).toList();

        if (type.size() == 2) {
            return new PokemonType(type.get(0), type.get(1));
        }

        if (type.size() == 1) {
            return new PokemonType(type.get(0));
        }

        throw new IllegalStateException();
    }

    protected boolean hasAllRequiredPokemon(){
        List<ShowdownPokemon> required = trainer.getRequiredPokemon();
        List<Pokemon> pokemon = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .toList();

        for (ShowdownPokemon r : required) {
            if (pokemon.stream().noneMatch(p -> isEqualPokemon(p, r))) {
                return false;
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

        for (String r : required) {
            if (!labels.contains(r)) {
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

        for (String r : required) {
            if (!moves.contains(r)) {
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
                .filter(i -> !i.equals(Items.AIR))
                .collect(Collectors.toSet());

        for (Item r : required) {
            if (!items.contains(r)) {
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

        for (String r : required) {
            if (!abilities.contains(r)) {
                return false;
            }
        }
        
        return true;
    }

    protected boolean hasAnyForbiddenType(){
        Set<PokemonType> forbidden = trainer.getForbiddenType();
        Set<PokemonType> types = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getTypes)
                .map(this::toPokemonType)
                .collect(Collectors.toSet());

        for (PokemonType f : forbidden) {
            if (types.stream().anyMatch(f::equals)) {
                return true;
            }
        }

        return false;
    }

    protected boolean hasAnyForbiddenPokemon(){
        List<ShowdownPokemon> forbidden = trainer.getForbiddenPokemon();
        List<Pokemon> pokemon = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .toList();

        for (ShowdownPokemon f : forbidden) {
            if (pokemon.stream().anyMatch(p -> isEqualPokemon(p, f))) {
                return true;
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

        for (String f : forbidden) {
            if (labels.contains(f)) {
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

        for (String f : forbidden) {
            if (moves.contains(f)) {
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
                .filter(i -> !i.equals(Items.AIR))
                .collect(Collectors.toSet());

        for (Item f : forbidden) {
            if (items.contains(f)) {
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

        for (String f : forbidden) {
            if (abilities.contains(f)) {
                return true;
            }
        }

        return false;
    }

    protected boolean hasOnlyAllowedType(){
        Set<PokemonType> allowed = trainer.getAllowedType();
        Set<PokemonType> types = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getTypes)
                .map(this::toPokemonType)
                .collect(Collectors.toSet());

        if (allowed.isEmpty()) {
            return true;
        }

        for (PokemonType t : types) {
            if (allowed.stream().noneMatch(t::equals)) {
                return false;
            }
        }

        return true;
    }

    protected boolean hasOnlyAllowedPokemon(){
        List<ShowdownPokemon> allowed = trainer.getAllowedPokemon();
        List<Pokemon> pokemon = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .toList();

        if (allowed.isEmpty()) {
            return true;
        }

        for (Pokemon p : pokemon) {
            if (allowed.stream().noneMatch(a -> isEqualPokemon(p, a))) {
                return false;
            };
        }

        return true;
    }

    protected boolean hasOnlyAllowedLabel() {
        Set<String> allowed = trainer.getAllowedLabel();
        Set<String> labels = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getSpecies)
                .map(Species::getLabels)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        if (allowed.isEmpty()) {
            return true;
        }

        for (String b : labels) {
            if (!allowed.contains(b)) {
                return false;
            }
        }

        return true;
    }

    protected boolean hasOnlyAllowedMove(){
        Set<String> allowed = trainer.getAllowedMove();
        Set<String> moves = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getMoveSet)
                .map(MoveSet::getMoves)
                .flatMap(List::stream)
                .map(Move::getName)
                .collect(Collectors.toSet());

        if (allowed.isEmpty()) {
            return true;
        }

        for (String m : moves) {
            if (!allowed.contains(m)) {
                return false;
            }
        }

        return true;
    }

    protected boolean hasOnlyAllowedHeldItem(){
        Set<Item> allowed = trainer.getAllowedHeldItem();
        Set<Item> items = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::heldItem)
                .map(ItemStack::getItem)
                .filter(i -> !i.equals(Items.AIR))
                .collect(Collectors.toSet());

        if (allowed.isEmpty()) {
            return true;
        }

        for (Item i : items) {
            if (!allowed.contains(i)) {
                return false;
            }
        }

        return true;
    }

    protected boolean hasOnlyAllowedAbility(){
        Set<String> allowed = trainer.getAllowedAbility();
        Set<String> abilities = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getAbility)
                .map(Ability::getName)
                .collect(Collectors.toSet());

        if (allowed.isEmpty()) {
            return true;
        }

        for (String a : abilities) {
            if (!allowed.contains(a)) {
                return false;
            }
        }

        return true;
    }

    protected Text getPlayerBusyErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.player_busy").formatted(Formatting.RED);
    }

    protected Text getRematchNotAllowedErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.rematch_not_allowed").formatted(Formatting.RED);
    }

    protected Text getCooldownNotElapsedErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.cooldown_not_elapsed", getRemainingCooldownInSeconds()).formatted(Formatting.RED);
    }

    protected Text getNoPlayerPokemonErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.no_player_pokemon").formatted(Formatting.RED);
    }

    protected Text getNoTrainerPokemonErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.no_trainer_pokemon").formatted(Formatting.RED);
    }

    protected Text getMaximumPartyLevelErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.maximum_party_level", trainer.getMaximumPartyLevel()).formatted(Formatting.RED);
    }

    protected Text getMinimumPartyLevelErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.minimum_party_level", trainer.getMinimumPartyLevel()).formatted(Formatting.RED);
    }

    protected Text getMaximumPartySizeErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.maximum_party_size", trainer.getMaximumPartySize()).formatted(Formatting.RED);
    }

    protected Text getMinimumPartySizeErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.minimum_party_size", trainer.getMinimumPartySize()).formatted(Formatting.RED);
    }
    
    protected Text getRequiredTypeErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.required_type", getMissingRequiredType()).formatted(Formatting.RED);
    }

    private String getMissingRequiredType() {
        Set<PokemonType> required = trainer.getRequiredType();
        Set<PokemonType> types = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getTypes)
                .map(this::toPokemonType)
                .collect(Collectors.toSet());

        for (PokemonType r : required) {
            if (types.stream().noneMatch(r::equals)) {
                return r.getString();
            }
        }

        throw new NoSuchElementException();
    }

    protected Text getRequiredAbilityErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.required_ability", getMissingRequiredAbility()).formatted(Formatting.RED);
    }

    private String getMissingRequiredAbility() {
        Set<String> required = trainer.getRequiredAbility();
        Set<String> abilities = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getAbility)
                .map(Ability::getName)
                .collect(Collectors.toSet());

        for (String r : required) {
            if (!abilities.contains(r)) {
                return r;
            }
        }

        throw new NoSuchElementException();
    }

    protected Text getRequiredHeldItemErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.required_held_item", getMissingRequiredHeldItem()).formatted(Formatting.RED);
    }

    private String getMissingRequiredHeldItem() {
        Set<Item> required = trainer.getRequiredHeldItem();
        Set<Item> items = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::heldItem)
                .map(ItemStack::getItem)
                .filter(i -> !i.equals(Items.AIR))
                .collect(Collectors.toSet());

        for (Item r : required) {
            if (!items.contains(r)) {
                return r.getName().getString();
            }
        }

        throw new NoSuchElementException();
    }

    protected Text getRequiredLabelErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.required_label", getMissingRequiredLabel()).formatted(Formatting.RED);
    }

    private String getMissingRequiredLabel() {
        Set<String> required = trainer.getRequiredLabel();
        Set<String> labels = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getSpecies)
                .map(Species::getLabels)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        for (String r : required) {
            if (!labels.contains(r)) {
                return r;
            }
        }

        throw new NoSuchElementException();
    }

    protected Text getRequiredMoveErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.required_move", getMissingRequiredMove()).formatted(Formatting.RED);
    }

    private String getMissingRequiredMove() {
        Set<String> required = trainer.getRequiredMove();
        Set<String> moves = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getMoveSet)
                .map(MoveSet::getMoves)
                .flatMap(List::stream)
                .map(Move::getName)
                .collect(Collectors.toSet());

        for (String r : required) {
            if (!moves.contains(r)) {
                return r;
            }
        }

        throw new NoSuchElementException();
    }

    protected Text getRequiredPokemonErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.required_pokemon", getMissingRequiredPokemon()).formatted(Formatting.RED);
    }

    private String getMissingRequiredPokemon() {
        List<ShowdownPokemon> required = trainer.getRequiredPokemon();
        List<Pokemon> pokemon = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .toList();

        for (ShowdownPokemon r : required) {
            if (pokemon.stream().noneMatch(p -> isEqualPokemon(p, r))) {
                return toPokemonSpecies(r);
            }
        }

        throw new NoSuchElementException();
    }

    protected Text getForbiddenTypeErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.forbidden_type", getExistingForbiddenType()).formatted(Formatting.RED);
    }

    private String getExistingForbiddenType() {
        Set<PokemonType> forbidden = trainer.getForbiddenType();
        Set<PokemonType> types = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getTypes)
                .map(this::toPokemonType)
                .collect(Collectors.toSet());

        for (PokemonType f : forbidden) {
            if (types.stream().anyMatch(f::equals)) {
                return f.getString();
            }
        }

        throw new NoSuchElementException();
    }

    protected Text getForbiddenAbilityErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.forbidden_ability", getExistingForbiddenAbility()).formatted(Formatting.RED);
    }

    private String getExistingForbiddenAbility() {
        Set<String> forbidden = trainer.getForbiddenAbility();
        Set<String> abilities = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getAbility)
                .map(Ability::getName)
                .collect(Collectors.toSet());

        for (String f : forbidden) {
            if (abilities.contains(f)) {
                return f;
            }
        }

        throw new NoSuchElementException();
    }

    protected Text getForbiddenHeldItemErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.forbidden_held_item", getExistingForbiddenHeldItem()).formatted(Formatting.RED);
    }

    private String getExistingForbiddenHeldItem() {
        Set<Item> forbidden = trainer.getForbiddenHeldItem();
        Set<Item> items = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::heldItem)
                .map(ItemStack::getItem)
                .filter(i -> !i.equals(Items.AIR))
                .collect(Collectors.toSet());

        for (Item f : forbidden) {
            if (items.contains(f)) {
                return f.getName().getString();
            }
        }

        throw new NoSuchElementException();
    }

    protected Text getForbiddenLabelErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.forbidden_label", getExistingForbiddenLabel()).formatted(Formatting.RED);
    }

    private String getExistingForbiddenLabel() {
        Set<String> forbidden = trainer.getForbiddenLabel();
        Set<String> labels = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getSpecies)
                .map(Species::getLabels)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        for (String f : forbidden) {
            if (labels.contains(f)) {
                return f;
            }
        }

        throw new NoSuchElementException();
    }

    protected Text getForbiddenMoveErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.forbidden_move", getExistingForbiddenMove()).formatted(Formatting.RED);
    }

    private String getExistingForbiddenMove() {
        Set<String> forbidden = trainer.getForbiddenMove();
        Set<String> moves = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getMoveSet)
                .map(MoveSet::getMoves)
                .flatMap(List::stream)
                .map(Move::getName)
                .collect(Collectors.toSet());

        for (String f : forbidden) {
            if (moves.contains(f)) {
                return f;
            }
        }

        throw new NoSuchElementException();
    }

    protected Text getForbiddenPokemonErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.forbidden_pokemon", getExistingForbiddenPokemon()).formatted(Formatting.RED);
    }

    private String getExistingForbiddenPokemon() {
        List<ShowdownPokemon> forbidden = trainer.getForbiddenPokemon();
        List<Pokemon> pokemon = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .toList();

        for (ShowdownPokemon f : forbidden) {
            if (pokemon.stream().anyMatch(p -> isEqualPokemon(p, f))) {
                return toPokemonSpecies(f);
            }
        }

        throw new NoSuchElementException();
    }

    protected Text getNotAllowedTypeErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.not_allowed_type", getExistingNotAllowedType()).formatted(Formatting.RED);
    }

    protected String getExistingNotAllowedType(){
        Set<PokemonType> allowed = trainer.getAllowedType();
        Set<PokemonType> types = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getTypes)
                .map(this::toPokemonType)
                .collect(Collectors.toSet());

        if (allowed.isEmpty()) {
            throw new NoSuchElementException();
        }

        for (PokemonType t : types) {
            if (allowed.stream().noneMatch(t::equals)) {
                return t.getString();
            }
        }

        throw new NoSuchElementException();
    }

    protected Text getNotAllowedAbilityErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.not_allowed_ability", getExistingNotAllowedAbility()).formatted(Formatting.RED);
    }

    private String getExistingNotAllowedAbility() {
        Set<String> allowed = trainer.getAllowedAbility();
        Set<String> abilities = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getAbility)
                .map(Ability::getName)
                .collect(Collectors.toSet());

        if (allowed.isEmpty()) {
            throw new NoSuchElementException();
        }

        for (String a : abilities) {
            if (!allowed.contains(a)) {
                return Abilities.INSTANCE.get(a).getDisplayName();
            }
        }

        throw new NoSuchElementException();
    }

    protected Text getNotAllowedHeldItemErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.not_allowed_held_item", getExistingNotAllowedHeldItem()).formatted(Formatting.RED);
    }

    private String getExistingNotAllowedHeldItem() {
        Set<Item> allowed = trainer.getAllowedHeldItem();
        Set<Item> items = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::heldItem)
                .map(ItemStack::getItem)
                .filter(i -> !i.equals(Items.AIR))
                .collect(Collectors.toSet());

        if (allowed.isEmpty()) {
            throw new NoSuchElementException();
        }

        for (Item i : items) {
            if (!allowed.contains(i)) {
                return i.getName().getString();
            }
        }

        throw new NoSuchElementException();
    }

    protected Text getNotAllowedLabelErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.not_allowed_label", getExistingNotAllowedLabel()).formatted(Formatting.RED);
    }

    private String getExistingNotAllowedLabel() {
        Set<String> allowed = trainer.getAllowedLabel();
        Set<String> labels = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getSpecies)
                .map(Species::getLabels)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        if (allowed.isEmpty()) {
            throw new NoSuchElementException();
        }

        for (String b : labels) {
            if (!allowed.contains(b)) {
                return b;
            }
        }

        throw new NoSuchElementException();
    }

    protected Text getNotAllowedMoveErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.not_allowed_move", getExistingNotAllowedMove()).formatted(Formatting.RED);
    }

    private String getExistingNotAllowedMove() {
        Set<String> allowed = trainer.getAllowedMove();
        Set<String> moves = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .map(Pokemon::getMoveSet)
                .map(MoveSet::getMoves)
                .flatMap(List::stream)
                .map(Move::getName)
                .collect(Collectors.toSet());

        if (allowed.isEmpty()) {
            throw new NoSuchElementException();
        }

        for (String m : moves) {
            if (!allowed.contains(m)) {
                return m;
            }
        }

        throw new NoSuchElementException();
    }

    protected Text getNotAllowedPokemonErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.not_allowed_pokemon", getExistingNotAllowedPokemon()).formatted(Formatting.RED);
    }

    private String getExistingNotAllowedPokemon() {
        List<ShowdownPokemon> allowed = trainer.getAllowedPokemon();
        List<Pokemon> pokemon = player.getPokemonList().stream()
                .filter(Objects::nonNull)
                .map(BattlePokemon::getEffectedPokemon)
                .toList();

        for (Pokemon p : pokemon) {
            if (allowed.stream().noneMatch(a -> isEqualPokemon(p, a))) {
                return p.getDisplayName().getString();
            };
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
