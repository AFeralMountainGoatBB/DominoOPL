package bbrence.domino;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.util.Vector;


/**
 * Created by raven on 10/30/2017.
 */

public class BoneYardViewClass {

    /**
     function formats and displays tiles found in boneyardclass passed
     @param Boneyard - Boneyard model that will be displayed
     @param caller - the calling activity
     */
    public void BoneYardView(BoneyardClass Boneyard, Activity caller)
    {
        GridLayout layout = caller.findViewById(R.id.BoneyardLayout);
        layout.removeAllViews();
        for(tile x : Boneyard.GetBoneyard())
        {
            String file = "d" + x.GetTileRepresentation().replace("-","");
          //  Log.i("trying to load", file);
            int myImage = caller.getResources().getIdentifier(file, "drawable", caller.getPackageName());
           // Log.i("Image id ", Integer.toString(myImage));
            ImageView im = new ImageView(caller);
            im.setImageResource(myImage);
            float rotation = 180;
            im.setRotation(rotation);
            layout.addView(im);
        }
    }

}
