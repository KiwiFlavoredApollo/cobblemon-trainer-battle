# README

## Trainer Battle

### 1. `/trainerbattle`

- Initiates Pokemon battle with trainers
- Players will use Pokemons in their parties
- Trainer Pokemon levels may differ depending on their configuration

```
/trainerbattle radicalred:<trainer>
/trainerbattle inclementemerald:<trainer>
/trainerbattle custom:<trainer>
/trainerbattle random
```

### 2. `/trainerbattleflat`

- Initiates Pokemon battle with trainers
- Players will use Pokemons in their parties
- Pokemon levels are set to 100 for both players and trainers

```
/trainerbattleflat radicalred:<trainer>
/trainerbattleflat inclementemerald:<trainer>
/trainerbattleflat custom:<trainer>
/trainerbattleflat random
```

## Battle Frontier

- Mini-game for players who wants to test their luck and skills
- Three random Pokemons are given to players
- Players can trade Pokemons with trainers once obtained victory

### 1. `/battlefrontier start`

Creates battle frontier session

### 2. `/battlefrontier stop`

Removes battle frontier session

### 3. `/battlefrontier pokemons`

Shows Pokemons

### 4. `/battlefrontier reroll`

Re-rolls Pokemons

### 5. `/battlefrontier trade`

`/battlefrontier trade`

Shows Pokemons available for trade

`/battlefrontier trade <playerslot> <trainerslot>`

Trades Pokemons with trainers

## Configuration

```
"economy": "None"
```

### Available `economy` Options

- `None`
- `OctoEconomy`

## Custom Trainers

- Trainer files can be added via data packs
- Custom trainer files must be located under `<data pack>/data/cobblemontrainerbattle/custom`
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
- `<data pack>/data/cobblemontrainerbattle/configuration/<group>/<trainer>.json`
- Multiple commands are supported
- Configuration can be set globally by modifying `defaults.json`
- `<data pack>/data/cobblemontrainerbattle/configuration/defaults.json`
- Commands are run as server
- `%player%` placeholder can be used to indicate player

## TODOs

- [ ] Fix bug SimpleTM TM/TR drop
- [ ] Add feature defining Battle Frontier trainer sequence by data pack
- [ ] Refactor TrainerFileParser.createPokemon using JsonObject.has
- [ ] When on Battle Frontier battle, player Pokemons are slightly damaged
- [ ] Add subcommand for `/battlefrontier` showing winning streak

## Reference
- [Pokemon Showdown Team Formats - Cobblemon Showdown](https://gitlab.com/cable-mc/cobblemon-showdown/-/blob/master/sim/TEAMS.md#packed-format)
- [Cobblemon - Gitlab](https://gitlab.com/cable-mc/cobblemon)
- [Cobblemon Challenge - Github](https://github.com/TurtleHoarder/Cobblemon-Challenge)
- [CobblemonTrainers - Github](https://github.com/davo899/CobblemonTrainers/tree/main)