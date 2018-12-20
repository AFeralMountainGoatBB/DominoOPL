package bbrence.domino;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.Image;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Vector;

/**
 * Created by raven on 10/30/2017.
 */

public class PlayerViewClass {

    /**
     method formats and displays the tiles in player's hand as domino tile buttons
     @param Player - human player's hand to be passed
     @param caller - calling activity
     @param EndTurn - whether or not the player has ended their turn, effectively determines
     whether the button tiles are enabled
     */
    public void ViewPlayerHand(final HumanPlayerClass Player, final Activity caller, boolean EndTurn)
    {
        GridLayout layout = caller.findViewById(R.id.PlayerHand);
        layout.removeAllViews();
        for(tile x : Player.GetHand()) {
            //parsing tile to get the correct filename to load
            String file = "d" + x.GetTileRepresentation().replace("-", "");
            //  Log.i("trying to load", file);
            //loading the representative domino image with filename parsed from first step (found in drawable folder)
            int myImage = caller.getResources().getIdentifier(file, "drawable", caller.getPackageName());
            // Log.i("Image id ", Integer.toString(myImage));
            final ImageButton im = new ImageButton(caller);
            im.setImageResource(myImage);
            layout.addView(im);
            float rotation = 180;
            im.setRotation(rotation);
            im.setBackgroundColor(Color.parseColor("#ffffff"));
            //make sure the button is tagged with its string representation
            String tag = x.GetTileRepresentation();
            im.setTag(tag);

            //defining new OnClickListener
            im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //get tile representation
                    String TileRep = view.getTag().toString();
                    //set button visibility and send message to player
                    TextView PlayerMessage = caller.findViewById(R.id.MessageBox);
                    Button LHS = caller.findViewById(R.id.LHS);
                    Button RHS = caller.findViewById(R.id.RHS);
                    PlayerMessage.setVisibility(View.VISIBLE);
                    LHS.setVisibility(View.VISIBLE);
                    RHS.setVisibility(View.VISIBLE);
                    PlayerMessage.setText("Play " + TileRep + " On which side?");
                    Player.SetSelectedTile(TileRep);

                }
            });
            if (EndTurn)
            {
                im.setEnabled(false);
            }
            else
            {
                im.setEnabled(true);
            }
        }

        String Label="";
        TextView PlayerPassedLabel = caller.findViewById(R.id.HumanPassedTurnLabel);
        if (Player.GetPassed()) {
            Label = caller.getResources().getString(R.string.PassedTurnString, "Yes");
        }
        if (!Player.GetPassed())
        {
            Label = caller.getResources().getString(R.string.PassedTurnString, "No");
        }
        PlayerPassedLabel.setText(Label);
    }

    /**
     method sets display for the computer's turn, enabling and disabling certain buttons on the screen
     @param Human - human playerclass whose turn it was
     @param caller - calling activity
     */
    public void SetComputerTurn(HumanPlayerClass Human, Activity caller)
    {
        //set all buttons player uses to make moves to invisible, and "let ComputerPlay and Serialize
        //to visible and enabled

        Button PassButton = caller.findViewById(R.id.PassTurnButton);
        PassButton.setVisibility(View.INVISIBLE);
        PassButton.setText("Draw");

        ViewPlayerHand(Human, caller, true);

        Button HelpButton = caller.findViewById(R.id.HelpButton);
        HelpButton.setVisibility(View.INVISIBLE);

        Button CompTurnButton = caller.findViewById(R.id.ComputerTurnButton);
        CompTurnButton.setVisibility(View.VISIBLE);

        //update their labels
        String Label="";
        TextView PlayerPassedLabel = caller.findViewById(R.id.HumanPassedTurnLabel);
        if (Human.GetPassed()) {
            Label = caller.getResources().getString(R.string.PassedTurnString, "Yes");
        }
        if (!Human.GetPassed())
        {
            Label = caller.getResources().getString(R.string.PassedTurnString, "No");
        }
        PlayerPassedLabel.setText(Label);
    }

    /**
     method sets display for the human's turn, enabling and disabling certain buttons on the screen
     @param Human - human playerclass whose turn it is
     @param caller - calling activity
     */
    public void SetPlayerTurn(HumanPlayerClass Human, Activity caller)
    {
        ViewPlayerHand(Human, caller, false);

        Button DrawButton = caller.findViewById(R.id.PassTurnButton);
        DrawButton.setVisibility(View.VISIBLE);

        Button HelpButton = caller.findViewById(R.id.HelpButton);
        HelpButton.setVisibility(View.VISIBLE);

        String Label="";
        TextView PlayerPassedLabel = caller.findViewById(R.id.HumanPassedTurnLabel);
        if (Human.GetPassed()) {
            Label = caller.getResources().getString(R.string.PassedTurnString, "Yes");
        }
        if (!Human.GetPassed())
        {
            Label = caller.getResources().getString(R.string.PassedTurnString, "No");
        }
        PlayerPassedLabel.setText(Label);

    }

    /**
     method formats and displays the computer's hand as a set of domino tiles
     @param Computer - computer playerclass whose hand will be displayed
     @param caller - calling activity
     */
    public void ViewPlayerHand(ComputerPlayerClass Computer, final Activity caller)
    {
        GridLayout layout = caller.findViewById(R.id.ComputerHand);
        layout.removeAllViews();
        for(tile x : Computer.GetHand()) {
            //parsing tile to get the correct filename to load
            String file = "d" + x.GetTileRepresentation().replace("-", "");
            //  Log.i("trying to load", file);
            //loading the representative domino image with filename parsed from first step (found in drawable folder)
            int myImage = caller.getResources().getIdentifier(file, "drawable", caller.getPackageName());
            // Log.i("Image id ", Integer.toString(myImage));
            final ImageView im = new ImageView(caller);
            im.setImageResource(myImage);
            layout.addView(im);
            float rotation = 180;
            im.setRotation(rotation);

            im.setBackgroundColor(Color.parseColor("#ffffff"));

            String Label="";
            TextView CompPassedLabel = caller.findViewById(R.id.CompPassedTurnLabel);
            if (Computer.GetPassed()) {
                Label = caller.getResources().getString(R.string.PassedTurnString, "Yes");
            }
            if (!Computer.GetPassed())
            {
                Label = caller.getResources().getString(R.string.PassedTurnString, "No");
            }
            CompPassedLabel.setText(Label);

        }
    }

    /**
     method sets display for the computer's turn, enabling and disabling certain buttons on the screen
     @param Player - playerclass whose score is to be displayed
     @param Display - the display or more accurately the textview that the score will be displayed to
     @param caller - calling activity
     */
    public void ViewPlayerScore(PlayerClass Player, TextView Display, Activity caller)
    {
      int score= Player.GetScore();
      Resources res = caller.getResources();
      String HumanOrComputer = Display.getTag().toString();
      String text = res.getString(R.string.PlayerScoreDisplay, HumanOrComputer, score);
      Display.setText(text);
      Display.setVisibility(View.VISIBLE);
    }

}
