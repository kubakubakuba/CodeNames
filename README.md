# Semestral project for B0B36PJV - CodeNames game in JavaFX

### The aim of the project
The main goal of our semestral project was to implement a working CodeNames game, that could be played on any number of devices, connected to the public internet.

### How it all works 
One instance of the game is running as a server, which other players join to play the game. One player than acts as a lobby host, which has a control of the game (starting and loading necessary game data), the other players then join the lobby and can play the game.

### Running the game
The game can either be compiled and run from source, or you can download the latest release from the releases tab. The game is then run by executing the .jar file (simply clicking on it).
If you want to self-host the server, you need to start the same .jar file with the `-type=server` and `-port=xxxx` argument (port argument is optional). The server instance will then start and you can connect to it from the game.

## More info
All information can be found on [the wiki page of this repository](https://github.com/kubakubakuba/CodeNames/wiki).

## Jakub Pelc, Prokop Jansa, 2023