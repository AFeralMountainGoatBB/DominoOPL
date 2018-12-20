package bbrence.domino;

import android.content.Intent;
import android.content.res.Resources;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static bbrence.domino.TournamentActivity.ROUND_SUCCESS;

public class RoundActivity extends AppCompatActivity {

    @Override
    /**
     method sets display for the computer's turn, enabling and disabling certain buttons on the screen
     @param Human - human playerclass whose turn it was
     @param caller - calling activity
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);
        Intent DataIntent = getIntent();
        ActionBar Title = getSupportActionBar();

        if(getIntent().getExtras()!=null)
        {
            Round.SetRoundNumber(DataIntent.getIntExtra("RoundNumber", 1));
            TournamentGoal=DataIntent.getIntExtra("TournamentGoal", 200);
            Human.SetScore(DataIntent.getIntExtra("HumanScore", 0));
            Computer.SetScore(DataIntent.getIntExtra("ComputerScore", 0));
            if(DataIntent.getStringExtra("Board")!=null)
            {
                Round.SetLoadedRound(true);
                Button StartRoundButton = findViewById(R.id.StartRound);
                StartRoundButton.setText("Continue Round");
                Board.LoadBoard(DataIntent.getStringExtra("Board"));
                BoneYard.LoadBoneyard(DataIntent.getStringExtra("Boneyard"));
                Human.LoadHand(DataIntent.getStringExtra("HumanHand"));
                Computer.LoadHand(DataIntent.getStringExtra("ComputerHand"));
                String Parseinfo = DataIntent.getStringExtra("PassedInfo");
                boolean passed = DataIntent.getBooleanExtra("Passed", false);
                Round.ParseInfo(Parseinfo, passed, Human, Computer, Round);
            }
        }

        String TitleMessage=getResources().getString(R.string.ActionBarRound, Round.GetRoundNumber());
        Title.setTitle(TitleMessage);

    }

    //Active round that will be played this activity
    RoundClass Round = new RoundClass();
    //Roundviewclass that will be displaying round information
    RoundViewClass RoundView = new RoundViewClass();
    //boneyard for this round
    BoneyardClass BoneYard = new BoneyardClass();
    //board for this round
    BoardClass Board = new BoardClass();
    //boardview that will display the board
    BoardViewClass BoardView = new BoardViewClass();
    //boneyard view class that will display the boneyard
    BoneYardViewClass ViewB = new BoneYardViewClass();
    //human player for this round
    HumanPlayerClass Human = new HumanPlayerClass();
    //computer player for this round
    ComputerPlayerClass Computer = new ComputerPlayerClass();
    //playerview class that will display all player information
    PlayerViewClass TestViewPlayer = new PlayerViewClass();
    //serialize class in case user wants to save and quit
    SerializeClass Serialize = new SerializeClass();
    //tournamentgoal score
    int TournamentGoal;

    /**
     method controller for computerturn, calls computerturn in the model class and interprets the
     return string for calling display functions to update the activity display
     */
    public void ComputerTurn(View view)
    {
        String ComputerTurnResult= Round.ComputerTurn(Board, Human.GetPassed(), BoneYard, Computer, this);
        //Log.i("ComputerTurn", ComputerTurnResult);
        TextView MessageBox = findViewById(R.id.MessageBox);
        Button ComputerTurn = findViewById(R.id.ComputerTurnButton);
        ComputerTurn.setVisibility(View.INVISIBLE);

        MessageBox.setText(ComputerTurnResult);

        ViewB.BoneYardView(BoneYard, this);
        TestViewPlayer.ViewPlayerHand(Computer, this);
        BoardView.ViewBoard(Board, this);
        TestViewPlayer.SetPlayerTurn(Human, this);

        Round.SetTurn(RoundClass.Players.USER);
        RoundView.UpdateTurnCounter(Round, this);

        //end of turn "Cleanup"
        if (BoneYard.isEmpty()) {
            if(Round.CheckLockCondition(Board, Human, Computer))
            {
                Round.RoundEnd(Human, Computer);
                //call exit protocol
                RoundView.EndRound(Round, this);
            }
        }
        if (Computer.GetHand().isEmpty())
        {
            Round.RoundEnd(Human, Computer);
            TestViewPlayer.ViewPlayerHand(Human,this , true);
            //call exit protocol
            RoundView.EndRound(Round, this);
        }
    }

