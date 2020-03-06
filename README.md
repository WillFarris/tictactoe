# Tic Tac Toe
CS 494 / Will Farris

## About
A simple client-server networked Tic Tac Toe game for my CS494 term project. A server thread waits for connections and spawns up to 5 games at a time. The network protocol for this game is defined in PROTOCOl.pdf.


## Compilation

Manual (recommended):
* Clone the repository with `git clone https://github.com/WillFarris/tictactoe`
* Open the directory with `cd tictactoe`
* Run `compile.sh` on Unix-based systems (requires a JDK)

Using Intellij IDEA:

* Open Intellij and select File -> New -> Project from Version Control
* For the URL, enter: https://github.com/WillFarris/tictactoe
* Build and run from withing the IDE
