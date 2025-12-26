# Trainer Preset

## Introduction

Trainer preset is the most important part of the mod as it allows data pack authors to add custom trainers. Following is an example trainer preset file. `team` is the only required attribute. Others are optional.

```json
{
  "team": "radicalred/leader_misty",
  
  "display_name": "My Trainer",
  "level_mode": "normal",
  "battle_format": "single",
  "battle_ai": "generation5",
  "battle_theme": "cobblemon:battle.pvn.default",
  "cooldown_in_seconds": 0,
  "entity_uuid": "d8f7f037-33fe-4f55-8670-67b1494b7896",
  "texture": "cobblemontrainerbattle:textures/entity/trainer/slim/red_piikapiika.png",
    
  "is_spawn_allowed": true,
  "is_rematch_allowed": true,
    
  "maximum_party_size": 6,
  "minimum_party_size": 1,

  "maximum_party_level": 100,
  "minimum_party_level": 1,
    
  "required_label": ["legendary", "gen3"],
  "required_pokemon": [
    {
      "species": "cobblemon:ninetales",
      "form": "Alola"
    }
  ],
  "required_held_item": [
    "minecraft:dirt"
  ],
  "required_ability": ["pressure", "static"],
  "required_move": ["tackle", "ember"],
  "required_type": [
    ["water", "*"]
  ],
    
  "forbidden_label": [],
  "forbidden_pokemon": [],
  "forbidden_held_item": [],
  "forbidden_ability": [],
  "forbidden_move": [],
  "forbidden_type": [],

  "allowed_label": [],
  "allowed_pokemon": [],
  "allowed_held_item": [],
  "allowed_ability": [],
  "allowed_move": [],
  "allowed_type": [],
    
  "on_victory_commands": [
    "give %player% minecraft:diamond"
  ],
  "on_defeat_commands": [
    
  ]
}
```

## `team`

`team` must indicate a file under `data/cobblemontrainerbattle/trainer_team` without `.json` file extension. For example, if you want to use `data/cobblemontrainerbattle/trainer_team/custom/custom_trainer.json`, it should be set to `custom/custom_trainer`.

```
"team": "custom/custom_trainer",
```

## `level_mode`

- `normal`
- `relative`
- `flat`

## `battle_format`

- `single`
- `double`
- `triple`

> For Cobblemon 1.5.2, only `single` is available

## `battle_ai`

- `random`
- `generation5`
- `strong0`
- `strong1`
- `strong2`
- `strong3`
- `strong4`
- `strong5`

## `entity_uuid`

If not set, both player and trainer Poké Balls are thrown from the player. When entity UUID is assigned to the trainer, trainer Poké Balls are thrown from the trainer entity. It's for improving player experience. NPC mods like EasyNPC is highly recommended. 

## `texture`

If set, the trainer entity spawns with specified texture. You can also use textures provided by other mods and resource packs.

## `is_spawn_allowed`

Replaces `is_spawning_allowed`. For the time being, `is_spawning_allowed` works. However, it is recommended to migrate to `is_spawn_allowed`.

## Required Conditions

At least one Pokémon in the party should satisfy required conditions. For example, if all Pokémon does not have required type, the trainer will refuse to battle.

## Forbidden Conditions

All Pokémon in the party should satisfy forbidden conditions. For example, if any Pokémon have forbidden held item, the trainer will refuse to battle.

## Allowed Conditions

All Pokémon in the party should satisfy allowed conditions. For example, if any Pokémon have moves other than those that are allowed, the trainer will refuse to battle.

## Pokémon Type Condition

It takes lowercase type names like `water`, `fire` and `grass`. `*` and `+` can be used for specifying multiple types.

### Example 1

```
"required_type": [
  ["water"]
]
```

Requires Water single-type Pokémon.

### Example 2

```
"required_type": [
  ["*"]
]

"required_type": [
  ["+"]
]
```

Requires any single-type Pokémon.

### Example 3

```
"required_type": [
  ["water", "grass"]
]
```

Requires Water and Grass dual-type Pokémon.

### Example 4

```
"required_type": [
  ["water", "*"]
]
```

Requires Water single-type or Water dual-type Pokémon.

### Example 5

```
"required_type": [
  ["water", "+"]
]
```

Requires Water dual-type Pokémon.

### Example 6

```
"required_type": [
  ["*", "*"]
]

"required_type": [
  ["+", "+"]
]

"required_type": [
  ["*", "+"]
]
```

Requires any dual-type Pokémon.

## Commands

- Multiple commands are supported
- Commands are run as server
- `%player%` placeholder is available