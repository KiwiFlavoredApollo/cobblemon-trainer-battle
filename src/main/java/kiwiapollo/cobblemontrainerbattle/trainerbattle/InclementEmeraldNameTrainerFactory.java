package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.exceptions.TrainerNameNotExistException;

public class InclementEmeraldNameTrainerFactory {
    TrainerFileScanner trainerFileScanner;

    public InclementEmeraldNameTrainerFactory() {
        this.trainerFileScanner = new InclementEmeraldTrainerFileScanner();
    }

    public Trainer create(String name) throws TrainerNameNotExistException {
        trainerFileScanner.assertExistTrainerName(name);
        return new TrainerFileParser().parse(trainerFileScanner.toTrainerFilePath(name));
    }
}
