# Data Pack

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

## Advancement

Cobblemon Trainer Battle offers custom advancement criteria. Please refer to [GitHub repository](https://github.com/KiwiFlavoredApollo/CobblemonTrainerBattle/tree/master/src/main/generated/data/cobblemontrainerbattle/advancements) for specific usages.

```
cobblemontrainerbattle:defeat_trainer
cobblemontrainerbattle:kill_trainer
```

```
{
  "parent": "cobblemontrainerbattle:defeat_elite_drake",
  "criteria": {
    "defeat_champion_wallace": {
      "conditions": {
        "count": 1,
        "trainer": "trainer:entity/champion_wallace"
      },
      "trigger": "cobblemontrainerbattle:defeat_trainer"
    }
  },
  "display": {
    "announce_to_chat": true,
    "background": "minecraft:textures/gui/advancements/backgrounds/adventure.png",
    "description": {
      "translate": "advancement.cobblemontrainerbattle.defeat_champion_wallace.description"
    },
    "frame": "goal",
    "hidden": false,
    "icon": {
      "item": "cobblemontrainerbattle:champion_wallace_token"
    },
    "show_toast": true,
    "title": {
      "translate": "advancement.cobblemontrainerbattle.defeat_champion_wallace.title"
    }
  },
  "requirements": [
    [
      "defeat_champion_wallace"
    ]
  ],
  "sends_telemetry_event": false
}
```