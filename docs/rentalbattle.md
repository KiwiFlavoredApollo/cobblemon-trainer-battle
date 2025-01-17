# Rental Battle

Rental Battle replaces Battle Factory from the previous versions. It basically let players battle trainers with rental Pokémon. Rental Battles can be initiated with `/rentalbattle <trainer>` and `/rentalbattleother <trainer>`.

## Rules

Rental Battle is 3 on 3 battle. Rental Battle does not start if either player or the trainer does not have three or more Pokémon.  

## Rental Pokemon

Before starting Rental Battle, player must receive rental Pokémon. Rental Pokémon are volatile, and they are removed when player disconnects from the server. Rental Pokémon can also be removed by running `/rentalpokemon clear`.

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
/rentalpokemon showtrade
```

Prints status of tradable Pokémon.