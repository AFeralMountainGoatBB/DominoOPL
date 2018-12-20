package bbrence.domino;

import android.util.Log;

import java.util.Vector;

/**
 * Created by raven on 11/4/2017.
 */


public class HumanPlayerClass extends PlayerClass {
    //Class constants

    //Class Variables
    private int Score;
    private String SelectedTile;
    private boolean PassedLastTurn;
    private Vector<tile> PlayerHand;
    private Vector<tile> MoveSet;
    private boolean AlreadyDrawn;
    //Gui Components

    //Constructors
    /**
     function initializes humanclass and initializes all vars
     */
    public HumanPlayerClass()
    {
        this.PlayerHand = new Vector<tile>();
        this.MoveSet = new Vector<tile>();
        this.Score=0;
        this.SelectedTile = "";
        this.PassedLastTurn=false;
    }

    public HumanPlayerClass(int PassedScore)
    {
        this.PlayerHand = new Vector<tile>();
        this.MoveSet = new Vector<tile>();
        this.Score=PassedScore;
        this.PassedLastTurn=false;
    }

    //Selectors

    /**
     * returns true if the player has already drawn a tile this turn
     * @return true if player has drawn a tile this turn, false if not
     */
    final boolean GetDrawnStatus()
    {
        return this.AlreadyDrawn;
    }

    /**
     * returns true if the player has passed their last turn
     * @return true if player passed their last turn, false if not
     */
   final public boolean GetPassed()
    {
        return this.PassedLastTurn;
    }

    /**
     * returns string in private selectedTile
     * @return String stored in SelectedTile
     */
    final public String GetSelectedTile()
    {
        return this.SelectedTile;
    }

    public final int GetScore()
    {
        return this.Score;
    }
    //Mutators

    /**
     * Sets boolean DrawnStatus to true
     */
    public void SetDrawnStatus() {this.AlreadyDrawn=true;}

    /**
     * Sets boolean DrawnStatus to false
     */
    public void ClearDrawnStatus()
    {
        this.AlreadyDrawn=false;
    }

    /**
     * function sets score to passed val
     * @param score - score that players score will become
     */
public void SetScore(int score)
{
    this.Score = score;
}

    /**
     * function adds points to player's score
     * @param points - score that will be added to player score
     */
    public void AddPoints(int points)
    {
        this.Score= this.Score + points;
    }

    /**
     * function plays a tile to the side specified to the board specified
     * @param Board - Board that is active in play
     * @param TileRep - string tile representation that is parsed into a tile and played
     * @param side - side that is being played to
     * @param OpponentPassed - bool if opponent passed their turn
     */
    public boolean PlayTile(BoardClass Board, String TileRep,  int side, boolean OpponentPassed)
    {
        tile tile = this.FindTile(TileRep);
        if (tile==null) {
            return false;
        }

        if (side == 1) {
            if (!tile.GetDouble() && !OpponentPassed) {
                //any of these statements need to be true in order for the player to attempt to play on opponents side
                return false;
            }
        }
            if (Board.PlayTile(tile, side)) {
                this.RemoveTile(tile);
                this.PassTurnSet(false);
                this.EraseMoves();
                this.ClearDrawnStatus();
                return true;
            }
        //failure, so must be unplayable on the side chosen
        return false;
    }

