# README

## Significant Updates in 1.3.0

- `battlefrontier` command was renamed to `battlefactory`
- Subcommands became a bit longer but more precise
- `groupbattle` command is added
- Directory where trainer configuration files reside has changed 
  - from `<datapack>/data/cobblemontrainerbattle/configuration` 
  - to `<datapack>/data/cobblemontrainerbattle/trainers`

## Configuration

`config/cobblemontrainerbattle/config.json`

```
"economy": "None"
```

### Available `economy` Options

- `None`
- `OctoEconomy`

## Trainer Battle

### 1. `/trainerbattle`

- Initiates Pokemon battle with trainers
- Players will use Pokemons in their parties
- Trainer Pokemon levels may differ depending on their configuration

```
/trainerbattle radicalred/<trainer>
/trainerbattle inclementemerald/<trainer>
/trainerbattle custom/<trainer>
/trainerbattle random
```

### 2. `/trainerbattleflat`

- Initiates Pokemon battle with trainers
- Players will use Pokemons in their parties
- Pokemon levels are set to 100 for both players and trainers

```
/trainerbattleflat radicalred/<trainer>
/trainerbattleflat inclementemerald/<trainer>
/trainerbattleflat custom/<trainer>
/trainerbattleflat random
```

## Custom Trainers

- Trainer files can be added via data packs
- Custom trainer files must be located under `<datapack>/data/cobblemontrainerbattle/custom`
- Please make sure trainer files(`.json`) are in Smogon Teams JSON format
- Trainer Pokemon levels can be set relative to player Pokemons

### Custom Trainer Configuration (>=1.2.0)

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

- Trainer-specific configuration files reside in separate directory from trainer files
- Trainer-specific configuration file names must be the same as the trainer files
- `<datapack>/data/cobblemontrainerbattle/trainers/<namespace>/<trainer>.json`
- Multiple commands are supported
- Configuration can be set globally by modifying `defaults.json`
- `<datapack>/data/cobblemontrainerbattle/trainers/defaults.json`
- Commands are run as server
- `%player%` placeholder can be used to indicate player

## Group Battle

```
/groupbattle startsession <group>
/groupbattle stopsession
/groupbattle startbattle
```

## Custom Groups

```
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
- `<datapack>/data/cobblemontrainerbattle/groups/<group>.json`

## Battle Factory

- Mini-game for players who wants to test their luck and skills
- Three random Pokemons are given to player
- Player can trade Pokemons with trainers once obtained victory

### 1. `/battlefactory startsession`

Creates battle frontier session

### 2. `/battlefactory stopsession`

Removes battle frontier session

### 3. `/battlefactory showpokemon`

Shows Pokemons

### 4. `/battlefactory rerollpokemon`

Re-rolls Pokemons

### 5. `/battlefactory tradepokemon`

`/battlefactory tradepokemon`

Shows Pokemons available for trade

`/battlefactory tradepokemon <playerslot> <trainerslot>`

Trades Pokemons with trainers

### 6. `/battlefactory winningstreak`

Shows player's winning streak

### Configuration

```
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
- You can add onVictory/onDefeat configuration
- `<datapack>/cobblemontrainerbattle/arcades/battelfactory.json`

## TODOs

- [ ] Fix bug SimpleTM TM/TR drop
- [ ] Add feature defining Battle Frontier trainer sequence by data pack
- [ ] Refactor TrainerFileParser.createPokemon using JsonObject.has
- [ ] When on Battle Frontier battle, player Pokemons are slightly damaged

```
/battletrainer
/battlegroup
```

## Reference
- [Pokemon Showdown Team Formats - Cobblemon Showdown](https://gitlab.com/cable-mc/cobblemon-showdown/-/blob/master/sim/TEAMS.md#packed-format)
- [Cobblemon - Gitlab](https://gitlab.com/cable-mc/cobblemon)
- [Cobblemon Challenge - Github](https://github.com/TurtleHoarder/Cobblemon-Challenge)
- [CobblemonTrainers - Github](https://github.com/davo899/CobblemonTrainers/tree/main)