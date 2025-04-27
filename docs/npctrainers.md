# NPC Trainers

NPC trainers make Cobblemon experience more interesting. Players must have VS Seeker in their inventories in order to spawn trainers around them. NPC Trainers can either be spawned with Trainer Spawn Eggs or with following commands.

## Trainers

```
/summon cobblemontrainerbattle:trainer ~ ~ ~ {Trainer:"radicalred/leader_brock",Texture:"minecraft:textures/entity/player/slim/steve.png"}
```
```
/kill <uuid>
/kill @e[type=cobblemontrainerbattle:trainer]
```

## Trainers (>=1.9.15)

### Trainers

Command no longer accept `Texture`. The texture will be set according to the Trainer Preset.

```
/summon cobblemontrainerbattle:trainer ~ ~ ~ {Trainer:"radicalred/leader_brock"}
```
```
/kill <uuid>
/kill @e[type=cobblemontrainerbattle:trainer]
```

### Static Trainers

Static Trainers are special kind of Trainers. Static Trainers can only be removed with `/kill` command. Static Trainers are persistent by default but those spawned with `/summon` command need to have `PersistenceRequired` set explicitly.

```
/summon cobblemontrainerbattle:trainer ~ ~ ~ {Trainer:"radicalred/leader_brock",Texture:"minecraft:textures/entity/player/slim/steve.png", PersistenceRequired:1b}
```
```
/kill <uuid>
/kill @e[type=cobblemontrainerbattle:static_trainer]
```