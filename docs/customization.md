# Customization

## Introduction

Server admins can create data packs for Cobblemon Trainer Battle

## Data Pack Structure

```
datapacks/
└── your_datapack_name/
    ├── pack.mcmeta
    └── data/
        └── cobblemontrainerbattle/
            ├── trainers/
            │   ├── teams/
            │   │   ├── radicalred/
            │   │   ├── inclementemerald/
            │   │   ├── smogon/
            │   │   └── custom/
            │   │       ├── custom_trainer_1.json
            │   │       └── custom_trainer_2.json
            │   │
            │   └── options/
            │       ├── radicalred/
            │       ├── inclementemerald/
            │       ├── smogon/
            │       ├── custom/
            │       │   ├── custom_trainer_1.json
            │       │   └── custom_trainer_2.json
            │       └── defaults.json
            │
            ├── groups/
            │   ├── custom_group_1.json
            │   └── custom_group_1.json
            │
            ├── minigames/
            │   └── battlefactory.json
            │
            └── loot_tables/
                └── trainers/
                    ├── radicalred/
                    ├── inclementemerald/
                    ├── smogon/
                    ├── custom/
                    │   ├── custom_trainer_1.json
                    │   └── custom_trainer_2.json
                    └── defaults.json
```

## Trainer Team

- Trainer team files follows Showdown team JSON format
- Trainer Pokémon levels can be set relative to player Pokémon maximum level
- Manually writing trainer team files can be quite painful, please consider using `/cobblemontrainerbattle export <player>` command

```json
[
  {
    "name": "",
    "species": "Articuno",
    "gender": "",
    "level": 0,
    "item": "Leftovers",
    "ability": "Pressure",
    "evs": {"hp": 252, "atk": 0, "def": 0, "spa": 252, "spd": 4, "spe": 0},
    "nature": "Modest",
    "ivs": {"hp": 31, "atk": 31, "def": 31, "spa": 30, "spd": 30, "spe": 31},
    "moves": ["Ice Beam", "Hurricane", "Substitute", "Roost"]
  },
  {
    "name": "",
    "species": "Ludicolo",
    "gender": "",
    "level": 0,
    "item": "Life Orb",
    "ability": "Swift Swim",
    "evs": {"hp": 4, "atk": 0, "def": 0, "spa": 252, "spd": 0, "spe": 252},
    "nature": "Modest",
    "moves": ["Surf", "Giga Drain", "Ice Beam", "Rain Dance"]
  }
]
```

### Additional Features

Few attributes can be Minecraft-friendly, and additional attributes are available.

#### Species
```
"species": "Articuno"
```
```
"species": "cobblemon:pikachu"
```

#### Item

```
"item": "Leftovers"
```
```
"item": "minecraft:diamond"
```

#### Nature

```
"nature": "Modest"
```
```
"nature": "cobblemon:jolly"
```

#### Moves

```
"moves": ["Surf", "Giga Drain", "Ice Beam", "Rain Dance"]
```
```
"moves": ["flamecharge", "quickattack", "ember", "scratch"]
``` 

#### Shiny (Additional Attribute)

```
"shiny": true
```
```
"shiny": false
```

#### Form (Additional Attribute)

```
"form": "Galar"
```
```
"form": "Hisui Bias"
```

`form` attribute takes form names. Few other options are `Therian`, `Paldea-Combat` and `Galar-Zen`. You can identify form names from [Cobblemon species file](https://gitlab.com/cable-mc/cobblemon/-/tree/main/common/src/main/resources/data/cobblemon/species?ref_type=heads).

## Trainer Option

- Multiple commands are supported
- Commands are run as server
- `%player%` placeholder can be used to indicate the player in battle
- `battleTheme` can be configured per trainer (>= 1.6.7)
- Configuration can be set globally by modifying `defaults.json`

```json
{
  "isSpawningAllowed": true,
  "condition": {
    "isRematchAllowedAfterVictory": true,
    "maximumPartyLevel": 100,
    "minimumPartyLevel": 1
  },
  "battleTheme": "cobblemon:battle.pvn.default",
  "onVictory": {
    "balance": 0,
    "commands": [
      "give %player% minecraft:diamond"
    ]
  },
  "onDefeat": {
    "balance": 0,
    "commands": []
  }
}
```

## Trainer Group

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

## Battle Factory

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

## Loot Table

Loot tables can be set for each trainer. Trainers without loot tables will drop loots according to `defaults.json`.

```
{
  "type": "minecraft:entity",
  "pools": [
    {
      "rolls": 1,
      "entries": [
        {
          "type": "minecraft:item",
          "name": "cobblemontrainerbattle:trainer_token"
        }
      ],
      "conditions": [
        {
          "condition": "cobblemontrainerbattle:defeated_in_battle"
        },
        {
          "condition": "minecraft:random_chance",
          "chance": 0.5
        }
      ]
    }
  ]
}
```

## Trainer Textures

Gym Leaders, Elite Four and Champion textures (skins) can be overridden via resource pack. Make sure you have the right path. For example, if you want to override Gym Leader Brock's texture, the path should be `your_resource_pack/assets/cobblemontrainerbattle/textures/entity/trainer/slim/leader_brock.png`