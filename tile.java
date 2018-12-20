package bbrence.domino;

import android.util.Log;

/**
 * Created by raven on 10/30/2017.
 */


public class tile {

    //Class constants

    //Class Variables
    //total number of pips on the tile
    private int total;
    //value of first side of tile
    private int first;
    //value of second side of tile
    private int second;

    //if the tile is a double tile
    private  boolean isdouble = false;
    //keeps track of wether or not the tile can be played on the LHS
    private  boolean LHS=false; //only used by AI decision making
    //keeps track of wether or not the tile can be played on the LHS
    private boolean RHS=false; //only used by AI decision making

    //Constructors

    /**
     function that constructs tile with default values of -1, -2
     */
    tile()
    {
        this.SetValue(-1, -2);
        this.CalcTotal();
    }

    /**
     function that constructs tile with values passed to it and sets if tile is double or not
     @param x - left hand side of tile
     @param y - right hand side of tile
     */
    tile(int x , int y)
    {
        this.SetValue(x, y);
        this.CalcTotal();
    }

    //Selectors
    /**
     getter that gets the total number of pips on the tile
     @return - int value of total number of pips on tile, which is the sum of first and second
     */
    public final int GetTotal()
    {
        return this.total;
    }

    /**
     function that returns the first pip value in the tile
     @return - the value of pips on the first side of the tile
     */
    public final int GetFirst(){
        return this.first;
    }

    /**
     function that returns the second pip value in the tile
     @return - the value of pips on the second side of the tile
     */
    public final int GetSecond()
    {
        return this.second;
    }

    /**
     function that returns whether the tile can be played on the RHS of the board
     @return - true if it can be played, false if not
     */
    public final boolean GetRHS()
    {
        return this.RHS;
    }

    /**
     function that returns whether the tile can be played on the LHS of the board
     @return - true if it can be played, false if not
     */
    public final boolean GetLHS()
    {
        return this.LHS;
    }

    /**
     method getter that returns the value in isdouble wether or not if the tile is a double
     @return - true if the tile is a double, false if is not
     */
    boolean GetDouble()
    {
        return this.isdouble;
    }

    //Mutators

    /**
     method that sets the first side of the tile
     @param x - the value first will be set to
     */
    void SetFirst(int x){

        this.first=x;
    }

    /**
     method that sets the second side of the tile
     @param y - the value second will be set to
     */
    void SetSecond(int y){

        this.second=y;
    }

    /**
     method that sets the RHS tile to passed value, called by the functions involving moveset
     @param PassedVal - the value RHS will be set to
     */
    public void SetRHS(boolean PassedVal)
    {
        this.RHS=PassedVal;
    }

    /**
     method that sets the LHS tile to PassedValue, called by the functions involving moveset
     @param PassedVal - the value LHS will be set to
     */
    public void SetLHS(boolean PassedVal)
    {
        this.LHS=PassedVal;
    }

    /**
     method that sets both sides of the tile to passed int value
     @param x - int value to be set to the first side
     @param y - int value to be set to the second side
     */
    void SetValue(int x, int y)
    {
        this.SetFirst(x);
        this.SetSecond(y);
        if (x==y)
        {
            this.isdouble=true;
        }
        this.CalcTotal();
    }

    /**
     method that decodes tile representation and compares it to current tile
     @param TileRep - string representation of a tile
     @return true if it is the same tile, false if not
     */
    public boolean DecodeTileRepresentation(String TileRep)
    {
        return this.GetTileRepresentation().equals(TileRep);
    }

    /**
     method that turns tile into a tile representation string
     @return string representation of tile
     */
    public final String GetTileRepresentation()
    {
        String represent;
        represent = Integer.toString(this.GetFirst()) + "-" + Integer.toString(this.GetSecond());
        return represent;
    }

    /**
     method that swaps first and second pip values, used to compare both sides to lhs and rhs
     */
    void swap()
    {
        int temp=this.GetFirst();
        this.SetFirst(this.GetSecond());
        this.SetSecond(temp);
    }

    //Any utility (private) methods

    /**
     method that calculates total pips on a tile and stores it in total
     */
    private void CalcTotal()
    {
        this.total = this.GetFirst() + this.GetSecond();
    }

}
