package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.exceptions.TrainerNameNotExistException;

public class RadicalRedNameTrainerFactory {
    TrainerFileScanner trainerFileScanner;

    public RadicalRedNameTrainerFactory() {
        this.trainerFileScanner = new RadicalRedTrainerFileScanner();
    }

    public Trainer create(String name) throws TrainerNameNotExistException {
        trainerFileScanner.assertExistTrainerName(name);
        return new TrainerFileParser().parse(trainerFileScanner.toTrainerFilePath(name));
    }
}
