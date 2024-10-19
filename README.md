# README

## 1.5.0 Changelog

- Trainer teams are stored under `trainers/team/<namespace>`
- Trainer options are stored under `trainers/options/<namespace>`
- Renamed Smogon trainer to be shorter and uniform
- Fixed bug where Pokemon entities drop held items
- Trainer can be selected in `<namespace>:<trainer>`
- `/trainerbattle radicalred:biker_alex`
- Added new trainer condition `isRematchAllowedAfterVictory` 

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
    │       │   │       └── custom_trainer_1.json
    │       │   └── options/
    │       │       ├── radicalred/
    │       │       ├── inclementemerald/
    │       │       ├── smogon/
    │       │       ├── custom/
    │       │       ├── custom/
    │       │       │   ├── custom_trainer_1.json
    │       │       │   └── custom_trainer_1.json
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
- Custom trainer files must be located under `<datapack>/data/cobblemontrainerbattle/trainers/teams/custom`
- Please make sure trainer files(`.json`) are in Smogon Teams JSON format
- Trainer Pokemon levels can be set relative to player Pokemons

### Custom Trainer Option

```json
{
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
    "radicalred:leader_falkner",
    "radicalred:leader_bugsy",
    "radicalred:leader_whitney",
    "radicalred:leader_morty",
    "radicalred:leader_chuck",
    "radicalred:leader_jasmine",
    "radicalred:leader_pryce",
    "radicalred:leader_clair"
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