# NPC Trainers

NPC trainers make Cobblemon experience more interesting. Players must have VS Seeker in their inventories in order to spawn trainers around them. NPC Trainers can either be spawned with Trainer Spawn Eggs or with following commands.

## Trainers

- Replaces Neutral Trainers

```
/summon cobblemontrainerbattle:trainer ~ ~ ~ {Trainer:"radicalred/leader_brock"}
```
```
/kill <uuid>
/kill @e[type=cobblemontrainerbattle:trainer]
```

## Mannequins

- Replaces Static Trainers
- Invincible, Except `/kill` Command
- Persistent Entities

Mannequins spawned with Spawn Eggs are persistent by default. However, those spawned with `/summon` command need to have `PersistenceRequired` set explicitly.

```
/summon cobblemontrainerbattle:mannequin ~ ~ ~ {Trainer:"radicalred/leader_brock", PersistenceRequired:1b}
```
```
/kill <uuid>
/kill @e[type=cobblemontrainerbattle:mannequin]
```