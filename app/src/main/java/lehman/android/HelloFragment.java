package lehman.android;

import android.app.Fragment;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class HelloFragment extends Fragment {

    private static final String TAG = AppHelper.APP_LOG_TAG + "HelloFragment"; 

	public HelloFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_hello, container, false);
        ListView gridView=(ListView)rootView.findViewById(R.id.my_grid_view);
        ArrayList list=new ArrayList();
        list.add(R.drawable.val);
        list.add(R.string.greetings);
        MyAdapter adapter=new MyAdapter(getActivity(),R.layout.text,list);
        gridView.setAdapter(adapter);
        return rootView;
    }
}