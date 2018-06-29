package topnotes.nituk.com.topnotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyUploadsArrayAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> myUploadsNotesNameArray;
    private ArrayList<String> myUploadsSubjectNameArray;
    private ArrayList<String> myUploadsUploadDateArray;

    public MyUploadsArrayAdapter(Context context,ArrayList<String> myUploadsNotesNameArray
            ,ArrayList<String> myUploadsSubjectNameArray,ArrayList<String> myUploadsUploadDateArray) {
        super(context, -1,myUploadsNotesNameArray);
        this.myUploadsNotesNameArray=myUploadsNotesNameArray;
        this.myUploadsSubjectNameArray=myUploadsSubjectNameArray;
        this.myUploadsUploadDateArray=myUploadsUploadDateArray;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.fragment_upload_raw_layout, parent, false);
        TextView notesNameTextView=rowView.findViewById(R.id.uploadNotesNameTextView);
        TextView subjectNameTextView=rowView.findViewById(R.id.uploadSubjectNameTextView);
        TextView uploadDateTextView=rowView.findViewById(R.id.uploadFragmentDateTextView);

        notesNameTextView.setText(myUploadsNotesNameArray.get(position));
        subjectNameTextView.setText("Subject : "+myUploadsSubjectNameArray.get(position));
        uploadDateTextView.setText("Upload Date : "+myUploadsUploadDateArray.get(position));

        return rowView;
    }
}
