# README

## Configuration

`config/cobblemontrainerbattle/config.json`

```json
{
  "economy": "None",
  "enableTrainerSpawn": true
}
```

```dtd
/cobblemontrainerbattle reload
```

- Reloads configuration at runtime 

### Available `economy` Options

- `None`
- `OctoEconomy`

### NPC Trainers

- If `enableTrainerSpawn` is set to `true`, NPC trainers will spawn around players
- Basically the same as running `trainerbattle random` command but more interesting

## Trainer Battle

```
/trainerbattle <trainer>
/trainerbattle random

/trainerbattleflat <trainer>
/trainerbattleflat random
```

- Initiates Pokemon battle with trainers
- Players will use Pokemons in their parties
- Trainer Pokemon levels may differ depending on their configuration
- For `/trainerbattleflat`, Pokemon levels are set to 100 for both players and trainers

## Group Battle

```
/groupbattle startsession <group>
/groupbattle stopsession
/groupbattle startbattle
```

## Battle Factory

- Mini-game for players who wants to test their luck and skills
- Players must defeat 21 trainers
- Three random Pokemons are given to player
- Player can trade Pokemons with trainers once obtained victory

```
/battlefactory startsession
/battlefactory stopsession
/battlefactory showpokemon
/battlefactory rerollpokemon
/battlefactory tradepokemon
/battlefactory tradepokemon <playerslot> <trainerslot>
/battlefactory winningstreak
```

## NPCs

- Starting from 1.4.0, NPC feature is available
- Random trainers spawn around players in radius of 20 blocks
- Trainers disappear when defeated
- Trainers will fight back when attacked
- NPC trainers will be of Radical Red, Inclement Emerald and custom trainer you provided
- Trainer Spawn Egg is available
- `/summon cobblemontrainerbattle:trainer`

## Customization

### Custom Trainers

- Trainer files can be added via data packs
- Custom trainer files must be located under `<datapack>/data/cobblemontrainerbattle/custom`
- Please make sure trainer files(`.json`) are in Smogon Teams JSON format
- Trainer Pokemon levels can be set relative to player Pokemons

### Custom Trainer Configuration

```dtd
datapacks/
└── your_datapack_name/
    ├── data/
    │   └── cobblemontrainerbattle/
    │       ├── custom/
    │       │   ├── custom_trainer_1.json
    │       │   └── custom_trainer_2.json
    │       ├── radicalred/
    │       ├── inclementemerald/
    │       │
    │       ├── trainers/
    │       │   ├── custom/
    │       │   │   ├── custom_trainer_1.json
    │       │   │   └── custom_trainer_2.json
    │       │   ├── radicalred/
    │       │   ├── inclementemerald/
    │       │   └── defaults.json
    │       │
    │       ├── groups/
    │       │   ├── custom_group_1.json
    │       │   └── custom_group_1.json
    │       │
    │       └── arcades/
    │           └── battlefactory.json
    │
    └── pack.mcmeta
```

```json
{
  "condition": {
    "maximumPartyLevel": 100,
    "minimumPartyLevel": 1
  },
  "onVictory": {
    "balance": 0,
    "commands": []
  },
  "onDefeat": {
    "balance": 0,
    "commands": []
  }
}
```
- Multiple commands are supported
- Configuration can be set globally by modifying `defaults.json`
- Commands are run as server
- `%player%` placeholder can be used to indicate player

### Custom Groups

```json
{
  "trainers": [
    "radicalred/leader_falkner.json",
    "radicalred/leader_bugsy.json",
    "radicalred/leader_whitney.json",
    "radicalred/leader_morty.json",
    "radicalred/leader_chuck.json",
    "radicalred/leader_jasmine.json",
    "radicalred/leader_pryce.json",
    "radicalred/leader_clair.json"
  ],
  "onVictory": {
    "balance": 0,
    "commands": []
  },
  "onDefeat": {
    "balance": 0,
    "commands": []
  }
}
```

- Custom groups can be added with their onVictory/onDefeat behavior
- Basically the same as trainer configuration files

### Custom Battle Factory

```json
{
  "onVictory": {
    "balance": 0,
    "commands": []
  },
  "onDefeat": {
    "balance": 0,
    "commands": []
  }
}
```

### Exporting Player Pokemons

```
/cobblemontrainerbattle export <player>
```

- Exports player Pokemons as compatible trainer file
- While it's not strictly formatted like the Smogon team JSON format, it can be used for custom trainers

## TODOs

- [ ] Fix bug SimpleTM TM/TR drop
- [ ] Add feature defining Battle Frontier trainer sequence by data pack
- [ ] Refactor TrainerFileParser.createPokemon using JsonObject.has
- [ ] When on Battle Frontier battle, player Pokemons are slightly damaged

## Reference
- [Pokemon Showdown Team Formats - Cobblemon Showdown](https://gitlab.com/cable-mc/cobblemon-showdown/-/blob/master/sim/TEAMS.md#packed-format)
- [Cobblemon - Gitlab](https://gitlab.com/cable-mc/cobblemon)
- [Cobblemon Challenge - Github](https://github.com/TurtleHoarder/Cobblemon-Challenge)
- [CobblemonTrainers - Github](https://github.com/davo899/CobblemonTrainers/tree/main)