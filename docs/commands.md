# Commands

## Utility Commands

```
/cobblemontrainerbattle reload
```

Reloads `.minecraft/config/cobblemontrainerbattle/config.json`

```
/cobblemontrainerbattle export <player>
/cobblemontrainerbattle exportflat <player> <level>
/cobblemontrainerbattle exportrelative <player>
```

Exports player Pokémon to `.minecraft/cobblemontrainerbattle/`. While it's not strictly formatted like the Showdown team JSON format, it can be used for adding custom trainers.

## Trainer Battle

```
/trainerbattle random
/trainerbattle <trainer>

/trainerbattleother <player> random
/trainerbattleother <player> <trainer>

/trainerbattleflat random
/trainerbattleflat <trainer>

/trainerbattleflatother <player> random
/trainerbattleflatother <player> <trainer>
```

## Group Battle

```
/groupbattle startsession <group>
/groupbattle stopsession
/groupbattle startbattle

/groupbattleflat startsession <group>
/groupbattleflat stopsession
/groupbattleflat startbattle
```

## Battle Factory

[Battle Factory](https://bulbapedia.bulbagarden.net/wiki/Battle_Factory_(Generation_III)) is a mini-game introduced in main Pokémon series where players start battling random trainers with three rental Pokémon. Players get a chance to trade Pokémon with their opponent once obtained victory.

```
/battlefactory startsession
/battlefactory stopsession
/battlefactory startbattle
/battlefactory showpokemon
/battlefactory rerollpokemon
/battlefactory tradepokemon
/battlefactory tradepokemon <playerslot> <trainerslot>
/battlefactory winningstreak
```