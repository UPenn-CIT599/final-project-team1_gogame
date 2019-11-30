# MCIT591FinalProject

Upon running the Go application, users are greeted with a _Welcome to Go_ message, and are able to select from two modes from the primary dropdown, "Play Game" and "Open File".

## Play Game

## Open File
If the user chooses "Open File", they will need to select a .sgf file by clicking the "Select File" button. Two folders of .sgf files have been checked into the repo, which we recommend choosing from. These include
* _Sample Games_ - Several full-length games of Go which may be reviewed. These are to be used with "Replay" mode.
* _Sample Problems from Cho Chikun's Encyclopedia of Life and Death_ - Cho Chikun's collection of 900 elementry Go problems. These are to be used with "Practice Problem" mode.

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