package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.CreateTrainerFailedException;
import kiwiapollo.cobblemontrainerbattle.exceptions.WeightedRandomFailedException;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.*;

public class TotalRandomTrainerFactory {
    public Trainer create(ServerPlayerEntity player) {
        Map<RandomTrainerFactory, Integer> weightedTrainerFactories = Map.of(
                new RadicalRedRandomTrainerFactory(), CobblemonTrainerBattle.RADICAL_RED_TRAINERS.size(),
                new InclementEmeraldRandomTrainerFactory(), CobblemonTrainerBattle.INCLEMENT_EMERALD_TRAINERS.size(),
                new CustomRandomTrainerFactory(), CobblemonTrainerBattle.CUSTOM_TRAINERS.size()
        );

        try {
            return getWeightedRandom(toWeightedTrainers(weightedTrainerFactories, player));

        } catch (WeightedRandomFailedException e) {
            CobblemonTrainerBattle.LOGGER.error("Error occured while getting random trainer with weights");
            List<RandomTrainerFactory> randomTrainerFactories = weightedTrainerFactories.keySet().stream().toList();
            return getRoughRandom(toTrainers(randomTrainerFactories, player));
        }
    }

    private List<Trainer> toTrainers(List<RandomTrainerFactory> randomTrainerFactories, ServerPlayerEntity player) {
        List<Trainer> trainers = new ArrayList<>();

        for (RandomTrainerFactory randomTrainerFactory : randomTrainerFactories) {
            try {
                trainers.add(randomTrainerFactory.create(player));

            } catch (CreateTrainerFailedException ignored) {

            }
        }

        return trainers;
    }

    private Map<Trainer, Integer> toWeightedTrainers(Map<RandomTrainerFactory, Integer> weightedTrainerFactories, ServerPlayerEntity player) {
        Map<Trainer, Integer> weightedTrainers = new HashMap<>();

        for (Map.Entry<RandomTrainerFactory, Integer> weightedFactory : weightedTrainerFactories.entrySet()) {
            try {
                weightedTrainers.put(weightedFactory.getKey().create(player), weightedFactory.getValue());

            } catch (CreateTrainerFailedException ignored) {

            }
        }

        return weightedTrainers;
    }

    private Trainer getWeightedRandom(Map<Trainer, Integer> weightedTrainers) throws WeightedRandomFailedException {
        int totalWeight = weightedTrainers.values().stream().reduce(0, Integer::sum);
        int randomValue = new Random().nextInt(totalWeight);

        int currentWeight = 0;
        for (Map.Entry<Trainer, Integer> entry : weightedTrainers.entrySet()) {
            currentWeight += entry.getValue();
            if (randomValue < currentWeight) {
                return entry.getKey();
            }
        }

        throw new WeightedRandomFailedException();
    }

    private Trainer getRoughRandom(List<Trainer> trainers) {
        int random = new Random().nextInt();
        return trainers.get(random);
    }
}
