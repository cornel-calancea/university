©Corneliu Calancea, 2019

This archive contains the implementation of the project - OOP, 2019

Structure :
- abilities - package that implements all the heroes' superpowers
- angel - contains implementations for all types of Angels(which extend the Angel abstract class) and factory for them
- common - constants
- game - implementation of the game's flow
- hero - implementations for all Heroes(who must extend the Hero abstract class), and factory for them
- input - gets game input from .in file and loads the output to the corresponding .out file
- main
- magician - The Great Mage(Observer)
- map - implementation of the map(Singleton)
- strategies - all the strategies(each of them implements Strategy interface)

Flow :
After getting the input from the file, a Game is created and played round by round :
    - Overtime damages are applied
    - Players choose their strategies
    - The positions of the players are updated
    - All the battles are fought
    - The redundant overtime abilities are removed
    - The angels are deployed
    - Heroes' levels are updated

The Magician observes all the changes - the Game, Angels, and Heroes are the observables.

Interactions :
    - Hero-Ability : A Hero posesses some Abilities and in the battle, each ability "is applied" to the enemy through
    double dispatch(the Hero "endures" the Ability applied to him).
    - Hero-Angel : similar double dispatch mechanism - the player gets blessed by the angel
    - Hero-Strategy : each hero has a field with the strategy applied in the current round, and applies it before the
    battle. The Strategy, thus, adapts the Hero by modifying his HP, XP, and mods
