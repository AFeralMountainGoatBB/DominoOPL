package bbrence.domino;

import android.app.Activity;
import android.service.quicksettings.Tile;
import android.util.Log;

import java.util.Vector;

/**
 * Created by raven on 11/4/2017.
 */

public class ComputerPlayerClass extends PlayerClass {

    //Class constants

    //Class Variables
    private Vector<tile> MoveSet;
    private Vector<tile> PlayerHand;
    private int Score;
    private boolean PassedLastTurn;


    //Gui Components

    //Constructors
    /**
     function constructs computerplayer, initializes variables and sets base values up
     */
    ComputerPlayerClass()
    {
        this.PlayerHand = new Vector<tile>();
        this.MoveSet = new Vector<tile>();
        this.Score=0;
        this.PassedLastTurn=false;
    }

    //Selectors
    /**
     function gets moveset of computerPlayer
     */
    public Vector<tile> GetMoveSet()
    {
        return this.MoveSet;
    }

    /**
     function gets passed value of computer player, (whether or not they passed their last turn)
     */
    public boolean GetPassed()
    {
        return this.PassedLastTurn;
    }

    //Mutators

    /**
     function gets score value of player
     */
    public final int GetScore()
    {
        return this.Score;
    }

    /**
     function controls Computer's turn, calls decision making processes and plays tile returned by them
     @param board - board that is being played to
     @param OpponentPassed - if the opponent has passed
     @param boneyard - boneyard that is in play
     @param caller - activity that called this
     @param Round - active round class
     */
    public String ComputerTurn(BoardClass board, boolean OpponentPassed, BoneyardClass boneyard, RoundClass Round, Activity caller) {
        Log.i("ComputerTurn", "Starting ComputerTurn");
        String ReturnValue="";
        String TileRep;
        String Reason="";
        if (board.GetBoard().isEmpty())
        {
            Log.i("ComputerTurn", "Computer must place engine");
            this.PlayEngine(board, Round);
            ReturnValue = caller.getResources().getString(R.string.CompTurnREngine);
            ReturnValue +=Reason;
            return ReturnValue;
        }

        //no options to play?
        if (this.FindOptions(board, OpponentPassed)==false)
        {
            //if a tile was drawn successfully
            if (this.DrawTile(boneyard)) {
                if (!FindOptions(board, OpponentPassed)) {
                    //if new tile not playable then passes turn
                    Log.i("ComputerTurn", "New tile drawn but not playable, passing turn");
                    this.PassTurn();
                    TileRep = this.GetHand().lastElement().GetTileRepresentation();
                    ReturnValue=caller.getResources().getString(R.string.CompTurnRPassed, TileRep);
                    return ReturnValue;
                } else {
                    Log.i("CompTurn" , "drawn tile was playable");
                    this.ViewMoveset();
                    // if it is playable then plays the tile
                    if (this.SameSideChooseOption() != null) {
                        TileRep=this.GetHand().lastElement().GetTileRepresentation();
                        ReturnValue=caller.getResources().getString(R.string.CompTurnRDrewPlayed, TileRep, "Right Hand side");
                        Reason = " \n (it is the only tile it can play after drawing)";
                        this.PlayTile(board, SameSideChooseOption(), 1, OpponentPassed);
                        this.PassTurnSet(false);
                        this.EraseMoves();
                        ReturnValue+=Reason;
                        return ReturnValue;
                    }
                    if (this.OpponentSideChooseOption() != null) {
                        TileRep=this.GetHand().lastElement().GetTileRepresentation();
                        this.PlayTile(board, OpponentSideChooseOption(), 0, OpponentPassed);
                        this.PassTurnSet(false);
                        ReturnValue=caller.getResources().getString(R.string.CompTurnRDrewPlayed, TileRep, "Left Hand Side");
                        Reason = " \n (it is the only tile it can play after drawing)";
                        this.EraseMoves();
                        ReturnValue+=Reason;
                        return ReturnValue;
                    }
                }
            } else //if the boneyard was empty
            {
                this.PassTurn();
                ReturnValue = caller.getResources().getString(R.string.CompTurnRNoTiles);
                Log.i("CompTurn", " There was nothing to draw, computer is passing");
                return ReturnValue;
            }
        } // end of no options to start with
        // it was true, so computer has playable moves
        else {
            this.ViewMoveset();
            if (!OpponentPassed) {
                if (this.SameSideChooseOption() != null) {
                    TileRep = SameSideChooseOption().GetTileRepresentation();
                    this.PlayTile(board, SameSideChooseOption(), 1, OpponentPassed);
                    this.PassTurnSet(false);
                    Reason = "\n (it is the highest value tile playable on its own side, limiting its hand's value)";
                    ReturnValue = caller.getResources().getString(R.string.CompTurnRPlayedTile, TileRep, "Right Hand Side");
                    ReturnValue+=Reason;
                    this.EraseMoves();
                    return ReturnValue;
                }
                if (this.OpponentSideChooseOption() != null) {
                    TileRep = OpponentSideChooseOption().GetTileRepresentation();
                    this.PlayTile(board, OpponentSideChooseOption(), 0, OpponentPassed);
                    this.PassTurnSet(false);
                    Reason = " \n (it is the only tile it can play)";
                    ReturnValue=caller.getResources().getString(R.string.CompTurnRPlayedTile, TileRep, "LeftHandSide");
                    ReturnValue+=Reason;
                    this.EraseMoves();
                    return ReturnValue;
                }
            }

            if (OpponentPassed) {
                //try to play double on opponents side, then HVT on own side
                if (OpponentSideChooseOption() != null && OpponentSideChooseOption().GetDouble()) {
                    TileRep = OpponentSideChooseOption().GetTileRepresentation();
                    this.PlayTile(board, OpponentSideChooseOption(), 0, OpponentPassed);
                    this.PassTurnSet(false);
                    this.EraseMoves();
                    ReturnValue=caller.getResources().getString(R.string.CompTurnRPlayedTile, TileRep, "Left Hand Side");
                    Reason = "\n (it is a double and wants you to draw again)";
                    ReturnValue+=Reason;
                    return ReturnValue;

                } else if (SameSideChooseOption() != null) {
                    TileRep = SameSideChooseOption().GetTileRepresentation();
                    this.PlayTile(board, SameSideChooseOption(), 1, OpponentPassed);
                    this.PassTurnSet(false);
                    this.EraseMoves();
                    ReturnValue=caller.getResources().getString(R.string.CompTurnRPlayedTile, TileRep, "Right Hand Side");
                    Reason = "\n (it is the highest value tile or double playable on its own side, limiting its hand's value)";
                    ReturnValue+=Reason;
                    return ReturnValue;

                } else if (OpponentSideChooseOption() != null) {
                    TileRep = OpponentSideChooseOption().GetTileRepresentation();
                    this.PlayTile(board, OpponentSideChooseOption(), 0, OpponentPassed);
                    this.PassTurnSet(false);
                    this.EraseMoves();
                    ReturnValue=caller.getResources().getString(R.string.CompTurnRPlayedTile, TileRep, "Left Hand Side");
                    Reason = "\n (it is the highest value tile playable on opponents side)";
                    ReturnValue+=Reason;
                    return ReturnValue;
                }
            }
        }
        return "ComputerTurn Failed";
    }

