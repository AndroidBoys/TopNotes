package topnotes.nituk.com.topnotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class MyDownloadsArrayAdapter extends ArrayAdapter<String> {


    ArrayList<String> downloadsArray;
    Context context;
    public MyDownloadsArrayAdapter(Context context, ArrayList<String> downloadsArray) {
        super(context,-1,downloadsArray);
        this.downloadsArray=downloadsArray;
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.downloads_listview_raw_layout, parent, false);

        return rowView;
    }
}
