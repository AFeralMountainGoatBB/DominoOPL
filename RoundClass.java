package bbrence.domino;

import android.app.Activity;
import android.util.Log;

import static bbrence.domino.RoundClass.Players.COMPUTER;

/**
 * Created by raven on 11/4/2017.
 */

public class RoundClass {

    //Class constants
    //enum to represent whose turn it is and who wins the round
    enum Players {USER, COMPUTER, DRAW}

    //Class Variables
    //boolean to determine if the round is loaded from serialization file
    private boolean LoadedRound;
    //keeps track of whose turn it is
    private Players CurrentTurn;
    //determines what engine should be used for this round based on the roundnumber
    private int[] RoundEngineArray;
    //tracks the roundnumber
    private int RoundNumber;
    //stores who won the round using the enum declared earlier in class
    private Players Winner;
    // stores the score of the winning player
    private int WinnerScore;

    //Constructors
    /**
     constructor for creating a new round with no passed information, init variables of the class
     */
    public RoundClass()
    {
        this.RoundNumber=1;
        this.RoundEngineArray = new int[]{0, 6, 5, 4, 3, 2, 1};
        this.CurrentTurn = Players.DRAW;
        this.LoadedRound = false;
        this.Winner=null;
    }

    /**
    constructor for Roundclass with roundnumber passed to it, inits all vars accordingly
     */
    public RoundClass(int round)
    {
        this.RoundNumber = round;
        this.RoundEngineArray = new int[]{0, 6, 5, 4, 3, 2, 1};
        this.LoadedRound = false;
    }

    //Selectors
    /**
     getter for returning the winner of the round
     @return enum val representing the winner (human, computer, or draw)
     */
    public final Players Getwinner()
    {
        return this.Winner;
    }
    /**
     getter for returning the winner's score of the round
     @return int value representing the number of points being awarded to the winner of the round
     */
    public final int GetWinnerScore()
    {
        return this.WinnerScore;
    }

    /**
     getter for returning wether the round is being loaded from serialization file or not
     @return bool value representing wether or not the round is being loaded stored in LoadedRound
     */
    public final boolean GetLoadedRound()
    {
        return this.LoadedRound;
    }

    /**
    getter for finding the current round number
     @return - value that is in RoundNumber - the number round that is being played
     */
    public final int GetRoundNumber()
    {
        return this.RoundNumber;
    }

    /**
     getter to find out whose turn it currently is
     @return - enum val representing whose turn it is
     */
    public final Players CurrentTurn()
    {
        return this.CurrentTurn;
    }

    /**
     method that finds the current engine and returns it
     @return - single digit engine number between 0 and 6
     */
    public final int GetEngine()
    {
        int RoundCounter = this.RoundNumber % 7;
        return this.RoundEngineArray[RoundCounter];
    }
//Mutators

    /**
     setter for setting the value of LoadedRound, the representation if this round was laoded from mem or not
     @param Value - boolean value that will be
     */
    public void SetLoadedRound(boolean Value)
    {
        this.LoadedRound = Value;
    }

    /**
     setter for setting the value of winner, the representation which player won the round
     @param winner- enum value representing winner (USER, COMPUTER, DRAW that will be set as winner)
     */
    public void SetWinner(Players winner)
    {
        this.Winner=winner;
    }

    /**
     setter for setting the value of winner's score, int representation of how many points were won
     @param WinnerScore - int value of how many points are being awarded
     */
    public void SetWinnerScore(int WinnerScore)
    {
        this.WinnerScore=WinnerScore;
    }

    /**
     setter for setting the value of PlayerTurn, the representation of whose turn it is
     @param player - enum value that represents whose turn it will be next
     */
    public void SetTurn(Players player)
    {
        this.CurrentTurn=player;
    }


    /**
     method for the drawphase at beginning of a round, draws 8 tiles into each player's hand
     @param boneyard - boneyard that is being drawn from
     @param Human - human playerclass that is recieving 8 tiles
     @param Computer - computer playerclass that is also recieving 8 tiles
     */
    public void DrawPhase(BoneyardClass boneyard, PlayerClass Human, PlayerClass Computer)
    {
        //draw 8 tiles into each player's hand
        for (int i = 0; i < 8; i++)
        {
            if (boneyard.isEmpty()) {
                Log.i("DrawPhase", "Not enough tiles? Exiting early");
                return;
            }
            Human.DrawTile(boneyard);
            Computer.DrawTile(boneyard);
        }
        return;
    }

    /**
     method checks for the lock condition at the end of each round (both players pass and nobody can play a tile)
     @param board - board class being played to
     @param Human - humanplayer being checked
     @param Computer - Computer player being checked
     @return -true if locked, false if not a locked game
     */
    public boolean CheckLockCondition(BoardClass board, HumanPlayerClass Human, ComputerPlayerClass Computer)
    {
        //we should only bother calling this function if boneyard is already empty
       //check if the players have both passed
        if (Human.GetPassed() && Computer.GetPassed())
        {
            // check to see if the player and computer has any more options
            if (!Human.FindOptions(board, Computer.GetPassed())&& !Computer.FindOptions(board, Computer.GetPassed())) {
                //this means the game is locked and we need to call scoring to end the round and determine who won
                    return true;
            }
        }
        //no game is not locked
        return false;
    }

