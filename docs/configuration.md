# Configuration

The file is located at `.minecraft/config/cobblemontrainerbattle/config.json`

```json
{
  "economy": "None",
  "maximumTrainerSpawnCount": 1
}
```

## Available `economy` Options

- `None`
- `OctoEconomy`

## Deprecation Notice

- `OctoEconomy` (and thus economy feature itself) will likely be removed when Cobblemon Trainer Battle is ported to Minecraft 1.21.1 and Cobblemon 1.6
- Starting from 1.6.7, `enableTrainerSpawn` is replaced with `maximumTrainerSpawnCount`. If you want to disable trainer spawning feature, you can set `"maximumTrainerSpawnCount": 0`