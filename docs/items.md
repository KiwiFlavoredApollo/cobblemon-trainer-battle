# Items

## Tokens & Tickets

Trainers drop Trainer Tokens when defeated. Trainer entities do not drop Tokens when killed by players. Trainer Tokens are used for crafting Tickets for battling Gym Leaders.

Gym Leaders, Elite Four and Champion drop special kind of Tokens when defeated. Gym Leader Tokens are used for crafting Elite Four Tickets, and Elite Four Tokens are used for crafting Champion Tickets. Using recipe mods like EMI is highly recommended. 

## VS Seeker

VS Seekers are available in various colors. However, They are not just for aesthetic purposes. VS Seekers serve two purposes.

### 1. Periodically Spawns Trainers Around Players

Players can hold multiple VS Seekers in their inventories, allowing more than one group of trainers to spawn.

### 2. Manually Spawns Gym Leaders, Elite Four and the Champion

Tickets alone cannot spawn trainers. While aiming at a block nearby, players must use (right-click) VS Seeker while holding a Ticket in the other hand. Tickets are consumed when used.

## Trainer Spawn Eggs

By default, Trainer Spawn Eggs spawn random trainers. Trainer Spawn Eggs can be configured to spawn specific trainer in specific texture.

### Minecraft 1.20.1

```
/give @p cobblemontrainerbattle:trainer_spawn_egg{EntityTag:{Trainer:"radicalred/leader_brock",Texture:"minecraft:textures/entity/player/slim/steve.png"}}
```

### Minecraft 1.21.1

```
/give @p cobblemontrainerbattle:trainer_spawn_egg[entity_data={id:trainer, Trainer:"radicalred/leader_brock",Texture:"minecraft:textures/entity/player/slim/steve.png"}]
```

## Trainer Spawn Eggs (>=1.9.15)

### Trainer Spawn Eggs

Trainer Spawn Eggs spawn random trainers. Trainer Spawn Eggs can be configured to spawn specific trainer.

### Static Trainer Spawn Eggs

Static Trainer Spawn Eggs spawn trainers that do not move. These trainers can only be killed with `/kill` command.

### Mob Spawner Integration

Spawn Eggs can be applied to Mob Spawners.

### Minecraft 1.20.1

Command no longer accept `Texture`. The texture will be set according to the Trainer Preset.

```
/give @p cobblemontrainerbattle:trainer_spawn_egg{EntityTag:{Trainer:"radicalred/leader_brock"}}
```

### Minecraft 1.21.1

Command no longer accept `Texture`. The texture will be set according to the Trainer Preset.

```
/give @p cobblemontrainerbattle:trainer_spawn_egg[entity_data={id:trainer, Trainer:"radicalred/leader_brock"}]
```