    /**
     method controller drawing and passing the turn for player, called with the pass/draw button
     updates the model and display accordingly
     */
    public void DrawTilePassTurn(View view)
    {
        TextView MessageBox = findViewById(R.id.MessageBox);
        Button PassButton = findViewById(R.id.PassTurnButton);

        //hide buttons
        Button LHS = findViewById(R.id.LHS);
        Button RHS = findViewById(R.id.RHS);
        LHS.setVisibility(View.INVISIBLE);
        RHS.setVisibility(View.INVISIBLE);

        String MessageFormat;
        String Message;
        String DrawOrPass="";
        //check if player has drawn tile already
        if (!Human.GetDrawnStatus())
        {
            DrawOrPass="Draw";
        }
        else
        {
            DrawOrPass="Pass";
        }

        //first check to see if tiles are playable
        if(Human.CanPlay(Board, Computer.GetPassed()))
        {
            MessageFormat = getResources().getString(R.string.MessageBoxCantDrawOrPass);
            Message= String.format(MessageFormat, DrawOrPass );
            MessageBox.setText(Message);

            return;
        }

        if(!Human.GetDrawnStatus())
        {
            Human.ClearDrawnStatus();
            if(Human.DrawTile(BoneYard))
            {
                PassButton.setText("Pass");
                Human.SetDrawnStatus();
                TestViewPlayer.ViewPlayerHand(Human, this, false);
                ViewB.BoneYardView(BoneYard, this);
                MessageBox.setText("You have Drawn tile " + Human.GetHand().lastElement().GetTileRepresentation());
                return;
            }
            else
                {
                    PassButton.setText("Pass");
                    Human.SetDrawnStatus();
                    TestViewPlayer.ViewPlayerHand(Human, this, false);
                    Message = getResources().getString(R.string.MessageBoxFailToDraw);
                    MessageBox.setText(Message);

                    return;
                }
        } //end if user hasn't drawn this turn
        else
        { //if user has already attempted to draw this turn
            //already checked to make sure player has no playable tiles,
            // so this will just be passturn
            Human.PassTurnSet(true);
            Human.ClearDrawnStatus();

            MessageBox.setText("You have Passed your turn");
            Round.NextTurn();
            //make buttons invis and set to be computer turn essentially
            RoundView.UpdateTurnCounter(Round, this);
            TestViewPlayer.SetComputerTurn(Human, this);

            if (BoneYard.isEmpty())
            {
                if (Round.CheckLockCondition(Board, Human, Computer))
                {
                    Round.RoundEnd(Human, Computer);
                    RoundView.EndRound(Round, this);
                }
            }


        }

    }

