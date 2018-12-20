package bbrence.domino;

/**
 * Created by raven on 11/4/2017.
 */

public class TournamentClass {

    //Class Variables
    //tournament scoregoal, players will play until one of them reaches this file
    private int ScoreGoal;
    // roundcounter, represents what round of the tournament we are on
    private int RoundCounter;

    //Constructors
    /**
     constructor for tournament, initializes with default values
     */
    public TournamentClass()
    {
        this.RoundCounter=1;
        this.ScoreGoal=200;
    }

    //   Selectors
    /**
     getter method that returns the round number
     @return RoundCounter value, int representation of round number
     */
    public final int GetRoundCounter()
    {
        return this.RoundCounter;
    }

    /**
     getter method that returns the score goal
     @return ScoreGoal value, the int representation of what the players will play to
     */
    public final int GetScoreGoal()
    {
        return this.ScoreGoal;
    }

    //Mutators

    /**
     setter method that mutates the roundcounter to the value passed to it
     @param input - value passed that will become the new round number
     */
    public void SetRoundCounter(int input)
    {
        this.RoundCounter=input;
    }

    /**
     setter method that mutates the scoregoal to the value passed to it
     @param input - value passed that will become the new ScoreGoal
     */
    public void SetScoreGoal(int input)
    {
        this.ScoreGoal=input;
    }

    /**
    method checks to see if either player has won the tournament and then increments tournament
     @param Human - human player that is being evaluated
     @param Computer - computer that is being evaluated
     @return string if a player has won the round, null if nobody has won
     */
    public String NextRound(HumanPlayerClass Human, ComputerPlayerClass Computer)
    {
        //check to see if either player has won and return such, if not continue
        if (this.CheckWinner(Human)) {
            return "Human";
        }
        else if (this.CheckWinner(Computer))
        {
            return "Computer";
        }

        //increment roundnumber to reflect entering new round and return null
        this.RoundCounter++;
        return null;
    }

    //Any utility (private) methods
    /**
     method that checks to see if the player passed has won the tournament
     @param Player - player being checked to see if it has won the tournament
     */
private boolean CheckWinner(PlayerClass Player)
{
    //check if player has won and return accordingly
    if (this.GetScoreGoal() <=Player.GetScore())
    {
        return true;
    }
    else
    {
        return false;
    }
}







}
