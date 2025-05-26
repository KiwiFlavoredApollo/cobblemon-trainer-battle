# NPC Trainers

NPC trainers make Cobblemon experience more interesting. Players must have VS Seeker in their inventories in order to spawn trainers around them. NPC Trainers can either be spawned with Trainer Spawn Eggs or with following commands.

## Neutral Trainers

```
/summon cobblemontrainerbattle:neutral_trainer ~ ~ ~ {Trainer:"radicalred/leader_brock"}
```
```
/kill <uuid>
/kill @e[type=cobblemontrainerbattle:neutral_trainer]
```

## Hostile Trainers

- Require VS Seekers
- 10% Chance of Spawning
- Give Status Conditions When Hit
- Faint Player Pokémon When Forfeit

Please refer to [Configuration](../configuration) for disabling Hostile Trainers.

```
/summon cobblemontrainerbattle:hostile_trainer ~ ~ ~ {Trainer:"radicalred/leader_brock"}
```
```
/kill <uuid>
/kill @e[type=cobblemontrainerbattle:hostile_trainer]
```

## Static Trainers

- Invincible, Except `/kill` Command
- Persistent Entities

Static Trainers spawned with Static Trainer Spawn Eggs are persistent by default. However, those spawned with `/summon` command need to have `PersistenceRequired` set explicitly.

```
/summon cobblemontrainerbattle:static_trainer ~ ~ ~ {Trainer:"radicalred/leader_brock", PersistenceRequired:1b}
```
```
/kill <uuid>
/kill @e[type=cobblemontrainerbattle:static_trainer]
```