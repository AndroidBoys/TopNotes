package topnotes.nituk.com.topnotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyDownloadsArrayAdapter extends ArrayAdapter<String> {


    private List<String> downloadsNotesNameArray;
    private ArrayList<String> getDownloadsAuthorsNameArray;
    private Context context;
    public MyDownloadsArrayAdapter(Context context, List<String> downloadsNotesNameArray, ArrayList<String> getDownloadsAuthorsNameArray) {
        super(context,-1,downloadsNotesNameArray);
        this.downloadsNotesNameArray=downloadsNotesNameArray;
        this.getDownloadsAuthorsNameArray=getDownloadsAuthorsNameArray;
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.downloads_listview_raw_layout, parent, false);
        TextView notesNameTextView=rowView.findViewById(R.id.notesNameTextView);
        TextView authorsNameTextView=rowView.findViewById(R.id.authorsNameTextView);

        notesNameTextView.setText(downloadsNotesNameArray.get(position));
        authorsNameTextView.setText("Author : "+getDownloadsAuthorsNameArray.get(position));

        return rowView;
    }
}
