# Zombits

This is a very simple 2D retro-style zombie survival game.  
This project was created to learn the basics of libGDX while working on my main project, *Spellbound RPG*, therefore it is not a polished release, and there are no plans for future updates.  
  
There is no plot or objective, you just have to survive for as long as possible. Each zombie you kill increases the score by 1 point. The game saves the highes score so you can try to beat it.  
The difficulty of the game increases every 30 seconds, zombies will have more health and more will spawn. Your guns will also deal a bit more damage.    
During the game random ammo boxes will spawn on the map so you don't run out of ammo.  

Sprites are from [WuzzyWizard](https://wuzzywizard.itch.io/).  
Monogram font is from [datagoblin](https://datagoblin.itch.io/).  
Sound effects are from [PixaBay](https://pixabay.com/) and [Freesound.org](https://freesound.org/).  

## Controls

`WASD` to move.  
`Left Click` to shoot.  
`1 & 2` to switch weapons.  
`R` to reload.  
`ESC` to pause.  

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.  
  
Run `/gradle build` to build the project, you will find the jar file in `/lwjgl3/build/libs`