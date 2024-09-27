package kiwiapollo.trainerbattle;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.drops.LootDroppedEvent;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import kiwiapollo.trainerbattle.commands.BattleFrontierCommand;
import kiwiapollo.trainerbattle.commands.TrainerBattleCommand;
import kiwiapollo.trainerbattle.exceptions.NotTrainerBattleException;
import kiwiapollo.trainerbattle.exceptions.NotTrainerPokemonException;
import kotlin.Unit;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Stream;

public class TrainerBattle implements ModInitializer {
	public static final String NAMESPACE = "trainerbattle";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
	public static final Set<PokemonBattle> TRAINER_BATTLES = new HashSet<>();

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(new TrainerBattleCommand());
			dispatcher.register(new BattleFrontierCommand());
		});

		CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, battleVictoryEvent -> {
			try {
				// BATTLE_VICTORY event fires even if the player loses
				LOGGER.info("BATTLE_VICTORY event");

				assertTrainerBattle(battleVictoryEvent.getBattle());
				TRAINER_BATTLES.remove(battleVictoryEvent.getBattle());

				return Unit.INSTANCE;

			} catch (NotTrainerBattleException e) {
				return Unit.INSTANCE;
            }
        });

		CobblemonEvents.LOOT_DROPPED.subscribe(Priority.HIGHEST, lootDroppedEvent -> {
			try {
				// LOOT_DROPPED event fires before BATTLE_VICTORY event
				// Cobblemon Discord, Hiroku: It's only used if the player kills the pokemon by hand, not by battle
				// However Pokemons drop loot when defeated in battles, at least on 1.5.1
				LOGGER.info("LOOT_DROPPED event");

				assertTrainerBattlePokemon(lootDroppedEvent);
				lootDroppedEvent.cancel();
				LOGGER.info("Cancelled LOOT_DROPPED event");

				return Unit.INSTANCE;

			} catch (NotTrainerPokemonException e) {
				return Unit.INSTANCE;
            }
        });
	}

	private void assertTrainerBattle(PokemonBattle battle) throws NotTrainerBattleException {
		Stream<UUID> trainerBattleUuids = TRAINER_BATTLES.stream().map(PokemonBattle::getBattleId);
		if (trainerBattleUuids.noneMatch(uuid -> uuid == battle.getBattleId())) {
			throw new NotTrainerBattleException();
		}
	}

	private void assertTrainerBattlePokemon(LootDroppedEvent lootDroppedEvent) throws NotTrainerPokemonException {
		if (!(lootDroppedEvent.getEntity() instanceof PokemonEntity)) {
			throw new NotTrainerPokemonException();
		};

		PokemonEntity pokemonEntity = (PokemonEntity) lootDroppedEvent.getEntity();
		List<UUID> battleIds = TRAINER_BATTLES.stream().map(PokemonBattle::getBattleId).toList();
		if (battleIds.contains(pokemonEntity.getBattleId())) {
			return;
		}

		throw new NotTrainerPokemonException();
	}
}