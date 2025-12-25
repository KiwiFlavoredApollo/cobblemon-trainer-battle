# Data Pack

## Introduction

[Cobblemon Trainer Battle Data Pack](https://github.com/KiwiFlavoredApollo/cobblemon-trainer-battle-data-pack) can be used as a template.

## Data Pack Structure

```
datapacks/
└── your_datapack_name/
    ├── pack.mcmeta
    └── data/
        └── cobblemontrainerbattle/
            ├── trainer_team/
            │   ├── radicalred/
            │   ├── inclementemerald/
            │   ├── smogon/
            │   └── custom/
            │       ├── custom_trainer_1.json
            │       └── custom_trainer_2.json
            │
            ├── trainer_preset/
            │   ├── radicalred/
            │   ├── inclementemerald/
            │   ├── smogon/
            │   └── custom/
            │       ├── custom_trainer_1.json
            │       └── custom_trainer_2.json
            │
            └── loot_tables/
                └── entities/
                    └── trainer.json
```

## Trainer

### Trainer Preset 

> Please refer to [Trainer Preset](../trainerpreset) for more information

### Trainer Team

> Please refer to [Trainer Team](../trainerteam) for more information

## Loot Table

Cobblemon Trainer Battle offers a custom loot condition `cobblemontrainerbattle:defeated_in_battle` where trainers drop loots only when the trainer is defeated in Pokémon battle. Loot tables can be set for each trainer. Trainers without loot tables will drop loots according to `defaults.json`.

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