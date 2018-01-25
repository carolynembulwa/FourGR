package equity.com.fourgr.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Menu;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import equity.com.fourgr.R;
import com.squareup.picasso.Picasso;


public class Large_ImagePopup extends Dialog {

    Context context;

    public Large_ImagePopup(Context context, String imageurl) {
        super(context);

        this.context = context;

//Set costume dialog information
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_mylargeimage);//<< your xml layout for custome image popup

//get current screen h/w
        Display d = ((Activity) context).getWindowManager().getDefaultDisplay();
        int w = d.getWidth();
        int h = d.getHeight();

//Set popup window h/w full screen or 98% it up to you
        getWindow().setLayout((int)( w/100)*100, (int)( h/100)*100);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setCancelable(true);

//now get imageview inside custome dialog layout
        ImageView large = (ImageView) findViewById(R.id.large);

//Show dialog
        show();

//after you show dialog you can animate image zoom effect or anything you want to do with large imageview you can Google it how to animate imageview fade-in or zoom
        Picasso.with(context)
                .load(imageurl)
                .into(large);


    }
}
