# README

## Trainer Battle

### 1. `/trainerbattle`

- Initiates Pokemon battle with trainers
- Players will use Pokemons in their parties
- Trainer Pokemon levels may differ depending on their configuration

```
/trainerbattle radicalred <trainer>
/trainerbattle inclementemerald <trainer>
/trainerbattle custom <trainer>
/trainerbattle random
```

### 2. `/trainerbattleflat`

- Initiates Pokemon battle with trainers
- Players will use Pokemons in their parties
- Pokemon levels are set to 50 for both players and trainers

```
/trainerbattleflat radicalred <trainer>
/trainerbattleflat inclementemerald <trainer>
/trainerbattleflat custom <trainer>
/trainerbattleflat random
```

## Battle Frontier

- Mini-game for players who wants to test their luck and skills
- Three random Pokemons are given to players
- Players can trade Pokemons with trainers once obtain victory

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
"economy": "None",
"vanillaCurrencyItem": "minecraft:diamond",
"victoryCurrencyAmount": 0,
"defeatCurrencyAmount": 0
```

### `economy`

- `None`
- `Vanilla`
- `OctoEconomy`

### `vanillaCurrencyItem`

Effective only when `"economy": "Vanilla"`

### `victoryCurrencyAmount`

Amount of money players receive when they win against trainers

### `defeatCurrencyAmount`

Amount of money players lose when they lose against trainers

## Custom Trainers

- Trainer files can be added via data packs
- Custom trainer files must be located under `<data pack>/data/cobblemontrainerbattle/custom`
- Please make sure trainer files(`.json`) are in Smogon Teams JSON format
- Trainer Pokemon levels can be set relative to player Pokemons

## TODOs

- [ ] Fix bug SimpleTM TM/TR drop

## Reference
- [Pokemon Showdown Team Formats - Cobblemon Showdown](https://gitlab.com/cable-mc/cobblemon-showdown/-/blob/master/sim/TEAMS.md#packed-format)
- [Cobblemon - Gitlab](https://gitlab.com/cable-mc/cobblemon)
- [Cobblemon Challenge - Github](https://github.com/TurtleHoarder/Cobblemon-Challenge)
- [CobblemonTrainers - Github](https://github.com/davo899/CobblemonTrainers/tree/main)