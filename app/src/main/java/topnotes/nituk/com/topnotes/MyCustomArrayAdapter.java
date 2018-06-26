package topnotes.nituk.com.topnotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyCustomArrayAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] values;
    public MyCustomArrayAdapter(Context context,String[] values) {
        super(context,-1,values);
        this.context=context;
        this.values=values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.subject_listview_raw_layout, parent, false);
        TextView textView =rowView.findViewById(R.id.rowTextView);
        textView.setText(values[position]);
        return rowView;
    }
}