    /**
     * Accesses the Moveset commands to check if the player has any moves available to them
     * @param board -> the board that the are checked against
     * @param OpponentPassed -> if their opponent passed this will be true, false if otherwise,
     * important to generate accurate possible moves
     * @return if moves have been generated successfully, return true, if no moves found return false
     */
    public boolean CanPlay(BoardClass board, boolean OpponentPassed)
    {
        if (this.FindOptions(board, OpponentPassed))
        {
            this.EraseMoves();
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Modifies the data member MoveSet in order to find available moves, returns true if there are moves
     * @param board -> the board that the moves would be mapped to, function accesses the
     * left hand side and right hand side of the board to make its moves
     * @param OpponentPassed -> if their opponent passed this will be true, false if otherwise,
     * important to generate accurate possible moves
     * @return if moves have been generated successfully, return true, if no moves found return false
     */
    public boolean FindOptions(BoardClass board, boolean OpponentPassed)
    {
        Log.i("HumanFindOptions", "Starting FindOptions!");
//stores the left hand side and rhs of field, to limit function calls
        int BoardLeft = board.GetLHS();
        int BoardRight = board.GetRHS();

        //   Log.i("CompFindOptions", "Board is ");
        //iterate through hand, checking rhs vs lhs of field supplied, setting tile LHS and RHS bools to true if playable
        // uses info to see if last player passed to generate accurate possible moves
        // then stores all tiles with LHS or RHS true to moves

        //now for other side
        for (tile x : this.GetHand()) { //sets x to be iterator
            Log.i("HumanFindOptions", "Checking tile " + x.GetTileRepresentation());
            // checks first to see what sides it can play on, this being true means it can play both sides
            if (OpponentPassed || x.GetDouble())
            {
                Log.i("HumanFindOptions", "OpponentPassed or x is double");
                //can this tile be played on this left side?
                if (x.GetFirst() == BoardLeft || x.GetSecond() == BoardLeft)
                {
                    //setting true to tile being able to be played on LHS, will be stored in final step
                    Log.i("HumanFindOptions", "LHS tile added");
                    x.SetLHS( true);
                }
                if (x.GetFirst() == BoardRight || x.GetSecond() == BoardRight)
                {
                    Log.i("HumanFindOptions", "RHS tile added");
                    x.SetRHS(true); //set rhs of tile to true to be passed to moves in last step
                }
            }
            else // this means the player can only play its own side
            {
                if (x.GetFirst() == BoardLeft || x.GetSecond()== BoardLeft) //can this tile be played on this side?
                {
                    Log.i("HumanFindOptions", "LHS tile added");
                    x.SetLHS(true); //setting true to tile being able to be played on LHS, will be stored in final step
                }
            }
        } //end of iteration

        //build moveset for 'AI' to evaluate
        for (tile x : this.GetHand()) // x to be iterator
        {
            // checks each tile in hand to see if it is possible to be played on either side this turn
            if (x.GetLHS() || x.GetRHS())
            {
                // the tile can be played, so a copy is added to moveset as a possible move
                Log.i("HumanFindOptions", "Human added to moveset");
                this.MoveSet.add(x);
            }
        } // end moveset generation
        if (this.MoveSet.isEmpty())
        { Log.i("HumanFindOptions", "No moves generated");
            return false;
        }
        //if no legal moves found, then this will be empty, and return false so we can pass the turn
        //cout << "Done building options" << endl;
        Log.i("HumanFindOptions", "returning true");
        // if moveset.isempty()==false then there are possible moves, function returns true
        return true; }

    /**
     * function parse hand from string passed to it
     * @param HandRep -> the string representation of the hand that is parsed and loaded into hand
     */
    public void LoadHand(String HandRep)
    {
        //get sets of 3 tiles off every time, translate them to tiles and add them to layout, then deletes them from string
        HandRep=HandRep.replace(" ", "");
        while (!HandRep.isEmpty())
        {
            int index = HandRep.indexOf("-");
          //  Log.i("HumanLoadHand" , HandRep);
            String TileRep = HandRep.substring(index-1, index+2);
            HandRep=HandRep.replace(TileRep, "");
            this.AddTile(TileRep);
        }
    }

    //Any utility (private) methods

    /**
     * Sets the private boolean PassedLastTurn to the value passed to it,
     * representation if player passed their last turn
     * @param ValueSet -> value PassedLastTurn will be set to
     */
    public void PassTurnSet(boolean ValueSet)
    {
        this.PassedLastTurn=ValueSet;
    }

    /**
     * function sets the tile value to the tileRep passed to it
     * @param TileRep - what the tile is passed to
     */
    public void SetSelectedTile(String TileRep)
    {
        this.SelectedTile = TileRep;
    }

    /**
     * function clears tile value (sets to null)
     */
    public void ClearSelectedTile()
    {
        this.SelectedTile=null;
    }

    /**
     * function clears moveset and lhs / rhs values (sets to false)
     */
    private void EraseMoves()
    {
        this.MoveSet.removeAllElements();
        this.ResetRHSLHS();
        Log.i("EraseMoves", "Moves Erased");
    }

    /**
     * function clears lhs / rhs values in hand(sets to false)
     */
    private void ResetRHSLHS() {
        for (tile x : this.GetHand()) {
            x.SetRHS(false);
            x.SetLHS(false);
        }
    }


    /**
     function decides what tile, if any, they want to play on opponents side from moveset
     @return the tile the computer suggests playing first on this side
     */
    public tile OpponentSideChooseOption() {
        tile temp = new tile(-2, -2);

        for (tile x : this.MoveSet) {
            Log.i("HumOppSidechooseOption" , "This " + x.GetTileRepresentation() + "is in moveset");
            if (x.GetDouble() && x.GetRHS()) {
                Log.i("Human ChooseOption", "Human is playing " + x.GetTileRepresentation() +
                        " on the Right hand side as it is a double and the thinks you should make them draw again.");
                return x;
            }
        }
        for (tile x : this.MoveSet) {
            if (x.GetRHS()) {
                //this will find the highest value tile since temp is initalized to -2, -2
                if ((temp.GetFirst() + temp.GetSecond()) < (x.GetFirst() + x.GetSecond())) {
                    temp = x;
                }
            }
        }
        if (temp.GetFirst() + temp.GetSecond() >= 0) {
            Log.i("Human ChooseOption", "Human is playing " + temp.GetTileRepresentation() +
                    " on the RHS as it is the highest " +
                    "value tile that you can play.");
            //returning highest value tile playable with lhs value
            return temp;
        }
        temp=null;
        Log.i("HumOppSideChooseOption" ,"Something went wrong deciding what tile to play " +
                "for human with playerpassed==false");
        return temp;
    }

    /**
     function decides what tile, if any, they want to play on sameside from moveset
     @return tile the computer suggests playing on this side
     */
    public tile SameSideChooseOption() {
        tile temp = new tile(-2, -2);

        //part 1 checking for high value tiles on own side
        for (tile x: this.MoveSet) {
            if (x.GetLHS()) {Log.i("HumSameSideOp", x.GetTileRepresentation() + " IS LHS");}
            if (x.GetDouble()) {Log.i("HumSameSideOp", x.GetTileRepresentation() + " IS Double");}
            if (!x.GetDouble()) {Log.i("HumSameSideOp", x.GetTileRepresentation() + " IS not double");}
            Log.i("HumSameSideChooseOpp" , "This " + x.GetTileRepresentation() + " is in the moveset");
            //this will find the highest value tile since temp is initalized to -1,0
            if (x.GetLHS() && !x.GetDouble()) {
                Log.i("HumSameSideOpp", x.GetTileRepresentation());
                int A = temp.GetFirst() + temp.GetSecond();
                int B = x.GetFirst() + x.GetSecond();
                Log.i("HumSameSideOpp", "Is " + Integer.toString(A) + " < " + Integer.toString(B) + " ? " );
                if (A < B) {
                    temp = x;
                }
            }
        }
        // checks to see if not inital value then plays item, if the value is <0 that means
        // no tiles were found
        if (temp.GetFirst() + temp.GetSecond() >= 0) {
            Log.i("HumanSameSChooseOption",
                    "Human is playing " + temp.GetTileRepresentation() +
                            " on the left hand side as it is the highest value " +
                            "tile the human can play.");
            return temp;
        }
        //part 3 checking for doubles playable on own side
        for (tile x : MoveSet) {
            if (x.GetDouble() && x.GetLHS()) {
                Log.i("HumanSameChooseOption", "Human is advised to play " + x.GetTileRepresentation() +
                        " on the LHS hand side as it is a double and " +
                        "you should make the computer draw again.");
                return x;
            }
        }
        //if no playable tiles, return temp value set to null
        Log.i("HumSameSideChooseOp", "Something has went wrong");
        temp=null;
        return temp;
    }

    /**
     function decides using computer player logic what tile to suggest to player
     @return string tile of what action the computer logic suggests performing to player
     */
    public String Help(BoardClass board, boolean OpponentPassed)
    {
        tile tile;
        String TileRep;
        String Suggestion="";
        Log.i("HumanHelp", "Starting help function");

        if (this.FindOptions(board, OpponentPassed)==false){
            Suggestion="The computer suggests you try passing your turn";
            return Suggestion;
        }
         else
        {
            if (!OpponentPassed) {
            if (this.SameSideChooseOption() != null) {
                tile = this.SameSideChooseOption();
                TileRep = tile.GetTileRepresentation();
                if (!tile.GetDouble())
                {
                    Suggestion = "The computer thinks you should play " + TileRep + " On your side \n" +
                            "because it is your highest value tile and this will minimize your hand value";
                }
                if (tile.GetDouble())
                {
                    Suggestion = "The computer thinks you should play " + TileRep + " On your side \n" +
                            "because it is your only tile you can play on your side";
                }
                this.EraseMoves();
                return Suggestion;
            }
            if (this.OpponentSideChooseOption() != null) {
                tile = this.OpponentSideChooseOption();
                TileRep = tile.GetTileRepresentation();
                Suggestion = "The computer thinks you should play " +TileRep + " On their side \n " +
                        "because it is the only tile you can play";
                this.EraseMoves();
                return Suggestion;
            }
        }

        if (OpponentPassed) {
            //try to play double on opponents side, then HVT on own side
            if (OpponentSideChooseOption() != null && OpponentSideChooseOption().GetDouble()) {
                tile = OpponentSideChooseOption();
                TileRep = tile.GetTileRepresentation();
                Suggestion = "The computer thinks you should play " +TileRep + " On their side \n " +
                        "because they will have to draw again and you get rid of a double tile";
                this.EraseMoves();
                return Suggestion;

            } else if (SameSideChooseOption() != null) {
                tile = SameSideChooseOption();
                TileRep = tile.GetTileRepresentation();
                if (tile.GetDouble())
                {
                    Suggestion = "The computer thinks you should play " +TileRep + " On your side \n " +
                            "because its the only tile you have to play on your side";
                }
                if (!tile.GetDouble())
                {
                    Suggestion = "The computer thinks you should play " +TileRep + " On your side \n " +
                            "because its the highest value tile you have to play on your side, which will lessen your hand value";
                }

                this.EraseMoves();
                return Suggestion;

            } else if (OpponentSideChooseOption() != null) {
                tile = OpponentSideChooseOption();
                TileRep = tile.GetTileRepresentation();
                Suggestion = "The computer thinks you should play " +TileRep + " On their side \n " +
                        "because this is your highest value tile you can play on their side";
                this.EraseMoves();
                return Suggestion;
            }
            }
        }
        return Suggestion;
    }
}
