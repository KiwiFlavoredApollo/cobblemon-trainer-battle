package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.exceptions.TrainerNameNotExistException;

public class NameTrainerFactory {
    public Trainer create(String name) throws TrainerNameNotExistException {
        RadicalRedTrainerFileScanner.assertExistTrainerName(name);
        return new TrainerFileParser().parse(RadicalRedTrainerFileScanner.toTrainerFilePath(name));
    }
}