    /**
     function plays the engine of the round
     @param board - board being played to
     @param Round - roundclass that is active
     */
    public void PlayEngine(BoardClass board, RoundClass Round)
    {
        //get engine number then playtile
        int EngineInt = Round.GetEngine();
        String EngineRep= Integer.toString(EngineInt) + "-" + Integer.toString(EngineInt);
        tile tile =this.FindTile(EngineRep);
        board.PlayTile(tile, 1);
        this.RemoveTile(tile);
    }

    /**
     function plays tile passed to it
     @param board - board being played to
     @param tile - tile being played
     @param side -side of board being played to
     @param OpponentPassed - whether or not opponent passed
     */
    public boolean PlayTile(BoardClass board, tile tile, int side, boolean OpponentPassed ) {
        if (side == 0) {
            if (tile.GetDouble() || OpponentPassed) {
                if (board.PlayTile(tile, side)) {
                    this.RemoveTile(tile);
                    return true;
                }
            }
            //failure, so must be unplayable on the side chosen
            return false;
        }
        if (board.PlayTile(tile, side)) {
            this.RemoveTile(tile);
            return true;
        }
        return false;
    }

    /**
     function plays string representation of tile passed to it
     @param board - board being played to
     @param TileRep - string representation of tile being played
     @param side -side of board being played to
     @param OpponentPassed - whether or not opponent passed
     */
    public boolean PlayTile(BoardClass board, String TileRep, int side, boolean OpponentPassed ) {
        tile tile = this.FindTile(TileRep);
        if (side == 0) {
            if (tile.GetDouble() || OpponentPassed) {
                if (board.PlayTile(tile, side)) {
                    this.RemoveTile(tile);
                    return true;
                }
            }
            //failure, so must be unplayable on the side chosen
            return false;
        }
            if (board.PlayTile(tile, side)) {
                this.RemoveTile(tile);
                return true;
            }
        return false;
    }

