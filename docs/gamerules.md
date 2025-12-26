# Game Rules

Game rule replaces config files. 

## `maximumTrainerCountPerPlayer`

Defines the number of trainers spawn around a player.

## `trainerSpawnIntervalInSeconds`

Defines trainer spawn interval in seconds.

## `doTrainerApplyStatusCondition`

Trainers retaliate when attacked by players. When a player get hit by trainers, one of the player's Pokémon will receive random status condition.

## Example

```
/gamerule maximumTrainerCountPerPlayer 1
/gamerule trainerSpawnIntervalInSeconds 60
/gamerule doTrainerApplyStatusCondition true
```