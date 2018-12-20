package bbrence.domino;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FilenameFilter;

public class MainMenuActivity extends AppCompatActivity {
    //variable that keeps track of what file is chosen in the load game dialog
    String ChosenFile="";

    /**
     method creates the activity, quits program if passed intent from other activities
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("EXIT", false)) {
            finish();
        }
    }

    /**
     method starts an intent with no additional info and starts the tournamentActivity, linked by NewGame button
     */
    public void NewGame(View view)
    {
        Intent intent = new Intent(this, TournamentActivity.class);
        startActivity(intent);
        //start TournamentActivity and ask for Game Limit there
    }

    /**
     method quits activity and since this is the top activity, also quits the application,
     linked to quitgame button
     */
    public void QuitGame(View view)
    {
        Log.i("Quit Function", "User has requested to quit");
        //quit, works
        this.finishAffinity();
    }

    /**
     function creates dialog where user chooses what file they want to load,
     creates list of files from designated folder, when clicked the file is loaded and tournament
     launched with loaded round
     */
    public void LoadGame(View view)
    {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a save file");
        File[] files;
        File path = getExternalFilesDir(null);
       // String path = Environment.getExternalFilesDir ().toString()+;
        Log.d("Files", "Path: " + path);
       // File directory = new File(path);
        files = path.listFiles();
        final String[] FileList = new String[files.length];
     Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            FileList[i] = files[i].getName();
            Log.d("Files", "FileName:" + files[i].getName());
        }
// add a list
        //String[] animals = {"horse", "cow", "camel", "sheep", "goat"};
        builder.setItems( FileList, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
             ChosenFile = FileList[which];
            ChosenFile= ChosenFile.replace(".txt", "");
             Log.i("ChosenFile", ChosenFile);
                Intent intent = new Intent(getBaseContext(), TournamentActivity.class);
                intent.putExtra("filename", ChosenFile);
                startActivity(intent);
           //  Log.i("ChosenFile", ChosenFile);
            }
        });
// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();


    }
    }


