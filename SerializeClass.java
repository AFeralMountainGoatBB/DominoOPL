package bbrence.domino;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Vector;

import static android.content.Context.MODE_WORLD_READABLE;

/**
 * Created by raven on 11/15/2017.
 */

public class SerializeClass {

    private String filename=null;
    private String ComputerHand=null;
    private int ComputerScore=0;
    private String HumanHand=null;
    private int HumanScore=0;

    private String PassedInfo=null;
    private boolean LastPassed;
    private String Stock=null;
    private String Board = null;
    private int TournamentScore=0;
    private int RoundNumber=0;
    private File SaveFile;

    //case? 'Round in progress' , 'BeforeRoundStart' , just tournament info

    /**
     constructor for creating serializeclass, initializes all variables
     */
    SerializeClass()
    {
        SaveFile = null;
        filename=null;
        ComputerHand=null;
        ComputerScore=0;
        HumanHand=null;
        HumanScore=0;

        PassedInfo=null;
        LastPassed = false;
        Stock=null;
        Board = null;
        TournamentScore=0;
        RoundNumber=0;
    }

    /**
     method for setting filename in string format
     @param name - name that filename will be set to, representation of what the file is in string
     */
    public void SetFilename(String name)
    {
       // File path = context.getExternalFilesDir(null);
        //File directory = new File(path, this.filename);
        if (name!= null) {
            this.filename =  name + ".txt";
            Log.d("Files", "Filename Set:" + this.filename);
        }
    }

    /*******************************************
     * Functions for Saving games
     * information is saved in primitive data types and saved to text file
     *******************************************/

