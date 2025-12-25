# Items

## Tokens & Tickets

Trainers drop Trainer Tokens when defeated in battle. Trainers do not drop Tokens when killed. Trainer Tokens are used for crafting Trainer Tickets.

## Tickets

Tickets are used for spawning Gym Leaders, Elite Fours and Champions. VS Seeker is no longer needed. Right-click on the block you wish to spawn them.

## VS Seeker

In order to spawn wild trainers, players must have VS Seekers in their inventories. VS Seekers are available in various colors and each of them spawns specific group of trainers.

## Empty Poke Balls (deprecated)

Empty Poke Balls are used for storing Pokémon to Filled Poke Ball. It's basically converting Pokémon to items.

## Filled Poke Balls (deprecated)

Filled Poke Balls are intended to be used for creating custom trainer with Poke Ball Box. However, players can also use this for trading Pokémon like an item.

## Trainer Spawn Eggs

Trainer Spawn Eggs can be configured to spawn specific trainer in specific texture.

### Minecraft 1.20.1

```
/give @p cobblemontrainerbattle:trainer_spawn_egg{EntityTag:{Trainer:"radicalred/leader_brock"}}
/give @p cobblemontrainerbattle:mannequin_spawn_egg{EntityTag:{Trainer:"radicalred/leader_brock"}}
```

### Minecraft 1.21.1

```
/give @p cobblemontrainerbattle:trainer_spawn_egg[entity_data={id:trainer, Trainer:"radicalred/leader_brock"}]
/give @p cobblemontrainerbattle:mannequin_spawn_egg[entity_data={id:trainer, Trainer:"radicalred/leader_brock"}]
```

### Mob Spawner Integration

Trainer Spawn Eggs can be applied to Mob Spawners.