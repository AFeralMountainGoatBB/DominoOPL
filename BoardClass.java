package bbrence.domino;

import android.util.Log;

import java.util.ArrayList;
import java.util.Vector;
import java.util.Collections;
/**
 * Created by raven on 10/30/2017.
 */

public class BoardClass
{
    //Class Variables
    private Vector<tile> board;
    //front of board
    private int LHS;
    //back of board
    private int RHS;

    //Constructors

    /**
     function initializes the board vector and sets the lefthand and righthand values to -1
     */
    BoardClass()
    {
        this.board = new Vector<tile>();
        SetLHS(-1);
        SetRHS(-1);
    }

    //Selectors
    /**
     function gets board
     */
    public Vector<tile> GetBoard()
    {
        return this.board;
    }

    /**
     function gets the value of the left hand side
     @return value of lhs
     */
    public int GetLHS()
    {
        return this.LHS;
    }

    /**
     function gets the value of the right hand side
     @return value of rhs
     */
    public int GetRHS()
    {
        return this.RHS;
    }
    //Mutators

    /**
     function plays a tile to the board
     @param tile - tile passed to function to be played
     @param side - the side the tile will be played to
     @return true if successful, false if not
     */
    boolean PlayTile(tile tile, int side)
    {
        //checks if board is empty, meaning this should be the engine being placed
        if (this.board.isEmpty())
        {
            //placing engine
            Log.i("Playtile", "Engine Being Placed");
            this.board.add(tile);
            // sets initial LHS and RHS values
           this.SetLHS(tile.GetFirst());
           this.SetRHS(tile.GetSecond());
           return true; //return success
        }

        //right hand side
        if (side==1)
        {
            // check if tile can actually be played legally on this side
            if (this.GetRHS() == tile.GetFirst() || this.GetRHS() == tile.GetSecond())
            {
                // make sure it faces the right way
                if (this.GetRHS() == tile.GetSecond()) { tile.swap(); }
                // adds tile
                this.board.add(tile);
                // set new RHS
                this.RHS = tile.GetSecond();
                //return success, allowing Player to delete tile from their hand
                return true;
            }
            else
            {
                //not a legal move
                Log.i("BoardClass PlayTile", "Not a legal move (RHS)");
                //returns failure, player will not delete tile
                return false;
            }
        }
        //left hand side
        else if (side ==0)
        {
            // check if tile can actually be played legally on this side
            if (this.LHS == tile.GetFirst() || this.LHS == tile.GetSecond())
            {
                // make sure it faces the right way
                if (this.LHS == tile.GetFirst()) { tile.swap(); }
                //adds tile
                this.board.add(0, tile);
                // set new LHS
                this.SetLHS(tile.GetFirst());
                //return success, allowing Player to delete tile from their hand
                return true;
            }
            else
            {
              //  String LHS = this.LHS.toString();
                //not a legal move
                Log.i("Board Playtile", "Not a legal move (LHS)");
                //returns failure
                return false;
            }
        }
        //should not get here unless side was passed incorrectly
        Log.i("Board Playtile", "Side value passed incorrectly");
        return false;
    }

    /**
     function parses a string representation of a tile and then adds the tile to the board
     @param tilerep - tile passed to function to be played
     */
    private void AddTile(String tilerep)
    {
        int left = Character.getNumericValue(tilerep.charAt(0));
        int right = Character.getNumericValue(tilerep.charAt(2));
        tile tile = new tile(left, right);
        this.board.addElement(tile);
    }

    /**
     function parses string of boardrepresentation and constructs board from it, used in load function
     @param BoardRep - board representation in string format
     */
    public void LoadBoard(String BoardRep)
    {
        if (BoardRep==null) {
            return;
        }
        //get sets of 3 tiles off every time, translate them to tiles and add them to layout, then deletes them from string
        BoardRep=BoardRep.replaceAll("\\s", "");
        while (!BoardRep.isEmpty())
        {
            int index = BoardRep.indexOf("-");
            String TileRep = BoardRep.substring(0, 3);
            Log.i("LoadBoard", TileRep);
            BoardRep=BoardRep.replace(TileRep, "");
            this.AddTile(TileRep);
        }
        if (!board.isEmpty()) {
            this.SetLHS(this.board.firstElement().GetFirst());
            this.SetRHS(this.board.lastElement().GetSecond());
        }
    }

    /**
     function modifies left hand value to passed value, used to keep track of leftmost pips
     @param lhs value to be passed to the left hand side
     */
    void SetLHS(int lhs)
    {
        this.LHS=lhs;
    }

    /**
     function modifies right hand value to passed value, used to keep track of rightmost pips
     @param rhs value to be passed to the right hand side
     */
    void SetRHS(int rhs)
    {
        this.RHS=rhs;
    }
}
