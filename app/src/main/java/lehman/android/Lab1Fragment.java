package lehman.android;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.text.method.KeyListener;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

public class Lab1Fragment extends Fragment
{

    private static final String TAG = AppHelper.APP_LOG_TAG + "Lab1Fragment";
    static Object lock=new Object();
    static ToggleButton button;
    static ImageView image;
    static EditText latitude, longitude;
    static Button update,currentLocation, zoom;
    LocationManager locManager;
    ServiceTask task;
    double heading=10;
    double fov=75;
    final int width=400;
    final int height=600;
    final double pitch=0;
    final String API_KEY="AIzaSyBftWFHQT0WTCDJVJUd8Wsg8z0qhJvNItc";
    Receiver receiver=new Receiver();
    Location loc;

	public Lab1Fragment() {}

    public void setButText(ToggleButton button)
    {
        if(Receiver.on)
        {
           // Log.v("Google Maps Service", "Set text to stop service because on is "+GoogleMapsService.isOn());
            button.setText(button.getTextOff());
        }
        else
        {
           // Log.v("Google Maps Service", "Set text to start service because on is " + GoogleMapsService.isOn());
            button.setText(button.getTextOn());
        }
    }
    public static ToggleButton getBut()
    {
        return button;
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getActivity().registerReceiver(receiver,
                new IntentFilter("lehman.android.USER_ACTION"));
        locManager=(LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        loc=locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		Log.v(TAG, "Starting onCreateView");
		View rootView = inflater.inflate(R.layout.fragment_lab1, container, false);
        final ToggleButton but=(ToggleButton)rootView.findViewById(R.id.start_end_service);
        image=(ImageView)rootView.findViewById(R.id.mapsImage);
        image.setBackgroundResource(R.drawable.lab1);
        try {
            latitude = (EditText) rootView.findViewById(R.id.latitude);
            latitude.setText(Double.toString(loc.getLatitude()));
            longitude = (EditText) rootView.findViewById(R.id.longitude);
            longitude.setText(Double.toString(loc.getLongitude()));
         //   Toast.makeText(getActivity(),"latitude is "+latitude.getText(),Toast.LENGTH_LONG).show();
        }
        catch(NullPointerException e)
        {
            latitude = (EditText) rootView.findViewById(R.id.latitude);
            latitude.setText("can't get latitude");
            longitude = (EditText) rootView.findViewById(R.id.longitude);
            longitude.setText("can't get latitude");
        }
        String uri="https://maps.googleapis.com/maps/api/streetview?size="+width+"x"+height+"&location="+latitude.getText()+","+longitude.getText()+"&heading="+heading+"&fov="+fov+"&pitch="+pitch+"&key="+API_KEY;
        task=new ServiceTask(uri);
        task.execute();
        button=but;
        update=(Button)rootView.findViewById(R.id.update);
        zoom=(Button) rootView.findViewById(R.id.zoom);
        currentLocation=(Button)rootView.findViewById(R.id.current_location);
        setButText(but);
        final Intent intent=new Intent(getActivity().getApplicationContext(),GoogleMapsService.class);
        update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(v.equals(update))
                {

                    if (Receiver.on)
                    {
                        receiver.updateLocation();
                        getActivity().stopService(intent);
                        getActivity().startService(intent);
                    } else
                    {
                        image.setBackgroundResource(R.drawable.google_maps_service);
                    }
                }
            }
        });
        image.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.equals(image)) {
                    if (Receiver.on)
                    {
                        receiver.updateHeading();
                        getActivity().stopService(intent);
                        getActivity().startService(intent);
                        return true;
                    }

                }
                return false;
            }
        });
        zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.equals(zoom))
                {
                    if (Receiver.on)
                    {
                       receiver.updateFov();
                       getActivity().stopService(intent);
                       getActivity().startService(intent);
                    }
                    else
                    {
                        image.setBackgroundResource(R.drawable.google_maps_service_1);
                    }
                }
            }
        });
        but.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                    if(but.getText()==but.getTextOff())
                    {

                        getActivity().startService(intent);
                    }
                    else
                    {


                        getActivity().stopService(intent);
                        image.setBackgroundResource(R.drawable.lab1);
                        Location loc=locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        latitude.setText(Double.toString(loc.getLatitude()));
                        longitude.setText(Double.toString(loc.getLongitude()));
                        String url="https://maps.googleapis.com/maps/api/streetview?size="+width+"x"+height+"&location="+latitude.getText()+","+longitude.getText()+"&heading="+heading+"&fov="+fov+"&pitch="+pitch+"&key="+API_KEY;
                        task=new ServiceTask(url);
                        task.execute();
                    }
            }
        });
        currentLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(v.equals(currentLocation))
                {
                    loc=locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try {
                        latitude.setText(Double.toString(loc.getLatitude()));
                        longitude.setText(Double.toString(loc.getLongitude()));
                    }
                    catch(NullPointerException e)
                    {
                        latitude.setText("can't get latitude");
                        longitude.setText("can't get longitude");
                    }
                    String text1="https://maps.googleapis.com/maps/api/streetview?size="+width+"x"+height+"&location="+loc.getLatitude()+","+loc.getLongitude()+"&heading="+heading+"&fov="+fov+"&pitch="+pitch+"&key="+API_KEY;
                    task=new ServiceTask(text1);
                    task.execute();
                }
            }
        });
		Log.v(TAG, "onCreateView completed");
		return rootView;
	}

    @Override
    public void onDestroyView()
    {
        getActivity().unregisterReceiver(receiver);
        Intent intent=new Intent(getActivity().getApplicationContext(),GoogleMapsService.class);
        getActivity().stopService(intent);
        super.onDestroyView();
    }

    private class ServiceTask extends AsyncTask<String, Void, Object>
    {
        String url="";

        public ServiceTask(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
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
                BitmapDrawable d = new BitmapDrawable(null, (InputStream) content);
                return d;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o)
        {
            super.onPostExecute(o);
            if(Lab1Fragment.image!=null)
            {
                if(o!=null)
                {
                    image.setBackground((BitmapDrawable) o);
                }
                else
                {
                    image.setBackgroundResource(R.drawable.error);
                }
            }
        }
    }
}