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

### Defaults

```
cobblemontrainerbattle:battle.leader.default
cobblemontrainerbattle:battle.elite.default
cobblemontrainerbattle:battle.champion.default

cobblemontrainerbattle:battle.kanto.default
cobblemontrainerbattle:battle.johto.default
cobblemontrainerbattle:battle.hoenn.default
cobblemontrainerbattle:battle.sinnoh.default
cobblemontrainerbattle:battle.unova.default
cobblemontrainerbattle:battle.kalos.default
cobblemontrainerbattle:battle.alola.default
cobblemontrainerbattle:battle.galar.default
cobblemontrainerbattle:battle.hisui.default
cobblemontrainerbattle:battle.paldea.default
```

### Kanto and Johto

```
cobblemontrainerbattle:battle.leader.brock
cobblemontrainerbattle:battle.leader.misty
cobblemontrainerbattle:battle.leader.lt_surge
cobblemontrainerbattle:battle.leader.erika
cobblemontrainerbattle:battle.leader.koga
cobblemontrainerbattle:battle.leader.sabrina
cobblemontrainerbattle:battle.leader.blaine
cobblemontrainerbattle:battle.leader.giovanni

cobblemontrainerbattle:battle.leader.falkner
cobblemontrainerbattle:battle.leader.bugsy
cobblemontrainerbattle:battle.leader.whitney
cobblemontrainerbattle:battle.leader.morty
cobblemontrainerbattle:battle.leader.chuck
cobblemontrainerbattle:battle.leader.jasmine
cobblemontrainerbattle:battle.leader.pryce
cobblemontrainerbattle:battle.leader.clair

cobblemontrainerbattle:battle.elite.lorelei
cobblemontrainerbattle:battle.elite.bruno
cobblemontrainerbattle:battle.elite.agatha
cobblemontrainerbattle:battle.elite.lance

cobblemontrainerbattle:battle.champion.terry
```

### Hoenn

```
cobblemontrainerbattle:battle.leader.roxanne
cobblemontrainerbattle:battle.leader.brawly
cobblemontrainerbattle:battle.leader.wattson
cobblemontrainerbattle:battle.leader.flannery
cobblemontrainerbattle:battle.leader.norman
cobblemontrainerbattle:battle.leader.winona
cobblemontrainerbattle:battle.leader.tate_and_liza
cobblemontrainerbattle:battle.leader.juan

cobblemontrainerbattle:battle.elite.sidney
cobblemontrainerbattle:battle.elite.phoebe
cobblemontrainerbattle:battle.elite.glacia
cobblemontrainerbattle:battle.elite.drake

cobblemontrainerbattle:battle.champion.wallace
```

### Sinnoh

```
cobblemontrainerbattle:battle.leader.roark
cobblemontrainerbattle:battle.leader.gardenia
cobblemontrainerbattle:battle.leader.maylene
cobblemontrainerbattle:battle.leader.crasher_wake
cobblemontrainerbattle:battle.leader.fantina
cobblemontrainerbattle:battle.leader.byron
cobblemontrainerbattle:battle.leader.candice
cobblemontrainerbattle:battle.leader.volkner

cobblemontrainerbattle:battle.elite.aaron
cobblemontrainerbattle:battle.elite.bertha
cobblemontrainerbattle:battle.elite.flint
cobblemontrainerbattle:battle.elite.lucian

cobblemontrainerbattle:battle.champion.cynthia
```