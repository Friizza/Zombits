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
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
