package lehman.android;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by Owner on 3/13/2015.
 */
public class GoogleMapsService extends Service
{
    //static boolean on=false;
    static double longitude;
    static double latitude;
    static double heading=10;
    static double fov=75;
    final static int width=400;
    final static int height=600;
    final static double pitch=0;
    final static String API_KEY="AIzaSyBftWFHQT0WTCDJVJUd8Wsg8z0qhJvNItc";
    Location loc;
    final static String TAG="Google Maps Service";
    static ServiceTask task;
    static BitmapDrawable d;
    Intent intent;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;

    }

    @Override
    public void onCreate()
    {
       // on=false;
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

            Log.v("Google Maps Service", "Google Maps Service started");
            //on = true;
            intent=new Intent("lehman.android.USER_ACTION");
            intent.putExtra("start",true);
            sendBroadcast(intent);
            String url="https://maps.googleapis.com/maps/api/streetview?size="+width+"x"+height+"&location="+latitude+","+longitude+"&heading="+heading+"&fov="+fov+"&pitch="+pitch+"&key="+API_KEY;
            task=new ServiceTask(url);
            task.execute();
            return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy()
    {
        Log.v("Google Maps Service", "Google Maps Service stopped");
        intent=new Intent("lehman.android.USER_ACTION");
        intent.putExtra("stop",true);
        intent.removeExtra("start");
        sendBroadcast(intent);
        //on = false;
        super.onDestroy();//add Location Manager, LocationListener, and in manifest add permission fine location

    }

    public static void setLatitude(double lat)
    {
        latitude=lat;
    }

    public static void setLongitude(double log)
    {
        longitude=log;
    }

    public static void setHeading(double ang)
    {
        heading=ang;
    }

    public static void setFov(double ang)
    {
        fov=ang;
    }

    private class ServiceTask extends AsyncTask<String, Void, Object>
    {
        String url="";

        public ServiceTask(String url) {
            this.url = url;
        }


        @Override
        protected Object doInBackground(String[] params)
        {
            InputStream link = null;
            URL urlLink=null;
            Object content=null;
           // HttpURLConnection httpLink=null;
            try
            {
                urlLink=new URL(url);
                content=urlLink.getContent();

             //   httpLink=(HttpURLConnection)urlLink.openConnection();

            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            if(content!=null)
            {
                d = new BitmapDrawable(null, (InputStream) content);
                Receiver.d=d;
            }
            Intent intent = new Intent("lehman.android.USER_ACTION");
            intent.putExtra("drawable",true);
            sendBroadcast(intent);
            return d;
        }

    }

    public String readAll(Reader reader)
    {
        StringBuilder sb=new StringBuilder();
        int next;
        try
        {
            while((next=reader.read())!=-1)
            {
                sb.append((char)next);
            }
        }
        catch(IOException ioe){Log.v(TAG, "IOException: " + ioe.getMessage());}
        return sb.toString();
    }
}
