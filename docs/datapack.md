# Data Pack

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

## Trainer

> Please refer to [Trainer Team](../trainerteam) for adding custom trainer team files

- Multiple commands are supported
- Commands are run as server
- `%player%` placeholder can be used to indicate the player in battle
- Configuration can be set globally by modifying `defaults.json`
- `battleAI` takes one of following: 
  - `random`
  - `generation5`
  - `strong0` ~ `strong5`

```json
{
  "displayName": "My Trainer",
    
  "battleAI": "generation5",
    
  "isSpawningAllowed": true,
  "isRematchAllowed": true,
    
  "maximumPartySize": 6,
  "minimumPartySize": 1,

  "maximumPartyLevel": 100,
  "minimumPartyLevel": 1,
    
  "requiredLabel": ["legendary", "gen3"],
  "requiredPokemon": [
    {
      "species": "cobblemon:ninetales",
      "form": "Alola"
    }
  ],
  "requiredHeldItem": [
    "minecraft:dirt"
  ],
  "requiredAbility": ["pressure", "static"],
  "requiredMove": ["tackle", "ember"],
    
  "forbiddenLabel": [],
  "forbiddenPokemon": [],
  "forbiddenHeldItem": [],
  "forbiddenAbility": [],
  "forbiddenMove": [],
    
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
  "isRematchAllowed": true,

  "maximumPartySize": 6,
  "minimumPartySize": 1,
    
  "maximumPartyLevel": 100,
  "minimumPartyLevel": 1,
    
  "requiredLabel": [],
  "requiredPokemon": [],
  "requiredHeldItem": [],
  "requiredAbility": [],
  "requiredMove": [],
    
  "forbiddenLabel": [],
  "forbiddenPokemon": [],
  "forbiddenHeldItem": [],
  "forbiddenAbility": [],
  "forbiddenMove": [],
    
  "battleTheme": "battle.gym_leader.disc_1",
    
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
  "battleAI": "generation5",
  "battleTheme": "battle.elite_four.disc_1",
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

- `onVictory` and `onDefeat` will only take effect in normal Battle Factory session.

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
cobblemontrainerbattle:battlefactory_winning_streak
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