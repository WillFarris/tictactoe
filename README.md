# Tic Tac Toe
CS 494 / Will Farris

## About
A simple client-server networked Tic Tac Toe game for my CS494 term project. A server thread waits for connections and spawns up to 5 games at a time. The network protocol for this game is defined in PROTOCOl.pdf. Grading criteria is found in GRADING_SHEET.pdf.

## Compilation

Using Intellij IDEA:

* Open Intellij and select File -> New -> Project from Version Control
* For the URL, enter: https://github.com/WillFarris/tictactoe
* Build the .jar file from within IntelliJ (Build -> Build Artifacts -> TicTacToe.jar -> Build)

## Usage

The project can be run either directly from the .jar file on disk, or through IntelliJ's build profiles. Once 'build artifacts' has been run, the .jar can be found in `out/artifacts/TicTacToe_jar/TicTacToe.jar`.

From the command line in the root of the project folder, the following commands can be used:

`java -jar out/artifacts/TicTacToe_jar/TicTacToe.jar client` to start the client, or

`java -jar out/artifacts/TicTacToe_jar/TicTacToe.jar server <port>` to start a server on the specified port.

A sort of "demo" run can be achieved through the "Test" build profile in IntelliJ. This profile will start a server and two clients. Note, this version is buggy when dealing with disconnects due to calls to `System.exit()` within different threads, which will terminate the whole program instead of just the thread it's called from.
