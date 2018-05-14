package lehman.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Owner on 3/12/2015.
 */
public class MyAdapter extends ArrayAdapter
{

    public MyAdapter(Context context, int resource, List objects)
    {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Integer info=(Integer)getItem(position);
        if (position % 2 == 0)
        {
            ImageView image = null;
            if (convertView == null || convertView instanceof TextView)
            {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.image, parent, false);
            }
            image = (ImageView) convertView.findViewById(R.id.my_image);
            image.setBackgroundResource(info);
            image.setMaxHeight(50);
            image.setMaxWidth(50);
            return convertView;
        }
        TextView text = null;
        if (convertView == null  || convertView instanceof ImageView)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.text, parent, false);
        }
        text = (TextView) convertView.findViewById(R.id.my_text);
        text.setText(info);
        return convertView;
    }
}
