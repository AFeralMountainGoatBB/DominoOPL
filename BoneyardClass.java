package bbrence.domino;

import android.util.Log;

import java.util.Collections;
import java.util.Vector;

/**
 * Created by raven on 10/30/2017.
 */

public class BoneyardClass {
    //This vector is used as the stock for the game, all functions in this class revolve around it
    private Vector<tile> boneyard;

    //Constructors *************************
    /**
     * function: constructor for BoneyardClass, init data vars and generates boneyard
     */
    BoneyardClass()
    {
        this.boneyard = new Vector<tile>();
        this.GenerateBoneyard();
    }

    /**
     * function is called to test wether the boneyard is empty or not
     * @return whether or not boneyard is empty
     */
    public boolean isEmpty()
    {
        return this.boneyard.isEmpty();
    }

    /**
     * function: Generates the 28 tile set for gameplay and stores it in the vector
     * @params none
     */
    void GenerateBoneyard()
    {
        for (int i=0; i<7; i++)
        {
            for (int x=i; x<7; x++)
            {
                tile y = new tile(x, i);
                //   Log.i("New Tile", y.GetTileRepresentation());
                this.boneyard.addElement(y);
            }
        }
        Collections.shuffle(this.boneyard);
    }
    //Selectors ********************

    /**
     * function: returns the actual vector contained in boneyard for other functions to use
     * @params none
     * @return boneyard
     */
    Vector<tile> GetBoneyard()
    {
        return this.boneyard;
    }

    //Mutators ***********************

    /**
     function is called by DrawTile() in order to pop the last element of the vector (treated like a stack)
     */
    public void PopBoneyard()
    {
        //removes element at rear of boneyard
        this.boneyard.remove(this.boneyard.lastElement());
    }

    /**
     function parses a string to translate into tiles and adds parsed to this boneyard
     @param BoneyardRep - String representation of the boneyard that is parsed in this function
     */
    public void LoadBoneyard(String BoneyardRep)
    {
        this.boneyard.removeAllElements();
        if(BoneyardRep==null)
        {
            Log.i("LoadboneYard", "Boneyard null");
        }
        //get sets of 3 tiles off every time, translate them to tiles and add them to layout, then deletes them from string
    if(BoneyardRep!=null) {
    BoneyardRep = BoneyardRep.replaceAll("\\s", "");

    while (!BoneyardRep.isEmpty()) {
        int index = BoneyardRep.indexOf("-");
        String TileRep = BoneyardRep.substring(index - 1, index + 2);
        BoneyardRep = BoneyardRep.replace(TileRep, "");
        this.AddTile(TileRep);
    }
    if(!boneyard.isEmpty())
    {
        Collections.reverse(boneyard);
    }
}

    }

    /**
     function parses passed tile representation and adds the tile to the boneyard
     @param tilerep- String representation of the tile that is parsed in this function
     */
    private void AddTile(String tilerep)
    {
        int left = Character.getNumericValue(tilerep.charAt(0));
        int right = Character.getNumericValue(tilerep.charAt(2));
        tile tile = new tile(left, right);
        this.boneyard.addElement(tile);
    }

    /**
     function is called by a PlayerClass that wants a tile from the boneyard, this function returns and pops the last element of boneyard
     @return Returns the last tile of the boneyard
     */
    public tile DrawTile()
    {
        //copy the tile that will be returned
        tile tile = this.boneyard.lastElement();
        //delete its spot in the boneyard
        this.PopBoneyard();
        return tile;
    }

    //Any utility (private) methods

    /**
     function displays the entire vector boneyard's string representations in logcat
     */
    void DisplayBoneYard()
    {
        for (tile x: boneyard)
        {
            Log.i("Boneyard Tile ", x.GetTileRepresentation() );
        }
    }



}
