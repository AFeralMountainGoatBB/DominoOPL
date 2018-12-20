package bbrence.domino;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.Vector;

/**
 * Created by raven on 10/30/2017.
 */

public class BoardViewClass {

    /**
     function formats and displays tiles found in boardclass passed
     @param Board - Boardclass model that will be displayed
     @param caller - the calling activity
     */
    public void ViewBoard(BoardClass Board, Activity caller)
    {
        LinearLayout layout = caller.findViewById(R.id.Board);
        layout.removeAllViews();
        TextView Left = caller.findViewById(R.id.L);
        TextView Right = caller.findViewById(R.id.R);
        Left.setVisibility(View.VISIBLE);
        Right.setVisibility(View.VISIBLE);
        for(tile x : Board.GetBoard()) {
            String file = "d" + x.GetTileRepresentation().replace("-", "");
            //  Log.i("trying to load", file);
            int myImage = caller.getResources().getIdentifier(file, "drawable", caller.getPackageName());
            // Log.i("Image id ", Integer.toString(myImage));
            ImageView im = new ImageView(caller);
            im.setImageResource(myImage);
            layout.addView(im);

            float scale = .65f;
            im.setScaleX(scale);
            im.setScaleY(scale);
            //check to see if this tile needs to be rotated
            if (x.GetDouble()==false)
            {
                float rotation = 90;
                im.setRotation(rotation);
                im.setPadding(8, 0, 8, 0 );
               // im.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
    }

}
