package topnotes.nituk.com.topnotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyDownloadAnotherArrayAdapter extends ArrayAdapter<String> {

    private String[] downloadList;
    private Context context;
    public MyDownloadAnotherArrayAdapter(Context context, String[] downloadList) {
        super(context,-1,downloadList);
        this.context=context;
        this.downloadList=downloadList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =layoutInflater.inflate(R.layout.download_listview_another_raw_layout,parent,false);

        TextView downloadSubjectNameTextview=view.findViewById(R.id.downloadSubjectTextView);
        downloadSubjectNameTextview.setText(downloadList[position]);
        return view;
    }
}