    /**
     function decides what tile, if any, they want to play on opponents side from moveset
     @return the tile the computer would play on this side
     */
    public tile OpponentSideChooseOption() {
        tile temp = new tile(-2, -2);

            for (tile x : this.MoveSet) {
                Log.i("CompOppSidechooseOption" , "This " + x.GetTileRepresentation() + "is in moveset");
                if (x.GetDouble() && x.GetLHS()) {
                    Log.i("Computer ChooseOption", "Computer is playing " + x.GetTileRepresentation() +
                            " on the left hand side as it is a double and the computer wants you to draw again.");
                    return x;
                }
            }
            for (tile x : this.MoveSet) {
                if (x.GetLHS()) {
                    //this will find the highest value tile since temp is initalized to -2, -2
                    if ((temp.GetFirst() + temp.GetSecond()) < (x.GetFirst() + x.GetSecond())) {
                        temp = x;
                    }
                }
            }
            if (temp.GetFirst() + temp.GetSecond() >= 0) {
                Log.i("Computer ChooseOption", "Computer is playing " + temp.GetTileRepresentation() +
                        " on the right hand side as it is the highest " +
                        "value tile the computer can play.");
                //returning highest value tile playable with lhs value
                return temp;
            }
            temp=null;
            Log.i("CompOppSideChooseOption" ,"Something went wrong deciding what tile to play " +
                "for computer with playerpassed==false");
        return temp;

    }

    /**
     function decides what tile, if any, they want to play on sameside from moveset
     @return the tile the computer would play on this side
     */
    public tile SameSideChooseOption() {
        tile temp = new tile(-2, -2);

            //part 1 checking for high value tiles on own side
                for (tile x: this.MoveSet) {
                    if (x.GetRHS()) {Log.i("CompSameSideOp", x.GetTileRepresentation() + " IS RHS");}
                    if (x.GetDouble()) {Log.i("CompSameSideOp", x.GetTileRepresentation() + " IS Double");}
                    if (!x.GetDouble()) {Log.i("CompSameSideOp", x.GetTileRepresentation() + " IS not double");}
                    Log.i("CompSameSideChooseOpp" , "This " + x.GetTileRepresentation() + " is in the moveset");
                    //this will find the highest value tile since temp is initalized to -1,0
                    if (x.GetRHS() && !x.GetDouble()) {
                        Log.i("CompSameSideOpp", x.GetTileRepresentation());
                        int A = temp.GetFirst() + temp.GetSecond();
                        int B = x.GetFirst() + x.GetSecond();
                        Log.i("CompSameSideOpp", "Is " + Integer.toString(A) + " < " + Integer.toString(B) + " ? " );
                        if (A < B) {
                            temp = x;
                        }
                    }
                }
        // checks to see if not inital value then plays item, if the value is <0 that means
        // no tiles were found
            if (temp.GetFirst() + temp.GetSecond() >= 0) {
                Log.i("CompSameSChooseOption",
                        "Computer is playing " + temp.GetTileRepresentation() +
                                " on the right hand side as it is the highest value " +
                                "tile the computer can play.");
                return temp;
                }
                //part 3 checking for doubles playable on own side
                for (tile x : MoveSet) {
                    if (x.GetDouble() && x.GetRHS()) {
                        Log.i("Computer ChooseOption", "Computer is playing " + x.GetTileRepresentation() +
                                " on the right hand side as it is a double and " +
                                "the computer wants you to draw again.");
                        return x;
                    }
                }
                //if no playable tiles, return temp value set to null
        Log.i("CompSameSideChooseOp", "Something has went wrong");
        temp=null;
        return temp;
    }

