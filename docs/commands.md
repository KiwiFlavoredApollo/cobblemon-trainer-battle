# Commands

## Utility Commands

### Reload

```
/cobblemontrainerbattle reload
```
```
cobblemontrainerbattle.reload
```

Reloads configuration file located at `.minecraft/config/cobblemontrainerbattle/config.json`

### Export

```
/cobblemontrainerbattle export <player>
/cobblemontrainerbattle exportflat <player> <level>
/cobblemontrainerbattle exportrelative <player>
```
```
cobblemontrainerbattle.export
```

Exports player Pokémon to `.minecraft/cobblemontrainerbattle/`. While it's not strictly formatted like the Showdown team JSON format, it can be used for adding custom trainers.

## Trainer Battle

### `/trainerbattle`

```
/trainerbattle random
/trainerbattle <trainer>
```
```
cobblemontrainerbattle.trainerbattle.random
cobblemontrainerbattle.trainerbattle.trainer
```

### `/trainerbattleother`

```
/trainerbattleother <player> random
/trainerbattleother <player> <trainer>
```
```
cobblemontrainerbattle.trainerbattleother.random
cobblemontrainerbattle.trainerbattleother.trainer
```

### `/trainerbattleflat`

```
/trainerbattleflat random
/trainerbattleflat <trainer>
```
```
cobblemontrainerbattle.trainerbattleflat.random
cobblemontrainerbattle.trainerbattleflat.trainer
```

### `/trainerbattleflatother`

```
/trainerbattleflatother <player> random
/trainerbattleflatother <player> <trainer>
```
```
cobblemontrainerbattle.trainerbattleflatother.random
cobblemontrainerbattle.trainerbattleflatother.trainer
```

## Group Battle

### `/groupbattle`

```
/groupbattle startsession <group>
/groupbattle stopsession
/groupbattle startbattle
```
```
cobblemontrainerbattle.groupbattle
```

### `/groupbattleflat`

```
/groupbattleflat startsession <group>
/groupbattleflat stopsession
/groupbattleflat startbattle
```
```
cobblemontrainerbattle.groupbattleflat
```

## Battle Factory

[Battle Factory](https://bulbapedia.bulbagarden.net/wiki/Battle_Factory_(Generation_III)) is a mini-game introduced in main Pokémon series where players start battling random trainers with three rental Pokémon. Players get a chance to trade Pokémon with their opponent once obtained victory.

```
/battlefactory startsession
/battlefactory startsession infinite
/battlefactory stopsession
/battlefactory startbattle
/battlefactory showpokemon
/battlefactory rerollpokemon
/battlefactory tradepokemon
/battlefactory tradepokemon <playerslot> <trainerslot>
/battlefactory winningstreak
```
```
cobblemontrainerbattle.battlefactory
```