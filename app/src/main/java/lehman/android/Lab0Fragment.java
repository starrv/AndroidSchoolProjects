package lehman.android;

import android.app.Dialog;
import android.app.ListFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Lab0Fragment extends ListFragment
{

    private static final String TAG = AppHelper.APP_LOG_TAG + "Lab0Fragment";
    private String[] links;
    private ArrayList<String> titles = new ArrayList<String>();
    private ProgressTask task;
    Dialog dialog;
    JSONObject json;

    public Lab0Fragment()
    {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        dialog=new Dialog(getActivity());
        task=new ProgressTask("http://www.reddit.com/r/androiddev/hot/.json");
        task.execute();
        super.onActivityCreated(savedInstanceState);
        getListView().setPadding(20, 20, 20, 20);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(links[position]));
        startActivity(browserIntent);
        dialog.setTitle("redirecting please wait...");
        dialog.show();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        dialog.dismiss();
    }
    private class ProgressTask extends AsyncTask<String, Void, JSONObject>
    {
        String url;
        public ProgressTask(String url)
        {
            this.url=url;
        }

        @Override
        protected void onPreExecute()
        {
            getActivity().runOnUiThread(
            new Runnable()
            {
                public void run()
                {
                    dialog.setTitle("Loading...");
                }
            });
        }

        @Override
        protected void onPostExecute(JSONObject json)
        {
            JSONArray jarray=null;
            try
            {
               json = (JSONObject)json.get("data");
                jarray = (JSONArray)json.get("children");

                JSONObject json2=null;
                for(int i=0;i<jarray.length();i++)
                {
                    json2=jarray.getJSONObject(i);
                    json2=json2.getJSONObject("data");
                    titles.add(json2.getString("title"));
                }
                links = new String[titles.size()];

                for(int i=0;i<jarray.length();i++)
                {
                    json2=jarray.getJSONObject(i);
                    json2=json2.getJSONObject("data");
                    links[i]=json2.getString("url");
                }
                final JSONArray jar=jarray;

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.fragment_lab0, titles);
                setListAdapter(adapter);
            }
            catch(JSONException jse)
            {
                Log.v(TAG, "JSONException: " + jse.getMessage());

            }


        }

        @Override
        protected JSONObject doInBackground(String... params)
        {
            return getJson();
        }

        private JSONObject getJson()
        {

            JSONObject json=null;
            InputStream is=null;
            try
            {
                is = new URL(url).openStream();
                BufferedReader br=new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText=readAll(br);
                try
                {
                    json =new JSONObject(jsonText);
                    final JSONObject job=json;

                }
                catch(JSONException jex)
                {
                    Log.v(TAG, "JSONException: " + jex.getMessage());
                }
                finally{is.close();}
            }
            catch(IOException ioe)
            {
                Log.v(TAG, "IOException: " + ioe.getMessage());

            }
            return json;
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
}