    /**
     method for starting the round, checks for intent information to determine
     if the round is a loaded round and updates models and displays accordingly
     */
    public void StartRound(View view)
    {
        if (Human.GetHand().isEmpty())
        {
            Log.i("StartRound" , "Human hand empty");
            Round.DrawPhase(BoneYard, Human, Computer);
        }
        String Player;
        String text;
        Resources res = this.getResources();
        ViewB.BoneYardView(BoneYard,this );
        TestViewPlayer.ViewPlayerHand(Human, this, true);
        TestViewPlayer.ViewPlayerHand(Computer, this);
        BoardView.ViewBoard(Board, this);
        TextView MessageBox = findViewById(R.id.MessageBox);
        MessageBox.setText("");
        MessageBox.setVisibility(View.VISIBLE);
        Button FindEngine = findViewById(R.id.FindEngine);
        FindEngine.setVisibility(View.VISIBLE);
        if (Round.GetLoadedRound())
        {
            switch(Round.CurrentTurn())
            {
                case COMPUTER:
                    Button ComputerTurnButton = findViewById(R.id.ComputerTurnButton);
                    TestViewPlayer.ViewPlayerHand(Human, this, true);
                    ComputerTurnButton.setVisibility(View.VISIBLE);
                    Player="Computer";
                    text = res.getString(R.string.MessageBoxWhoseTurn, Player);
                    MessageBox.setText(text);
                    RoundView.UpdateTurnCounter(Round, this);
                    break;

                case USER:
                    TestViewPlayer.ViewPlayerHand(Human, this, false);
                    Player="Human";
                    text = res.getString(R.string.MessageBoxWhoseTurn, Player);
                    MessageBox.setText(text);
                    Round.SetTurn(RoundClass.Players.USER);
                    RoundView.UpdateTurnCounter(Round, this);
                    Button HelpButton = findViewById(R.id.HelpButton);
                    HelpButton.setVisibility(View.VISIBLE);
                    Button Draw = findViewById(R.id.PassTurnButton);
                    Draw.setVisibility(View.VISIBLE);
                    break;
            }
            if (!Board.GetBoard().isEmpty())
            {
                FindEngine.setVisibility(View.INVISIBLE);
            }
        }


        Button StartRound = findViewById(R.id.StartRound);
        StartRound.setVisibility(View.GONE);

        Button DrawPassButton = findViewById(R.id.PassTurnButton);
        DrawPassButton.setText("Draw");



        //show all labels
        TextView BoneYardLabel = findViewById(R.id.BoneyardLabel);
        TextView PlayerHandLabel = findViewById(R.id.HumanHandLabel);
        TextView ComputerHandLabel = findViewById(R.id.ComputersHandLabel);
        TextView PlayerPassedLabel = findViewById(R.id.HumanPassedTurnLabel);
        TextView ComputerPassedTurnLabel = findViewById(R.id.CompPassedTurnLabel);

        //Update PassedTurnLabels
        String Label="";
        if (Human.GetPassed()) {
            Label = getResources().getString(R.string.PassedTurnString, "Yes");
        }
        if (!Human.GetPassed())
        {
            Label =getResources().getString(R.string.PassedTurnString, "No");
        }
        PlayerPassedLabel.setText(Label);

        if (Computer.GetPassed()) {
            Label = getResources().getString(R.string.PassedTurnString, "Yes");
        }
        if (!Computer.GetPassed())
        {
            Label =getResources().getString(R.string.PassedTurnString, "No");
        }
        ComputerPassedTurnLabel.setText(Label);


        BoneYardLabel.setVisibility(View.VISIBLE);
        PlayerHandLabel.setVisibility(View.VISIBLE);
        ComputerHandLabel.setVisibility(View.VISIBLE);
        PlayerPassedLabel.setVisibility(View.VISIBLE);
        ComputerPassedTurnLabel.setVisibility(View.VISIBLE);
    }

