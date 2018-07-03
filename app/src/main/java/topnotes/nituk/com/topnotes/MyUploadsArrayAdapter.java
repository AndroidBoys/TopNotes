package topnotes.nituk.com.topnotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyUploadsArrayAdapter extends ArrayAdapter<Content> {

    private Context context;
    private List<Content> uploadedContent;

    public MyUploadsArrayAdapter(Context context, List<Content> uploadedContent) {
        super(context, -1,uploadedContent);
        this.uploadedContent=uploadedContent;
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.fragment_upload_raw_layout, parent, false);
        TextView notesNameTextView=rowView.findViewById(R.id.uploadNotesNameTextView);
        TextView subjectNameTextView=rowView.findViewById(R.id.uploadSubjectNameTextView);
        TextView uploadDateTextView=rowView.findViewById(R.id.uploadFragmentDateTextView);

        notesNameTextView.setText(uploadedContent.get(position).getTitle());
        subjectNameTextView.setText("Subject : "+uploadedContent.get(position).getAuthor());
        uploadDateTextView.setText("Upload Date : "+uploadedContent.get(position).getDate());
        ////
        return rowView;
    }
}
