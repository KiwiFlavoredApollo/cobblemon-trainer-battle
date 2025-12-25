# Rental Battle

Rental Battle replaces Battle Factory from the previous versions. It basically let players battle trainers with rental Pokémon. Rental Battles can be initiated with `/rentalbattle <trainer>` and `/rentalbattleother <trainer>`.

## Rental Pokemon

Before starting Rental Battle, player must receive rental Pokémon. Rental Pokémon can also be removed by running `/rentalpokemon clear`.

Trainers with empty team can be used for Rental Battle. Those trainers will receive random Pokémon.

### Random

`/rentalpokemon random` gives player three random Pokémon.

### Clone

`/rentalpokemon clone` clones the first three Pokémon from party. If the player has no three Pokémon, the command fails and does nothing.

### Trade

When players defeat trainers on Rental Battle, they can trade their Pokémon with the defeated trainers.

### Show

```
/rentalpokemon showrental
```

Prints status of rental Pokémon.

```
/rentalpokemon showtradable
```

Prints status of tradable Pokémon.