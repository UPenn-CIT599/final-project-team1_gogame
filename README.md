# MCIT591FinalProject

Upon running the Go application, users are greeted with a _Welcome to Go_ message, and are able to select from two modes from the primary dropdown, "Play Game" and "Open File".

## Play Game
If the user chooses "Play Game", they may customize a variety of settings, including number of players, board size, handicap, komi, and timer. Additionally, players may enter their names, and they may choose which color to play or allow their color to be chosen randomly.

Once the user has selected their desired settings, they may press the "Start Game" button to start a game of Go. The game will be played according to the Chinese ruleset. During each player's turn, they may choose to either place a stone on the board, pass, or resign. In the case of a one-player game, the computer player will always wait one second before making its move. Players may also return to the main menu at any time. If a player attempts to place a stone in an illegal location, a message will appear indicating why the given move was illegal.

If the game is timed, each player has a timer, which is displayed on the left side of the board. Each player's timer will count down when it is their turn. When a player's main timer runs out, they will enter byo-yomi phase. Within byo-yomi phase, players are given a specified number of byo-yomi periods. Once a player makes a move in byo-yomi phase, their timer is reset to the byo-yomi period length. Each time a player runs through a full byo-yomi period without moving, one period is deducted from their count; partial periods are never deducted. If a player's timer runs out and they have no byo-yomi periods remaining, that player loses the game. Red text is used to indicate when a player is close to running out of time.

The game ends when either both players pass consecutively, one player resigns, or, in the case of a timed game, one player runs out of time. If the game ends due to resign or time out, the end game menu will be brought up; if the game ends due to both players passing consecutively, the game will enter dead stone selection phase.

During dead stone selection phase, both players must agree on which stones are considered dead. In a one player game, the computer will indicate which stones it believes are dead, while in a two player game, players may select dead stones by clicking on them. Once both players agree on which stones are dead, they may press the "Calculate Score" button to bring up the end game menu, or, if no agreement is reached, they may press the "Continue Play" button to return to the game.

Once the game is over, an end game menu will show the results of the game. From the end game menu, players may press the "Save Replay" button to save a replay of their game as a .sgf file. Once the replay has been saved, it may be viewed by pressing the "View Replay" button, or by using the "Open File" option in the main menu. Players may also press the "Play Again" button to play a new game with the same settings; note that, if the colors were set to be selected randomly, they will be re-randomized before the new game. Finally, players may use the "Main Menu" button to return to the main menu.

## Open File
If the user chooses "Open File", they will need to select a .sgf file by clicking the "Select File" button. Two folders of .sgf files have been checked into the repo, which we recommend choosing from. These include
* _Sample Games_ - Several full-length games of Go which may be reviewed. These are to be used with "Replay" mode.
* _Sample Problems from Cho Chikun's Encyclopedia of Life and Death_ - Cho Chikun's collection of 900 elementary Go problems. These are to be used with "Practice Problem" mode.

### Replay Mode
If a file from the _Sample Games_ folder is selected, or any other .sgf file representing a game of Go, then the "Replay" option should be selected below the prompt _Is the selected file a replay or a practice problem?_ After clicking "Start", a blank Go board will appear, with "Restart", "Next Turn", and "Main Menu" buttons shown at the bottom.

Clicking the "Next Turn" button will show the next move played in the game, as specified in the .sgf file. If the move has a comment associated with it in the .sgf file, it will be displayed at the top of the board. The move number is also displayed at the top of the board.

At any point, the user may make their own move by clicking on any open intersection on the board, which will place a stone of which color has the next move. In this way, users may explore "variations" from the main line of the game. When this is done, a message indicating which move the user is branching off from will display at the top of the board. In addition, the "Next Turn" button changes to say instead "Back To Game". At any point, the user may click the "Back To Game" button to return to the point in the game where they made the branching move.

If the .sgf file was created using our application, and dead stones were selected as part of the scoring process for that game, these dead stones will be highlighted with red dots at the final position of the game.

If the user selects the "Restart" option, they will be prompted with a popup window asking _Are you sure you want to restart?_ By selecting "Yes", the game will be reset to the initial move. If the user selects the "Main Menu", they will be prompted with a popup window asking _Are you sure you want to return to the main menu?_ By selecting "Yes", the application will return to the _Welcome to Go_ page.

### Practice Problem Mode
If a file from the _Sample Problems from Cho Chikun's Encyclopedia of Life and Death_ folder is selected, or any other .sgf file representing a practice problem from the game of Go, then the "Practice Problem" option should be selected below the prompt _Is the selected file a replay or a practice problem?_ After clicking "Start", a Go board will appear with the starting position of the problem displayed, with "Restart" and "Next Turn" buttons shown at the bottom.

Users may play a move, with the goal generally being to find the "best move" in the given position. If the move made by the user is part of the tree of possible moves listed in the .sgf file, then the computer will make the appropriate response to the move, if applicable. If a move has a comment associated with it, this will display at the top of the board. If the user makes a move that is _not_ in the solution tree of the .sgf file, a message will appear indicating that they are off the path of the problem.

If the user selects the "Restart" option, they will be prompted with a popup window asking _Are you sure you want to restart?_ By selecting "Yes", the problem will be reset to the initial position. If the user selects the "Main Menu", they will be prompted with a popup window asking _Are you sure you want to return to the main menu?_ By selecting "Yes", the application will return to the _Welcome to Go_ page.
