package bbrence.domino;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by raven on 11/9/2017.
 */

public class RoundViewClass {

    /**
     method sets display for end of round information, including hiding buttons and displays winner
     @param Round - round model being read from
     @param caller - calling activity
     */
    public void EndRound(RoundClass Round, Activity caller)
    {
        Button ComputerTurn = caller.findViewById(R.id.ComputerTurnButton);
        ComputerTurn.setVisibility(View.INVISIBLE);

        Button DrawPass = caller.findViewById(R.id.PassTurnButton);
        DrawPass.setVisibility(View.INVISIBLE);

        Button SaveQuit = caller.findViewById(R.id.SaveQuit);
        SaveQuit.setVisibility(View.INVISIBLE);

        Button QuitEndRound = caller.findViewById(R.id.DisplayEndofRound);
        QuitEndRound.setVisibility(View.VISIBLE);

        TextView Messages = caller.findViewById(R.id.MessageBox);
        String Message = caller.getResources().getString(R.string.EndRoundMessage1, Round.Getwinner().toString(), Round.GetWinnerScore());
        Messages.setText(Message);

        if (Round.Getwinner()== RoundClass.Players.DRAW)
        {
            Messages.setText(caller.getResources().getString(R.string.RoundReturnDraw));
        }

    }

    /**
     method of updating turn counter information, sets labels based on whose turn it is
     @param Round - round model being user
     @param caller - the calling activity
     */
    public void UpdateTurnCounter(RoundClass Round, Activity caller)
    {
        TextView Display = caller.findViewById(R.id.TextVRoundCounter);
        switch(Round.CurrentTurn())
        {
            case COMPUTER:
                Display.setText(R.string.ComputersTurn);
                break;
            case USER:
                Display.setText(R.string.HumansTurn);
                break;
            case DRAW:
                Display.setText(R.string.RoundEnd);
                break;
            default:
                Display.setText(R.string.RoundSetup);
                break;
        }
    }

}
