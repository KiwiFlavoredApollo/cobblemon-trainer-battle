# Items

## Tokens & Tickets

Trainers drop Trainer Tokens when defeated. Trainer entities do not drop Tokens when killed by players. Trainer Tokens are used for crafting Tickets for battling Gym Leaders.

Gym Leaders, Elite Four and Champion drop special kind of Tokens when defeated. Gym Leader Tokens are used for crafting Tickets for battling Elite Four Ticket, and Elite Four Tokens are used for crafting Ticket for battling Champion. Using recipe mods like EMI is highly recommended. 

## VS Seeker

VS Seekers are available in various colors. However, They are not just for aesthetic purposes. VS Seekers serve two purposes.

### 1. Periodically Spawning Trainers Around Players

- Blue VS Seeker (All Trainers)
- Red VS Seeker (Radical Red Trainers)
- Green VS Seeker (Inclement Emerald Trainers)
- Purple VS Seeker (Smogon Trainers)
- Pink VS Seeker (Pokemon XY Trainers)
- Yellow VS Seeker (Pokemon BDSP Trainers)

Players can hold multiple VS Seekers in their inventories, allowing more than one group of trainers to spawn.

### 2. Spawning Gym Leaders, Elite Four and the Champion

Tickets alone cannot spawn trainers. Players must use (right-click) VS Seeker in one hand while Ticket in the other hand. Tickets are consumed when used.

## Trainer Spawn Egg

By default, Trainer Spawn Egg spawns random trainers. However, Trainer Spawn Egg can be configured to spawn specific trainer in specific texture.

### Minecraft 1.20.1

```
/give @p cobblemontrainerbattle:trainer_spawn_egg{EntityTag:{Trainer:"radicalred/leader_brock",Texture:"minecraft:textures/entity/player/slim/steve.png"}}
```

### Minecraft 1.21.1

```
/give @p cobblemontrainerbattle:trainer_spawn_egg[entity_data={id:trainer, Trainer:"radicalred/leader_brock",Texture:"minecraft:textures/entity/player/slim/steve.png"}]
```