    /**
     method for findengine, calls model functions to determine what the engine is, who has it and calls
     display to communicate results to user and updates buttons and who has the first turn accordingly
     */
    public void FindEngine(View view)
    {
        Round.FindEngine(Human, Computer, BoneYard, Round.GetEngine());
        TestViewPlayer.ViewPlayerHand(Computer, this);
        ViewB.BoneYardView(BoneYard, this );

        Button FindEngine = findViewById(R.id.FindEngine);
        FindEngine.setVisibility(View.GONE);
        Button ComputerTurnButton = findViewById(R.id.ComputerTurnButton);
        TextView Messages = findViewById(R.id.MessageBox);

        Resources res = this.getResources();
        String Player="";
        String EngineRepresentation = Integer.toString(Round.GetEngine()) +"-"+ Integer.toString(Round.GetEngine());
        if (Round.CurrentTurn()==null)
        {
            Log.i("FindEngineMethod", "Current turn is null");
            return;
        }
        String text ="";

        switch(Round.CurrentTurn()){
            //set visibility and other things for round up initially
            case USER:
                TestViewPlayer.ViewPlayerHand(Human, this, true);
                Button PlayEngine = findViewById(R.id.PlayEngine);
                PlayEngine.setVisibility(View.VISIBLE);
                ComputerTurnButton.setVisibility(View.INVISIBLE);
                Player="User";
                text = res.getString(R.string.MessageBoxEngineFound, Player, EngineRepresentation);
                Messages.setText(text);
                Round.SetTurn(RoundClass.Players.USER);
                RoundView.UpdateTurnCounter(Round, this);
                break;

            case COMPUTER:
                TestViewPlayer.ViewPlayerHand(Human, this, true);
                ComputerTurnButton.setVisibility(View.VISIBLE);
                Player="Computer";
                text = res.getString(R.string.MessageBoxEngineFound, Player, EngineRepresentation);
                Messages.setText(text);
                Round.SetTurn(RoundClass.Players.COMPUTER);
                RoundView.UpdateTurnCounter(Round, this);
                break;
        }

    }

    /**
     method button for playing a tile on the left hand side of the board / layout, updates
     display and model accordingly
     */
    public void LHSPlayTile(View view)
    {
        //regardless of what happens buttons LHS and RHS will be hidden and tag on PlayerMessage reset
        Button LHS = findViewById(R.id.LHS);
        Button RHS = findViewById(R.id.RHS);
        TextView PlayerMessage = findViewById(R.id.MessageBox);
        if (Human.GetSelectedTile()==null)
        {
            Log.i("LHS PlayTile", "null tag, probably a double click, skipping statement");
            return;
        }
        //getting a string representation of the tile
        String TileRep = Human.GetSelectedTile();
        //resetting the tag for future uses of tile button
        Human.ClearSelectedTile();

        //make lhs and rhs invisible again
        LHS.setVisibility(View.INVISIBLE);
        RHS.setVisibility(View.INVISIBLE);
        //actually attempt to play the tile now
        if (!Human.PlayTile(Board, TileRep, 0 , Computer.GetPassed()))
        {
            //basically just ignore everything, nothing changes in model so viewHand and ViewBoard doesnt have to be updated
            PlayerMessage.setText("Sorry, " + TileRep + " cannot be played there");

        }
        else
        {
            //objects moved so we have to update all views involved (player and board),
            PlayerMessage.setText("Tile " + TileRep + " played successfully on the left hand side");

            BoardView.ViewBoard(Board, this);
            TestViewPlayer.SetComputerTurn(Human, this);
            Round.NextTurn();
            RoundView.UpdateTurnCounter(Round, this);
        }

        //end turn stuff!
        if (Human.GetHand().isEmpty())
        {
            //player won but lets call scoring anyways as it does the same job
            Round.RoundScoring(Human, Computer);
            TestViewPlayer.ViewPlayerHand(Human, this, true);
            RoundView.EndRound(Round, this);
        }
        if (BoneYard.isEmpty() && Round.CheckLockCondition(Board, Human, Computer))
        {
            Round.RoundScoring(Human, Computer);
            TestViewPlayer.ViewPlayerHand(Human, this, true);
            RoundView.EndRound(Round, this);
        }

    }

