# Resource Pack

## Resource Pack Structure

```
resourcepacks/
└── your_resourcepack_name/
    ├── pack.mcmeta
    └── assets/
        └── cobblemontrainerbattle/
            ├── sounds.json
            ├── sounds/
            │   └── battle/
            │       ├── my_disc_1.ogg
            │       └── my_disc_2.ogg
            │
            └── textures/
                └── entity/
                    └── trainer/
                        └── slim/
                            ├── leader_brock.png
                            └── leader_misty.png
```

## Trainer Textures

- Gym Leaders, Elite Four and Champion textures (skins) can be overridden via resource pack.

## Battle Themes

Cobblemon provides three default sound keys. They must be prefixed with `cobblemon` namespace.

```
cobblemon:battle.pvw.default
cobblemon:battle.pvp.default
cobblemon:battle.pvn.default
```

Cobblemon Trainer Battle provides custom sound keys. They can be prefixed with `cobblemontrainerbattle` namespace, but it is not required.

> Custom sound keys are not available for SERVERONLY version

```
battle.default.disc_1
battle.default.disc_2
battle.default.disc_3

battle.gym_leader.disc_1
battle.gym_leader.disc_2
battle.gym_leader.disc_3

battle.elite_four.disc_1
battle.elite_four.disc_2
battle.elite_four.disc_3

battle.champion.disc_1
battle.champion.disc_2
battle.champion.disc_3
```