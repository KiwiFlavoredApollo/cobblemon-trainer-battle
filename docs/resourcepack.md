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

Cobblemon provides three default sound event keys.

```
cobblemon:battle.pvw.default
cobblemon:battle.pvp.default
cobblemon:battle.pvn.default
```

Cobblemon Trainer Battle provides custom sound event keys.

> Custom sound events keys are not available for `serveronly` version

```
cobblemontrainerbattle:battle.default.disc_1
cobblemontrainerbattle:battle.default.disc_2
cobblemontrainerbattle:battle.default.disc_3

cobblemontrainerbattle:battle.gym_leader.disc_1
cobblemontrainerbattle:battle.gym_leader.disc_2
cobblemontrainerbattle:battle.gym_leader.disc_3

cobblemontrainerbattle:battle.elite_four.disc_1
cobblemontrainerbattle:battle.elite_four.disc_2
cobblemontrainerbattle:battle.elite_four.disc_3

cobblemontrainerbattle:battle.champion.disc_1
cobblemontrainerbattle:battle.champion.disc_2
cobblemontrainerbattle:battle.champion.disc_3
```