    /**
     method for saving tournament information into string format
     @param TournamentScore - the score goal of the tournament being saved
     @param Round - Roundclass whose information is being loaded
     @param Writer - OutputStreamWriter that is being used for all save info
     */
    private void SaveTournament(int TournamentScore, RoundClass Round, OutputStreamWriter Writer)
    {
        //parse the Player into information we need for this area in string format
        String ExportString="Tournament Score: " + TournamentScore +   "\n";
        ExportString += "Round No.: " + Round.GetRoundNumber()+ "\n\n";

        try {
            Writer.write(ExportString);
            Writer.flush();
        }catch(IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     method for saving computer player information
     @param Computer - the computer player that is being saved to
     @param Writer - OutputStreamWriter that is being used for all save info
     */
    private void SaveComputer(ComputerPlayerClass Computer, OutputStreamWriter Writer)
    {
        //parse the Player into information we need for this area in string format
        String ExportString="Computer:\n\tHand: ";

        Vector<tile> WorkHand = Computer.GetHand();

        for( tile tile : WorkHand)
        {
            ExportString = ExportString + tile.GetTileRepresentation()+ " ";
        }
        ExportString += "\n\tScore: ";
        ExportString += Integer.toString(Computer.GetScore()) + "\n\n";

        try {
            Writer.write(ExportString);
            Writer.flush();
        }catch(IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }

    }

    /**
     method for saving human player information
     @param Human- the human player whose information is being saved
     @param Writer - OutputStreamWriter that is being used for all save info
     */
    private void SaveHuman(HumanPlayerClass Human, OutputStreamWriter Writer)
    {
        //parse the Player into information we need for this area in string format
        String ExportString="Human:\n\tHand: ";

        Vector<tile> WorkHand = Human.GetHand();

        for( tile tile : WorkHand)
        {
                ExportString = ExportString + tile.GetTileRepresentation()+ " ";
        }
        ExportString += "\n\tScore: ";
        ExportString += Integer.toString(Human.GetScore()) + "\n\n";

        try {
            Writer.write(ExportString);
            Writer.flush();
        }catch(IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }

    }

    /**
     method for saving board information
     @param Board- the Board whose information is being saved
     @param Writer - OutputStreamWriter that is being used for all save info
     */
    private void SaveBoard(BoardClass Board, OutputStreamWriter Writer)
    {
        //parse the Player into information we need for this area in string format
        String ExportString="Layout:\n\tL ";
        Vector<tile> WorkVector = Board.GetBoard();

        for( tile tile : WorkVector)
        {
            ExportString = ExportString + tile.GetTileRepresentation()+ " ";
        }
        ExportString += "R\n\n";
        try {
            Writer.write(ExportString);
            Writer.flush();
        }catch(IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }

    }

    /**
     method for saving boneyard information
     @param Boneyard - the boneyard whose information is being saved
     @param Writer - OutputStreamWriter that is being used for all save info
     */
    private void SaveBoneyard( BoneyardClass Boneyard, OutputStreamWriter Writer)
    {
        //parse the Boneyard into information we need for this area in string format
        String ExportString="Boneyard:\n\t";
        Vector<tile> WorkVector = Boneyard.GetBoneyard();
        Collections.reverse(WorkVector);

        for( tile tile : WorkVector)
        {
            ExportString = ExportString + tile.GetTileRepresentation()+ " ";
        }

        ExportString += "\n\n";
        try {
            Writer.write(ExportString);
            Writer.flush();
        }catch(IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }

    }

    /**
     method for saving whose turn it is and wether or not their opponent is passed information
     @param Human- the human player whose information is being saved
     @param Computer - the computer player whose information is being saved
     @param Round - the round whose information is being saved
     @param Writer - OutputStreamWriter that is being used for all save info
     */
    private void SavePassedTurnInformation(HumanPlayerClass Human, ComputerPlayerClass Computer,
                                           RoundClass Round, OutputStreamWriter Writer)
    {
        String ExportString = "Previous Player Passed: ";
        //parse the player and round into information we need for this area in string format
        switch(Round.CurrentTurn())
        {
            case USER:
                if (Computer.GetPassed())
                {
                    ExportString+= "Yes\n\n";
                }
                else
                {
                    ExportString+="No\n\n";
                }
                ExportString+= "Next Player: Human";
                break;

            case COMPUTER:
                if (Human.GetPassed())
                {
                    ExportString+= "Yes\n\n";
                }
                else
                {
                    ExportString+="No\n\n";
                }
                ExportString+= "Next Player: Computer";
                break;

            default:
                ExportString += "\n\nNext Player: ";
                break;
        }

        try {
            Writer.write(ExportString);
            Writer.flush();
        }catch(IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }

    }

    /**
     main method for save methods, establishes writer and calls each method in order and closes writer
     @param context - the context of the writer call
     @param Human- the human player whose information is being saved
     @param Computer - the computer player whose information is being saved
     @param Round - the round whose information is being saved
     @param TournamentScore - tournamentscore being saved
     @param Boneyard - boneyard whose information is being saved
     @param Board - board information that is being saved
     */
    public void CreateSaveFile(Context context, HumanPlayerClass Human, ComputerPlayerClass Computer,
                               RoundClass Round, int TournamentScore, BoneyardClass Boneyard, BoardClass Board) {
        try { // catches IOException below

            File path = context.getExternalFilesDir(null);
            Log.d("Files", "Path: " + path);
            Log.d("Files", filename);
            File directory = new File(path, this.filename);

            FileOutputStream FileOutput = new FileOutputStream(directory);
            OutputStreamWriter Writer = new OutputStreamWriter(FileOutput);

            //save all files in this order
            //tournament handles the first two lines
            this.SaveTournament(TournamentScore, Round, Writer);
            //computer handles computer hand and score
            this.SaveComputer(Computer, Writer);
            //saves human hand and score
            this.SaveHuman(Human, Writer);
            //saves the layout L -- R
            this.SaveBoard(Board, Writer);
            //saves the boneyard and faces it the correct way
            this.SaveBoneyard(Boneyard, Writer);
            //saves the information about whose turn it is and if the previous player passed
            this.SavePassedTurnInformation(Human, Computer, Round,Writer);
            //each function flushes buffer after they complete so unecessary to do that here,
            // close file after done writing
            Writer.close();
        }

        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


/***********************************
 * End functions for saving games
 ***********************************/

    /*****************************************************************
     * Functions for loading save games
     ****************************************************************/

    /**
     main method for loading all the saved information into strings and stored in this class
     @param context- the context of the call
     */
    public void ReadSaveFile(Context context) {
        try {
            File path = context.getExternalFilesDir(null);
            Log.d("FileLoad", "Path: " + path);
            Log.d("FileLoad", filename);
            File directory = new File(path, this.filename);

            InputStream FileInput = new FileInputStream(directory);
           InputStreamReader Reader = new InputStreamReader(FileInput);
           BufferedReader BuffRead = new BufferedReader(Reader);

           int length = filename.length();

           //load objects in this order, they do not return, just store information in private variables

            //load round # and tournament goal
            this.LoadTournament(BuffRead, length);
            //load computer player info
            this.LoadComputer(BuffRead, length);
            //load human player info
            this.LoadHuman(BuffRead, length);
            //load layout information L -- R
            this.LoadBoard(BuffRead, length);
            //load stock, reverses to get it to face the correct way
            this.LoadBoneyard(BuffRead, length);

            this.LoadPassTurnInformation(BuffRead, length);

        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     method for loading tournament information into the serialize class
     @param Reader- the reader being used for all loadfunctions
     @param Filelength - length of file for resetting marker
     */
    private void LoadTournament(BufferedReader Reader, int Filelength) {
        String Input = "";

        try { Reader.mark(Filelength);
            while ((Input = Reader.readLine())!=null)
            {
                Input = Input.replaceAll(" ", "");
               if(Input.contains("TournamentScore:"))
               {
                   int index = Input.indexOf(":");
                   Input = Input.substring(index+1);
                   this.TournamentScore=Integer.parseInt(Input);
               }
               if(Input.contains("RoundNo.:"))
               {
                   int index = Input.indexOf(":");
                   Input = Input.substring(index+1);
                   Log.i("LoadRound", Input);
                   this.RoundNumber = Integer.parseInt(Input);
               }
            } Reader.reset();
        }
        catch(IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     method for loading computer player information into the serialize class
     @param Reader- the reader being used for all loadfunctions
     @param length- length of file for resetting marker
     */
    private void LoadComputer(BufferedReader Reader, int length)
    {
        String Input;

        try {
            Reader.mark(length);
            while ((Input = Reader.readLine())!=null)
            {
                if(Input.contains("Computer:"))
                {
                    Input=Reader.readLine();
                    Input = Input.replaceAll("\\s", "");
                    Input = Input.replace("Hand:", "");
                    Log.i("LoadFunction in Comp", Input);
                    //Input = Input.replace("-","");
                    this.ComputerHand = Input;
                    //now get the score
                    Input=Reader.readLine();
                    Input = Input.replaceAll("\\s", "");
                    int index = Input.indexOf(":");
                    Input = Input.substring(index+1);
                    Log.i("LoadFunction in Comp", Input);
                    this.ComputerScore=Integer.parseInt(Input);
                    break;
                }
            }
        }
        catch(IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     method for loading Human player information into the serialize class
     @param Reader- the reader being used for all loadfunctions
     @param length- length of file for resetting marker
     */
    private void LoadHuman(BufferedReader Reader, int length)
    {
        String Input;
        try {
            Reader.mark(length);
            while ((Input = Reader.readLine())!=null)
            {
                if(Input.contains("Human:"))
                {
                    Input=Reader.readLine();
                    Input = Input.replaceAll("\\s", "");
                    Input = Input.replace("Hand:", "");
                    //Input = Input.replace("-","");
                    Log.i("LoadFunction in Hum", Input);
                    this.HumanHand = Input;
                    //now get the score
                    Input=Reader.readLine();
                    Input = Input.replace("Score:", "");
                    Input = Input.replaceAll("\\s", "");
                    Log.i("LoadFunction in Hum", Input);
                    this.HumanScore=Integer.parseInt(Input);
                    break;
                }
            }Reader.reset();
        }
        catch(IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     method for loading Board information into the serialize class
     @param Reader- the reader being used for all loadfunctions
     @param length- length of file for resetting marker
     */
    private void LoadBoard(BufferedReader Reader, int length)
    {
        String Input;

        try {
            Reader.mark(length);
            while ((Input = Reader.readLine())!=null)
            {
                if(Input.contains("Layout:"))
                {
                    Input=Reader.readLine();
                    Input = Input.replaceAll(" ", "");
                    Input = Input.replace("L" , "");
                    Input = Input.replace("R", "");
                    this.Board = Input;
                    break;
                }
            }Reader.reset();
        }
        catch(IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     method for loading boneyard information into the serialize class
     @param Reader- the reader being used for all loadfunctions
     @param length- length of file for resetting marker
     */
    private void LoadBoneyard(BufferedReader Reader, int length)
    {
        String Input;

        try {
            Reader.mark(length);
            while ((Input = Reader.readLine())!=null)
            {
                Log.i("LoadBoneYardTEST", Input);
                if(Input.contains("Boneyard:"))
                {
                    Input=Reader.readLine();
                    this.Stock = Input;
                    break;
                }
            }
        }
        catch(IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     method for loading passed turn information into the serialize class
     @param Reader- the reader being used for all loadfunctions
     @param length- length of file for resetting marker
     */
    private void LoadPassTurnInformation(BufferedReader Reader, int length)
    {
        String Input;
        try {
            Reader.mark(length);
            while ((Input = Reader.readLine())!=null)
            {
                if(Input.contains("Previous Player Passed:"))
                {
                    Input = Input.replaceAll("\\s", "");
                    if (Input.contains("Yes"))
                    {
                        this.LastPassed=true;
                    }
                    else if (Input.contains("No"))
                    {
                        this.LastPassed=false;
                    }

                    //now get the next turn info
                    Input=Reader.readLine();
                    Input=Reader.readLine();
                    Input = Input.replace("Next Player:", "");
                    Input = Input.replaceAll("\\s", "");
                    this.PassedInfo=Input;
                    break;
                }
            }
        }
        catch(IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /*****************************************************
     * End load functions
     ******************************************************/

    /**
     getter scoreGoal information that is saved in the serialize class variable TournamentScore
     @return - int value of TournamentScore
     */
    public final int GetScoreGoal()
    {
        return this.TournamentScore;
    }

    /**
     getter for HumanScore that is saved in the serialize class variable HumanScore
     @return - int value of Human score - how many points they have earned so far
     */
    public final int GetHumanScore()
    {
        return this.HumanScore;
    }

    /**
     getter for ComputerScore that is saved in the serialize class variable ComputerScore
     @return - int value of Computer score - how many points they have earned so far
     */
    public final int GetComputerScore()
    {
        return this.ComputerScore;
    }

    /**
     getter for RoundNumber that is saved in the serialize class variable RoundNumber
     @return - int value of RoundNumber - what round number is it
     */
    public final int GetRoundnumber()
    {
        return this.RoundNumber;
    }

    /**
     getter for HumanHand string that is saved in the serialize class variable HumanHand
     @return - string representation of the tiles held by the humanplayer
     */
    public final String GetHumanHand()
    {
        return this.HumanHand;
    }

    /**
     getter for ComputerHand string that is saved in the serialize class variable ComputerHand
     @return - string representation of the tiles held by the ComputerPlayer
     */
    public final String GetComputerHand()
    {
        return this.ComputerHand;
    }

    /**
     getter for board layout string that is saved in the serialize class variable Board
     @return - string representation of the tiles present in Board
     */
    public final String GetBoardLayout()
    {
        return this.Board;
    }

    /**
     getter for boneyard string that is saved in the serialize class variable Stock
     @return - string representation of the tiles present in the boneyard
     */
    public final String GetBoneyard()
    {
        return this.Stock;
    }

    /**
     getter for passed turn information string that is saved in the serialize class variable PassedInfo
     @return - string representation of passed turn information to be passed to round
     */
    public final String GetPassedInfo()
    {
        return this.PassedInfo;
    }

    /**
     getter for finding last passed string that is saved in the serialize class variable LastPassed
     @return - string representation of the tiles present in LastPassed
     */
    public final boolean GetLastPassed() { return this.LastPassed; }

    /**
     function that resets all variables in the class, clearing for between loading a round
     */
    public void ClearAll()
    {
        filename=null;
       ComputerHand=null;
        ComputerScore=0;
        HumanHand=null;
       HumanScore=0;
       PassedInfo=null;
        LastPassed=false;
      Stock=null;
       Board = null;
        TournamentScore=0;
       RoundNumber=0;
       SaveFile=null;
    }
}