    /**
     method button for playing a tile on the right hand side of the board / layout, updates
     display and model accordingly
     */
    public void RHSPlayTile(View view)
    {
//regardless of what happens buttons LHS and RHS will be hidden and tag on PlayerMessage reset
        Button LHS = findViewById(R.id.LHS);
        Button RHS = findViewById(R.id.RHS);
        TextView PlayerMessage = findViewById(R.id.MessageBox);

        //check to prevent nullptr exception
        if (Human.GetSelectedTile()==null)
        {
            Log.i("LHS PlayTile", "null tag, probably a double click, skipping statement");
            return;
        }
        String TileRep = Human.GetSelectedTile();
        Human.ClearSelectedTile();
        LHS.setVisibility(View.INVISIBLE);
        RHS.setVisibility(View.INVISIBLE);

        if (!Human.PlayTile(Board, TileRep, 1 , Computer.GetPassed()))
        {
            //basically just reset everything, nothing changes in model so view doesnt have to be updated
            PlayerMessage.setText("Sorry, " + TileRep + " cannot be played there");
        }
        else
        {
            //objects moved so we have to update all views involved (player and board)
            PlayerMessage.setText("Tile " + TileRep + " played successfully on the right hand side");

            BoardView.ViewBoard(Board, this);

            TestViewPlayer.SetComputerTurn(Human, this);
            Round.NextTurn();
            RoundView.UpdateTurnCounter(Round, this);
        }

        //end turn stuff!
        if (Human.GetHand().isEmpty())
        {
            //player won but lets call scoring anyways as it does the same job
            Round.RoundScoring(Human, Computer);
            TestViewPlayer.ViewPlayerHand(Human, this, true);
            RoundView.EndRound(Round, this);
        }
        if (BoneYard.isEmpty() && Round.CheckLockCondition(Board, Human, Computer))
        {
            Round.RoundScoring(Human, Computer);
            TestViewPlayer.ViewPlayerHand(Human, this, true);
            RoundView.EndRound(Round, this);
        }
    } //end of RHS playtile

    /**
     method button for the user playing the engine in the incase they have the first turn,
     controlled by button of same name, updates model and display for the engine being played
     */
    public void PlayEngine(View view)
    {
        Button PlayEngineButton = findViewById(R.id.PlayEngine);
        PlayEngineButton.setVisibility(View.GONE);

        int EngineInt = Round.GetEngine();
        String EngineRep = Integer.toString(EngineInt) + "-" + Integer.toString(EngineInt);
        Human.PlayTile(Board, EngineRep, 0, false);
        BoardView.ViewBoard(Board, this);
        Round.NextTurn();
        TestViewPlayer.SetComputerTurn(Human, this);
        RoundView.UpdateTurnCounter(Round, this);
    }

    /**
     method button for controlling the end of a round, when a winner has been determined
     determines winner and their score to be awarded, creates an intent with this information and
     calls back to tournamentActivity to restart the round
     */
public void EndOfRound(View view)
{
    Intent data = new Intent();

    switch (Round.Getwinner())
    {
        case DRAW:
            data.putExtra("Winner", "DRAW");
            break;

        case USER:
            data.putExtra("Winner", "USER");
            data.putExtra("Score", Round.GetWinnerScore());
            break;

        case COMPUTER:
            data.putExtra("Winner", "COMPUTER");
            data.putExtra("Score", Round.GetWinnerScore());
            break;
    }

    setResult(RESULT_OK,data);
    finish();
}

    /**
     method button for saving and quitting the round, updates display and model respectively
     */
public void SaveAndQuit(View view)
{
    EditText FileInput = findViewById(R.id.FileNameInput);
    if(FileInput.getVisibility()==View.INVISIBLE) {
        FileInput.setVisibility(View.VISIBLE);
        return;
    }

    String input = FileInput.getText().toString();
    if (input==null||input.matches(""))
    {
        return;
    }

    Serialize.SetFilename(input);
    //mother save function, will call individual files and build savefile itself
    Serialize.CreateSaveFile(this, Human, Computer, Round, TournamentGoal, BoneYard, Board);

    Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.putExtra("EXIT", true);
    startActivity(intent);

    FileInput.setText("");
    FileInput.setVisibility(View.INVISIBLE);

}

    /**
     method button for controlling the help function for player, updates display to
     communicates results to player
     */
public void HelpAction(View view)
{
    String Message = Human.Help(Board, Computer.GetPassed());

    TextView MessageBox = findViewById(R.id.MessageBox);
    MessageBox.setText(Message);

}

}
