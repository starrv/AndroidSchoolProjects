package lehman.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

/**
 * Created by Owner on 3/22/2015.
 */
public class Receiver extends BroadcastReceiver
{
    static Drawable d;
    static boolean on=false;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Toast.makeText(context, "hello", Toast.LENGTH_LONG).show();
        if (intent.getExtras().getBoolean("drawable"))
        {
            if(d!=null) {
                Lab1Fragment.image.setBackground(d);
            }
            else
            {
                Lab1Fragment.image.setBackgroundResource(R.drawable.error);
            }
        }

        if (intent.getExtras().getBoolean("start"))
        {
            try
            {
              Lab1Fragment.getBut().setText(Lab1Fragment.getBut().getTextOff());
            }
            catch (NullPointerException e)
            {
            }
            on = true;
           // Toast.makeText(context,"Service is on evaluates to "+on,Toast.LENGTH_LONG).show();
        }

        if(intent.getExtras().getBoolean("stop"))
        {
            try
            {
                Lab1Fragment.getBut().setText(Lab1Fragment.getBut().getTextOn());
            }
            catch(NullPointerException e){}
            on = false;
           // Toast.makeText(context,"Service is on evaluates to "+Receiver.on,Toast.LENGTH_LONG).show();
        }
    }

    public void updateLocation()
    {
        try
        {
            GoogleMapsService.setLatitude(Double.parseDouble(new StringBuilder(Lab1Fragment.latitude.getText()).toString()));
            GoogleMapsService.setLongitude(Double.parseDouble(new StringBuilder(Lab1Fragment.longitude.getText()).toString()));
        }
        catch(Exception e){}
    }

    public void updateHeading()
    {
        GoogleMapsService.setHeading(GoogleMapsService.heading + 5);
    }

    public void updateFov()
    {
        if (GoogleMapsService.fov >= 30)
        {
            GoogleMapsService.setFov(GoogleMapsService.fov - 45);
        }
        else
        {
            GoogleMapsService.setFov(75);
        }
    }
}
