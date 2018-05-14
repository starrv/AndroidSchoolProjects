package lehman.android;

import android.app.Fragment;
import android.os.AsyncTask;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class Lab2Fragment extends Fragment {

    private static final String TAG = AppHelper.APP_LOG_TAG + "Lab2Fragment";
    static boolean flag;
    FanView fanView;
    final static float MIN=15;
    static float change=MIN;
    Button low,med,high,off;

	public Lab2Fragment() {}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        flag=false;
        Log.v(TAG, "Starting onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_lab2, container, false);
        fanView = (FanView) rootView.findViewById(R.id.fanView);
        low = (Button) rootView.findViewById(R.id.low);
        med = (Button) rootView.findViewById(R.id.med);
        high = (Button) rootView.findViewById(R.id.high);
        low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=true;
                change = MIN;
                fanView.invalidate();
                fanView.requestLayout();
            }
        });
        med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                flag=true;
                change = 2*MIN;
                fanView.invalidate();
                fanView.requestLayout();
            }
        });
        high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=true;
                change = 3*MIN;
                fanView.invalidate();
                fanView.requestLayout();
            }
        });
        off = (Button) rootView.findViewById(R.id.off);
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flag = false;
            }
        });
        Log.v(TAG, "onCreateView completed");
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        flag=false;
    }
}