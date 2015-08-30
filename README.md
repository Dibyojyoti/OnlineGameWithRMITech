## OnlineGameWithRMITech

Dibyojyoti Sanyal, Pranay Sarkar, Anirban Chatterjee

Multi user  game application Distributed Systems  Project in Winter 2014-15 semester at Technische Universität Darmstadt.

*Group Members*

1. Dibyojyoti Sanyal (https://github.com/Dibyojyoti) (@Dibyojyoti)
2. Anirban Chatterjee (https://github.com/anirban99) (@anirban99)
3. Pranay Sarkar (https://github.com/pranay22) (@pranay22)

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

*How to execute*

Steps to run the game

1. Get the local machine IP address. You can also see the local IP address which will be displayed after the server is run. 
2. Set the IP address in arg value(e.g. arg value="127.0.1.1") in the Build.xml
3. Run the Build.xml
4. Input Player name to play the game. Two players should not have the same name else the second player with the same name will not be able to login.


Note:
1. If you want to run the Build.xml again, stop the rmi registry which is running
