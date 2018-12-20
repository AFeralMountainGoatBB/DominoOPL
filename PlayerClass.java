package bbrence.domino;
import android.util.Log;
import java.util.Vector;

/**
 * Created by raven on 11/4/2017.
 */

//super class for HumanPlayerClass and ComputerPlayerClass
public class PlayerClass {

    //Class constants

    //Class Variables
    private Vector<tile> MoveSet;
    private int Score;
    private boolean PassedLastTurn;
    private Vector<tile> PlayerHand;

    //Constructors
    /**
     method constructs playerclass, initializes variables.
     */
    PlayerClass()
    {
        this.PlayerHand = new Vector<tile>();
        Vector<tile> MoveSet = new Vector<tile>();
        this.Score=0;
        PassedLastTurn=false;
    }
    //Selectors

    /**
     method returns the value of PassedLastturn - whether or not the player passed their last turn
     @return PassedLastTurn - did this user pass their last turn?
     */
    public boolean GetPassed()
    {
        return this.PassedLastTurn;
    }

    /**
     method returns the value of Score, how many points this player has earned in the tournament so far
     @return value stored in member variable score (how many points this player has in this tournament
     */
    public int GetScore()
    {
        return this.Score;
    }

    /**
     method returns the the hand of the player
     @return the vector of tiles that is the player's hand in the round
     */
    public Vector<tile> GetHand()
    {
        return this.PlayerHand;
    }

    //mutators

    /**
     method if possible, removes a tile from the boneyard and places it into user's hand
     @param boneyard - boneyard active in round where this function will attempt to draw from
     @return true if the tile could be drawn, false if the boneyard is empty
     */
    public boolean DrawTile(BoneyardClass boneyard)
    {
        if (boneyard.isEmpty())
        {
            Log.i("DrawTile", "Boneyard was empty");
            //on return false the method will call for Boneyard Empty to be displayed
            return false;
        }
        //adds element returned by boneyard.Drawtile, boneyard.Drawtile pops the element off the back for us
        this.PlayerHand.addElement(boneyard.DrawTile());
        return true;
    }

    /**
     method adds a tile to the player's hand bsaed on the string representation passed to it
     @param TileRep - string representation of a tile
     */
    public void AddTile(String TileRep)
    {
        int left = Character.getNumericValue(TileRep.charAt(0));
        int right = Character.getNumericValue(TileRep.charAt(2));
        tile tile = new tile(left, right);
        Log.i("Addtile" , TileRep);
        this.PlayerHand.addElement(tile);
    }

    /**
     method adds points to the players existing score value
     @param points - number of points to be added
     */
    public void AddPoints(int points)
    {
        this.Score= this.Score + points;
    }

    /**
     method finds a tile in hand by string representation passed to it
     @param TileRep - string representation of a tile
     @return the tile found in hand, a tile init to -1, -1 if no tile found
     */
    public tile FindTile(String TileRep)
    {
        for (tile tile : this.PlayerHand)
        {
           if(tile.DecodeTileRepresentation(TileRep))
               return tile;
        }
        //returning null should never be allowed to happen
        return null;
    }

    /**
     method removes a specific tile from the players hand
     @param tile - tile that will be removed from hand
     */
    public void RemoveTile(tile tile)
    {
        if (this.PlayerHand.contains(tile)) {
            this.PlayerHand.remove(tile);
        }else
        {
            Log.i("RemoveTile", "Attempted to remove non existent tile from playerhand");
        }
    }

    /**
     method sets passturn value to a value passed, useful for loading game
     @param ValueSet - boolean value that will represent whether or not the player passed their last turn
     */
    public void PassTurnSet(boolean ValueSet)
    {
        this.PassedLastTurn=ValueSet;
    }


}
