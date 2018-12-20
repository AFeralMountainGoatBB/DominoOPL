package bbrence.domino;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TournamentActivity extends AppCompatActivity {

    //active tournamentclass
    TournamentClass Tournament = new TournamentClass();
    //class that will display tournament information
    TournamentViewClass TournamentView = new TournamentViewClass();
    //serialize class for the tournament
    SerializeClass Serialize = new SerializeClass();
    //human that will be playing this tournament
    HumanPlayerClass Human = new HumanPlayerClass();
    //computer that will be playing this tournament
    ComputerPlayerClass Computer = new ComputerPlayerClass();
    //playerview class that will display the player information
    PlayerViewClass ViewPlayers = new PlayerViewClass();

    //success used for intent
    static final int ROUND_SUCCESS=1;

    /**
     method that creates activity, checks for intent data which communicates we are loading a tournament
     and loads them if they are present, sets up buttons and the view accordingly
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);

        Intent Data = getIntent();
        Log.i("Checking vals serialize", "Is data.getextras null?");

        if(Data.getExtras()!=null)
        {
            String filename = Data.getStringExtra("filename");
            Serialize.SetFilename(filename);
            Serialize.ReadSaveFile(this);
            //set loaded info
            Tournament.SetScoreGoal(Serialize.GetScoreGoal());
            Tournament.SetRoundCounter(Serialize.GetRoundnumber());
            //load human scores

            Human.SetScore(Serialize.GetHumanScore());
            Computer.SetScore(Serialize.GetComputerScore());
            Log.i("Checking val humanscore" , Integer.toString(Human.GetScore()));
          //  Log.i("Checking vals serialize" , Integer.toString(Serialize.GetScoreGoal()));

            TextView Hint = findViewById(R.id.ScorePrompt);
            Hint.setVisibility(View.GONE);
            EditText InputScoreGoal = findViewById(R.id.InputScoreGoal);
            InputScoreGoal.setVisibility(View.GONE);
            Button EnterScoreButton = findViewById(R.id.EnterScoreGoalButton);
            EnterScoreButton.setVisibility(View.GONE);

            Button ContinueRoundButton = findViewById(R.id.ContinueRoundButton);
            ContinueRoundButton.setVisibility(View.VISIBLE);

            TextView ComputerScore = findViewById(R.id.ComputerPlayerScore);
            TextView HumanScore = findViewById(R.id.HumanPlayerScore);

            ViewPlayers.ViewPlayerScore(Human, HumanScore, this );
            ViewPlayers.ViewPlayerScore(Computer, ComputerScore, this );
            TournamentView.DisplayRoundNumber(Tournament, this);
            TournamentView.DisplayTournamentGoal(Tournament, this);

            //still have values in HumanHand, ComputerHand, BoardLayout, StockLayout
        }
    }

    /**
     method that handles when an activity returns here (round activity) recieves
     data from round as in who won and how many points they earned, controls view as well accordingly
     also checks models for tournament winner and calls view functions to represent that
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        String winner = "";
        String Message = "";

        Button QuitButton = findViewById(R.id.QuitTournament);
        QuitButton.setVisibility(View.VISIBLE);

        Button ContinueRoundButton = findViewById(R.id.ContinueRoundButton);
        ContinueRoundButton.setVisibility(View.INVISIBLE);

        TextView ComputerScore = findViewById(R.id.ComputerPlayerScore);
        TextView HumanScore = findViewById(R.id.HumanPlayerScore);

        ViewPlayers.ViewPlayerScore(Human, HumanScore, this );
        ViewPlayers.ViewPlayerScore(Computer, ComputerScore, this );

        Button NewRound = findViewById(R.id.StartNewRound);
        NewRound.setVisibility(View.VISIBLE);

        Serialize.ClearAll();

        if (requestCode == ROUND_SUCCESS) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                if (data.getExtras()!=null) {
                    winner = data.getStringExtra("Winner");
                    int score = 0;
                    switch(winner) {
                        case "COMPUTER":
                            score = data.getIntExtra("Score", 0);
                            Computer.AddPoints(score);
                            Message = String.format(getResources().getString(R.string.RoundReturnWinner), winner, score);
                            break;

                        case "USER":
                            score = data.getIntExtra("Score", 0);
                            Human.AddPoints(score);
                            Message = String.format(getResources().getString(R.string.RoundReturnWinner), winner, score);
                            break;

                        case "DRAW":
                            Message = getResources().getString(R.string.RoundReturnDraw);
                            break;
                    }
                    TextView ReturnMessage = findViewById(R.id.TournamentMessageTextView);
                    ReturnMessage.setText(Message);
                    ReturnMessage.setVisibility(View.VISIBLE);

                    //checks for winner and increments round if nobody has won
                    winner = Tournament.NextRound(Human, Computer);

                    //Check to see if anyone won and close tournament if someone won
                    if (winner!=null)
                    {
                        if (winner.equals("Human"))
                        {
                            Message = String.format(getResources().getString(R.string.TournamentWinner),
                                    winner, Human.GetScore(), Tournament.GetRoundCounter());
                        }

                        if (winner.equals("Computer"))
                        {
                            Message = String.format(getResources().getString(R.string.TournamentWinner),
                                    winner, Computer.GetScore(), Tournament.GetRoundCounter());
                        }

                        TextView TournamentWonMBox = findViewById(R.id.TournamentWinnerMessageBox);
                        TournamentWonMBox.setText(Message);
                        TournamentWonMBox.setVisibility(View.VISIBLE);

                        //make sure only option is to quit tournament
                        Button NextRoundButton = findViewById(R.id.StartNewRound);
                        NextRoundButton.setVisibility(View.INVISIBLE);

                    }
                    TournamentView.DisplayRoundNumber(Tournament, this);
                }
            }
        }
        ViewPlayers.ViewPlayerScore(Human, HumanScore, this );
        ViewPlayers.ViewPlayerScore(Computer, ComputerScore, this );
    }

    /**
     button method that handles starting a new tournament, reads user's input
     and adjusts displays and models accordingly
     */
    public void EnterScore(View view)
    {
        EditText InputScoreField =findViewById(R.id.InputScoreGoal);
        String input = InputScoreField.getText().toString();
        if (input==null||input.matches(""))
        {
            return;
        }
        int InputScoreGoal = Integer.parseInt(InputScoreField.getText().toString());
        //if not greater than zero we don't want it, a score of one
        // is fine, as it will result in a sudden death tournament

        if (!(InputScoreGoal > 0 ))
            return;
        //hide enter score information
        Button EnterScoreButton = findViewById(R.id.EnterScoreGoalButton);
        EnterScoreButton.setVisibility(View.GONE);
        TextView Prompt = findViewById(R.id.ScorePrompt);
        Prompt.setVisibility(View.INVISIBLE);


       Tournament.SetScoreGoal(InputScoreGoal);
       InputScoreField.setVisibility(View.GONE);
       InputScoreField.setEnabled(false);

       TextView ComputerScore = findViewById(R.id.ComputerPlayerScore);
       TextView HumanScore = findViewById(R.id.HumanPlayerScore);

       ViewPlayers.ViewPlayerScore(Human, HumanScore, this );
       ViewPlayers.ViewPlayerScore(Computer, ComputerScore, this );
       TournamentView.DisplayRoundNumber(Tournament, this);
       TournamentView.DisplayTournamentGoal(Tournament, this);

      Button NewRound = findViewById(R.id.StartNewRound);
      NewRound.setVisibility(View.VISIBLE);
      Button QuitButton = findViewById(R.id.QuitTournament);
      QuitButton.setVisibility(View.VISIBLE);
    }

    /**
     button method that handles when starting a new round, creating an intent and starting the activity,
     */
    public void StartNewRound(View view)
    {

        Intent intent = new Intent(this, RoundActivity.class);
        intent.putExtra("RoundNumber", Tournament.GetRoundCounter());
        intent.putExtra("HumanScore", Human.GetScore());
        intent.putExtra("ComputerScore", Computer.GetScore());
        intent.putExtra("TournamentGoal", Tournament.GetScoreGoal());

        if (Serialize.GetBoardLayout()!=null)
        {
            intent.putExtra("Board", Serialize.GetBoardLayout());
            intent.putExtra("Boneyard", Serialize.GetBoneyard());
            intent.putExtra("HumanHand", Serialize.GetHumanHand());
            intent.putExtra("ComputerHand", Serialize.GetComputerHand());
            intent.putExtra("PassedInfo", Serialize.GetPassedInfo());
            intent.putExtra("Passed", Serialize.GetLastPassed());
        }

        startActivityForResult(intent, ROUND_SUCCESS);
    }

    /**
     button method that quits the tournament back to the main menu
     */
    public void QuitTournament(View view)
    {
        finish();
    }
}
