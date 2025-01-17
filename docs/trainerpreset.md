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
    
  "is_spawning_allowed": true,
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
    
  "forbidden_label": [],
  "forbidden_pokemon": [],
  "forbidden_held_item": [],
  "forbidden_ability": [],
  "forbidden_move": [],
    
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

> For `1.9.x+1.5.2`, only `single` is available

## `battle_ai`

- `random`
- `generation5`
- `strong0`
- `strong1`
- `strong2`
- `strong3`
- `strong4`
- `strong5`

## Commands

- Multiple commands are supported
- Commands are run as server
- `%player%` placeholder is available