    /**
    method that sets the round information up and calls scoring function for end of round scoring
     @param Human - humanclass being evaluated and passed to scoring
     @param Computer - computer player that is being passed to scoring
     */
    public void RoundEnd(HumanPlayerClass Human, ComputerPlayerClass Computer)
    {
        this.RoundScoring(Human, Computer);
        this.SetTurn(Players.DRAW);
    }

    /**
     setter for setting the round number to the passed value
     @param roundnum- future roundnumber in int format
     */
    public void SetRoundNumber(int roundnum)
    {
        this.RoundNumber = roundnum;
    }

    /**
     method called at the end of round to determine winner and award points
     @param Human- Human player whose hand is being evaluated
     @param Computer - Computer player whose hand is being evaluated
     */
    public void RoundScoring(HumanPlayerClass Human, ComputerPlayerClass Computer) {
        //tally up totals for human and computer
        int HumanTotal = 0;
        int ComputerTotal = 0;
        //tally human hand
        for (tile x : Human.GetHand()) {
            HumanTotal += x.GetTotal();
        }
        //tally computer hand
        for (tile x : Computer.GetHand()) {
            ComputerTotal += x.GetTotal();
        }
        //check hands to see who has more points, winner has less (even when winner wins by eliminating their hand)
        if (HumanTotal > ComputerTotal)
        {
            Log.i("RoundScoring", "Computer Won");
            Computer.AddPoints(HumanTotal);
            this.SetWinner(COMPUTER);
            this.SetWinnerScore(HumanTotal);
        }
        else if (ComputerTotal > HumanTotal)
        {
            Log.i("RoundScoring", "Human Won");
            Human.AddPoints(ComputerTotal);
            this.SetWinner(Players.USER);
            this.SetWinnerScore(ComputerTotal);
        }
        //draw case
       else if(HumanTotal == ComputerTotal)
        {
            Log.i("RoundScoring" , "Tie, no points awarded");
            this.SetWinner(Players.DRAW);
            this.SetWinnerScore(0);
        }

    }

    /**
     controls computer turn and returns the reason they played what they played
     @param Board - Board being played to
     @param OpponentPassed - bool whether or not opponent passed
     @param boneyard - boneyard active in round
     @param Computer - computer player whose turn is happening
     @param caller - calling activity
     @return - returns string that contains the message the player is sending back,
     which describes what their move is and why they did it
     */
    public String ComputerTurn(BoardClass Board, boolean OpponentPassed, BoneyardClass boneyard, ComputerPlayerClass Computer, Activity caller)
    {
        String ComputerMove = Computer.ComputerTurn(Board, OpponentPassed, boneyard, this, caller);
        this.NextTurn();
        return ComputerMove;
    }


    /**
     method which looks through each player's hand and tries to find the engine,
     if not found forces each player to draw a tile and repeats the search
     @param User - human player whose hand is being searched and drawn to?
     @param Computer - computer player whose hand is being searched and drawn to
     @param boneyard - boneyard being drawn from
     @param intEngine - single digit engine number that is being looked for
     @return - boolean value returns true when engine is found and false if
     something went wrong (only with incorrect savegames)
     */
    public boolean FindEngine(HumanPlayerClass User, ComputerPlayerClass Computer, BoneyardClass boneyard, int intEngine)
    {
        //get string representation of the engine to be
        String Engine = Integer.toString(intEngine) + "-" + Integer.toString(intEngine);
        while (true) {

            //check player 1's  and player 2's hand for the correct tile
            //check user hand
            for (tile tile : User.GetHand()) {
                if (Engine.equals(tile.GetTileRepresentation())) {
                    this.SetTurn(Players.USER);
                    return true;
                }
            }
            //check Computer hand
            for (tile tile : Computer.GetHand()) {
                if (Engine.equals(tile.GetTileRepresentation())) {
                    this.SetTurn(COMPUTER);
                    return true;
                }
            }
            //check to see if boneyard has run out, draw if it hasnt
           if(!User.DrawTile(boneyard))
            {
                Log.i("RoundFindEngine", "Boneyard emptied, error");
                return false;
            }
            if(!Computer.DrawTile(boneyard)) {
                Log.i("RoundFindEngine", "Boneyard emptied, error");
                return false;
            }
        }
    }

    /**
    method determines who has the next turn and sets it appropiately, called after every move
     */
    public void NextTurn()
    {
        switch (this.CurrentTurn())
        {
            case USER:
                this.SetTurn(COMPUTER);
                break;

            case COMPUTER:
                this.SetTurn(Players.USER);
                break;
        }
    }

    /**
     parses string passed to it containing round information and sets information appropiately
     @param info - string containing round information passed by intent to roundactivity
     @param Human - humanclass being loaded into
     @param Computer -  computer being loaded into
     @param Round - round information being loaded for
     */
    public void ParseInfo(String info, boolean passed, HumanPlayerClass Human, ComputerPlayerClass Computer, RoundClass Round)
    {
        if (info==null)
        {
            return;
        }
        if (info.contains("Computer"))
        {
            Round.SetTurn(Players.COMPUTER);
        }
        else if (info.contains("Human"))
        {
            Round.SetTurn(Players.USER);
        }
        else
        {
            Round.SetTurn(Players.DRAW);
        }

        switch(Round.CurrentTurn())
        {
            case COMPUTER:
                Human.PassTurnSet(passed);
                break;

            case USER:
               Computer.PassTurnSet(passed);
                break;

            default:
                Log.i("Loading functions", "No case found");
                break;
        }

    }

}

