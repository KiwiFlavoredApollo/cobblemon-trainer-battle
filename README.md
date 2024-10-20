# README

## 1.5.2 Changelog

- Data packs made for earlier versions will NOT be compatible
- Trainer teams are stored under `trainers/teams/`
- Trainer options are stored under `trainers/options/`
- Renamed Smogon trainer to be short and uniform
- Fixed bug where Pokemon entities drop held items
- Trainer selector starts with `trainer:`
- For example, `/trainerbattle trainer:radicalred/biker_alex`
- Added new battle condition `isRematchAllowedAfterVictory`
- Added new trainer option `isSpawningAllowed`
- Removed restrictions on custom trainer team needed to be put into `custom` directory

## Configuration

`config/cobblemontrainerbattle/config.json`

```json
{
  "economy": "None",
  "enableTrainerSpawn": true
}
```

### Available `economy` Options

- `None`
- `OctoEconomy`

```dtd
/cobblemontrainerbattle reload
```

- Reloads configuration at runtime

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
- Players must defeat 7 trainers per round
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

- Random trainers spawn around players in radius of 20 blocks
- Trainers disappear when defeated
- Trainers will fight back when attacked
- Trainer Spawn Egg is available
- `/summon cobblemontrainerbattle:trainer`

## Customization

### Data Pack Structure

```dtd
datapacks/
└── your_datapack_name/
    ├── data/
    │   └── cobblemontrainerbattle/
    │       ├── trainers/
    │       │   ├── teams/
    │       │   │   ├── radicalred/
    │       │   │   ├── inclementemerald/
    │       │   │   ├── smogon/
    │       │   │   └── custom/
    │       │   │       ├── custom_trainer_1.json
    │       │   │       └── custom_trainer_2.json
    │       │   └── options/
    │       │       ├── radicalred/
    │       │       ├── inclementemerald/
    │       │       ├── smogon/
    │       │       ├── custom/
    │       │       │   ├── custom_trainer_1.json
    │       │       │   └── custom_trainer_2.json
    │       │       └── defaults.json
    │       │
    │       ├── groups/
    │       │   ├── custom_group_1.json
    │       │   └── custom_group_1.json
    │       │
    │       └── minigames/
    │           └── battlefactory.json
    │
    └── pack.mcmeta
```

### Custom Trainer Team

- Trainer files can be added via data packs
- Please make sure trainer files(`.json`) are in Smogon Teams JSON format
- Trainer Pokemon levels can be set relative to player Pokemons
- Please refer to example data pack for more information [CobblemonTrainerBattleFC](https://github.com/KiwiFlavoredApollo/CobblemonTrainerBattleFC)

### Custom Trainer Option

```json
{
  "isSpawningAllowed": true,
  "condition": {
    "isRematchAllowedAfterVictory": true,
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

### Custom Trainer Group

```json
{
  "trainers": [
    "trainer:radicalred/leader_falkner",
    "trainer:radicalred/leader_bugsy",
    "trainer:radicalred/leader_whitney",
    "trainer:radicalred/leader_morty",
    "trainer:radicalred/leader_chuck",
    "trainer:radicalred/leader_jasmine",
    "trainer:radicalred/leader_pryce",
    "trainer:radicalred/leader_clair"
  ],
  "condition": {
    "isRematchAllowedAfterVictory": true,
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

### Exporting Player Team

```
/cobblemontrainerbattle export <player>
```

- While it's not strictly formatted like the Smogon team JSON format, it can be used for adding custom trainers 
- `.minecraft/cobblemontrainerbattle/exports/<player>_<timestamp>.json`

## Credits

### Trainer Skins
- [piikapiika](https://www.minecraftskins.com/profile/5894998/piikapiika)
  - White (Hilda) | Pokemon
  - Black (Hilbert) | Pokemon
  - Alola Leaf | Pokemon
  - Leaf | Pokemon
  - Silver | Pokemon
  - Red - Pokemon
  - Blue/Ookido Green | Pokemon

- [idkgraceorsmth](https://www.minecraftskins.com/profile/8183289/idkgraceorsmth)
  - Diner Waitress - Mia
  - Cherry Blossom Garden - Selene
  - Blacksmith - Roxie

## TODOs
- [ ] Fix bug SimpleTM TM/TR drop
- [ ] Add feature defining Battle Frontier trainer sequence by data pack
- [ ] When on Battle Frontier battle, player Pokemons are slightly damaged

## Reference
- [Pokemon Showdown Team Formats - Cobblemon Showdown](https://gitlab.com/cable-mc/cobblemon-showdown/-/blob/master/sim/TEAMS.md#packed-format)
- [Cobblemon - Gitlab](https://gitlab.com/cable-mc/cobblemon)
- [Cobblemon Challenge - Github](https://github.com/TurtleHoarder/Cobblemon-Challenge)
- [CobblemonTrainers - Github](https://github.com/davo899/CobblemonTrainers/tree/main)