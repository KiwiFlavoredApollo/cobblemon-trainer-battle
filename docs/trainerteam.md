# Trainer Team

Trainer team files follow Showdown team JSON format. Additional attributes are available. Few attributes can be set Minecraft-friendly. Manually writing trainer team files can be quite painful, please consider using `/exportpokemon` command to facilitate the process.

## Example

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
    "species": "cobblemon:ninetales",
    "form": "Alola",
    "shiny": true,
    "gender": "",
    "level": 0,
    "item": "cobblemon:life_orb",
    "ability": "Swift Swim",
    "evs": {"hp": 4, "atk": 0, "def": 0, "spa": 252, "spd": 0, "spe": 252},
    "nature": "cobblemon:jolly",
    "moves": ["flamecharge", "quickattack", "ember", "scratch"]
  }
]
```

## Species

### Random Pokemon

```
[
  {
    "species": "cobblemontrainerbattle:random"
  }
]
```

## Relative Level

- You need to set `level_mode` to `relative` in trainer preset file
- Sets the trainer Pokémon level relative to player's maximum party level

### Example 1

Pokémon will have same level as the player's maximum party level.

```
[
  {
    "species": "cobblemon:bulbasaur",
    "level": 0
  }
]
```

### Example 2

Pokémon will have two levels above the player's maximum party level.

```
[
  {
    "species": "cobblemon:bulbasaur",
    "level": 2
  }
]
```

### Example 3

Pokémon will have two levels below the player's maximum party level.

```
[
  {
    "species": "cobblemon:bulbasaur",
    "level": -2
  }
]
```

## Form Names

Few other options for form names are `Hisui`, `Therian`, `Paldea-Combat` and `Galar-Zen`. Form names for a Pokémon can be found in [Cobblemon species file](https://gitlab.com/cable-mc/cobblemon/-/tree/main/common/src/main/resources/data/cobblemon/species?ref_type=heads).

```json
{
  "implemented": true,
  "name": "Arcanine",
  "nationalPokedexNumber": 59,
  "primaryType": "fire",
  "forms": [
    {
      "name": "Hisui",
      "primaryType": "fire",
      "secondaryType": "rock"
    }
  ]
}
```