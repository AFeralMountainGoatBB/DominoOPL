package bbrence.domino;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

/**
 * Created by raven on 11/6/2017.
 */

public class TournamentViewClass {

    /**
     method that displays the tournament goal
     @param Tournament - tournament model that is being read from
     @param caller - calling activity being displayed to
     */
    public void DisplayTournamentGoal(TournamentClass Tournament, Activity caller)
    {
        TextView Display = caller.findViewById(R.id.CurrentRoundText);
       int ScoreGoal = Tournament.GetScoreGoal();
       Resources res = caller.getResources();
       String text = res.getString(R.string.TournamentScoreGoal, ScoreGoal);
       Display.setText(text);
       Display.setVisibility(View.VISIBLE);
    }

    /**
     method that displays the round number
     @param Tournament - tournament model that is being read from
     @param caller - calling activity being displayed to
     */
    public void DisplayRoundNumber(TournamentClass Tournament, Activity caller)
    {
        TextView Display = caller.findViewById(R.id.TournamentScore);
        int RoundNumber = Tournament.GetRoundCounter();
        Resources res = caller.getResources();
        String text = res.getString(R.string.CurrentRound, RoundNumber);
        Display.setText(text);
        Display.setVisibility(View.VISIBLE);

    }


}
