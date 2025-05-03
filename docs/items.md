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

## Empty Poke Balls

Empty Poke Balls are used for storing Pokémon to Filled Poke Ball. It's basically converting Pokémon to items.

## Filled Poke Balls

Filled Poke Balls are intended to be used for creating custom trainer with Poke Ball Box. However, players can also use this for trading Pokémon like an item.

## Trainer Spawn Eggs

Trainer Spawn Eggs can be configured to spawn specific trainer in specific texture.

### Minecraft 1.20.1

```
/give @p cobblemontrainerbattle:neutral_trainer_spawn_egg{EntityTag:{Trainer:"radicalred/leader_brock"}}
/give @p cobblemontrainerbattle:hostile_trainer_spawn_egg{EntityTag:{Trainer:"radicalred/leader_brock"}}
/give @p cobblemontrainerbattle:static_trainer_spawn_egg{EntityTag:{Trainer:"radicalred/leader_brock"}}
```

### Minecraft 1.21.1

```
/give @p cobblemontrainerbattle:neutral_trainer_spawn_egg[entity_data={id:trainer, Trainer:"radicalred/leader_brock"}]
/give @p cobblemontrainerbattle:hostile_trainer_spawn_egg[entity_data={id:trainer, Trainer:"radicalred/leader_brock"}]
/give @p cobblemontrainerbattle:static_trainer_spawn_egg[entity_data={id:trainer, Trainer:"radicalred/leader_brock"}]
```

### Items

- Neutral Spawn Eggs
- Hostile Spawn Eggs
- Static Spawn Eggs

### Mob Spawner Integration

Trainer Spawn Eggs can be applied to Mob Spawners.