    /**
     function finds possible moves of player and stores them in moveset
     @param board - board being played to
     @param OpponentPassed - whether or not opponent passed
     @return true if there are options generated, false if not
     */
    public boolean FindOptions(BoardClass board, boolean OpponentPassed)
    {
        Log.i("CompFindOptions", "Starting FindOptions!");
//stores the left hand side and rhs of field, to limit function calls
        int BoardLeft = board.GetLHS();
        int BoardRight = board.GetRHS();

     //   Log.i("CompFindOptions", "Board is ");
    //iterate through hand, checking rhs vs lhs of field supplied, setting tile LHS and RHS bools to true if playable
    // uses info to see if last player passed to generate accurate possible moves
    // then stores all tiles with LHS or RHS true to moves

    //now for other side
		for (tile x : this.GetHand()) { //sets x to be iterator
            Log.i("CompFindOptions", "Checking tile " + x.GetTileRepresentation());
            // checks first to see what sides it can play on, this being true means it can play both sides
        if (OpponentPassed || x.GetDouble())
        {
            Log.i("CompFindOptions", "OpponentPassed or x is double");
            //can this tile be played on this left side?
            if (x.GetFirst() == BoardLeft || x.GetSecond() == BoardLeft)
            {
                //setting true to tile being able to be played on LHS, will be stored in final step
                Log.i("CompFindOptions", "LHS tile added");
                x.SetLHS( true);
            }
            if (x.GetFirst() == BoardRight ||x.GetSecond() == BoardRight)
            {
                Log.i("CompFindOptions", "RHS tile added");
                x.SetRHS(true); //set rhs of tile to true to be passed to moves in last step
            }
        }
        else // this means the player can only play its own side
        {
            if (x.GetFirst() == BoardRight || x.GetSecond()== BoardRight) //can this tile be played on this side?
            {
                Log.i("CompFindOptions", "RHS tile added");
                x.SetRHS(true); //setting true to tile being able to be played on RHS, will be stored in final step
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
            Log.i("CompFindOptions", "Computer added to moveset");
            this.MoveSet.add(x);
        }
    } // end moveset generation
	if (this.MoveSet.isEmpty())
	{ Log.i("CompFindOptions", "No moves generated");
		    return false;
	}
	//if no legal moves found, then this will be empty, and return false so we can pass the turn
    //cout << "Done building options" << endl;
        Log.i("CompFindOptions", "returning true");
		// if moveset.isempty()==false then there are possible moves, function returns true
        return true; }

    /**
     function reads a string representation of hand and parses it into tiles and loads into hand
     @param HandRep - hand stored as a string, will be parsed and added to hand
     */
    public void LoadHand(String HandRep)
    {
        //get sets of 3 tiles off every time, translate them to tiles and add them to layout, then deletes them from string
        HandRep=HandRep.replace(" ", "");
        while (!HandRep.isEmpty())
        {

            int index = HandRep.indexOf("-");
            String TileRep = HandRep.substring(index-1, index+2);
            HandRep=HandRep.replace(TileRep, "");
            this.AddTile(TileRep);
        }
    }

    /**
     function modifies the score value of this player to passed val
     @param score - passed value that will be scorevalue
     */
    public void SetScore(int score)
    {
        this.Score = score;
    }

    /**
     function modifies the score value of this player to passed val
     @param points- passed value that adds to current score
     */
    public void AddPoints(int points)
    {
        this.Score= this.Score + points;
    }
    //Any utility (private) methods

    /**
     function modifies whether or not the player passed their last turn
     @param ValueSet- passed value that will be set
     */
    public void PassTurnSet(boolean ValueSet)
    {
        this.PassedLastTurn=ValueSet;
    }

    /**
     function modifies whether or not the player passed their last turn, always sets to true
     */
    private void PassTurn()
    {
        this.PassedLastTurn=true;
    }

    /**
     function displays the moveset in Logcat for debugging
     */
    private void ViewMoveset() {
        for (tile x : this.MoveSet)
        {
            Log.i("CompViewMoveSet" , "Tile " + x.GetTileRepresentation() + " Is in moveset");
        }

    }

    /**
     function erases the vector moveset and calls reset RHS and LHS of tiles
     */
    private void EraseMoves()
    {
        this.MoveSet.removeAllElements();
        this.ResetRHSLHS();
        Log.i("EraseMoves", "Moves Erased");
    }

    /**
     function resets all the RHS and LHS to false to reset for finding moves
     */
    private void ResetRHSLHS() {
        for (tile x : this.GetHand()) {
            x.SetRHS(false);
            x.SetLHS(false);
        }
    }

}
