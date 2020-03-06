# Tic Tac Toe
CS 494 / Will Farris

## About
A simple client-server networked Tic Tac Toe game for my CS494 term project. A server thread waits for connections and spawns up to 5 games at a time. The network protocol for this game is defined in PROTOCOl.pdf.


## Installation
The project was created with Intellij, importing from version control and building from within the IDE is the recommended installation method for now


## Usage

To start the server from the command line: `java -jar TicTacToe.jar server <port>`

To start the client from the command line: `java -jar TicTacToe.jar client`
