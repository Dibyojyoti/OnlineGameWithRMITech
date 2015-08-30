## OnlineGameWithRMITech

Multi user  game application Distributed Systems  Project in Winter 2014-15 semester at Technische Universität Darmstadt.

Group Members

Dibyojyoti Sanyal (https://github.com/Dibyojyoti) (@Dibyojyoti)
Anirban Chatterjee (https://github.com/anirban99) (@anirban99)
Pranay Sarkar (https://github.com/pranay22) (@pranay22)

This is a multi user Fly Hunting Game using Java RMI technology.

The Game is implemented as a Client/Server application based on Java RMI. The game is about hunting a fly with a fly flap. On the GUI a fly randomly appears. By pressing the mouse on top of the fly, the fly was “hunted”. The player who caught the fly first gets a point. Once the fly was hunted it re-appears at a different position. All players see the same fly at the same
position. Also, all players can see the current points of all other players. Currently the game can be plaied by atmost Three 
players.

o The GUI for the client should be Swing based.
o The GUI shows the fly
o The GUI shows a list of all players with their current points scored
o The GUI notifies the player when a fly was hunted
o Used the Model-View-Controller pattern e.g. changes to window size should
not delete the model etc.

*Server:*
- The server sends the current position of the fly to all clients
- The server distributes all changes to all players (points, fly hunted, participants)

*Client:*
- The client sends all changes (one point gained, fly hunted, etc.) to the server.
- The client should load the stubs from the server codebase (file-url should suffice)
