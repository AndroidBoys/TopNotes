package topnotes.nituk.com.topnotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class UploadFragment extends Fragment {


    private ListView uploadFileListView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_upload,container,false);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                UploadDialogFragment uploadDialogFragment =UploadDialogFragment.getInstance();
                uploadDialogFragment.show(fragmentManager,"upload");//Here upload is a tag name.
            }
        });

        uploadFileListView=view.findViewById(R.id.uploadFileListView);

        ArrayAdapter<String> uploadFileListAdapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.subjectList));
        uploadFileListView.setAdapter(uploadFileListAdapter);

        return view;
    }

    public static UploadFragment getInstance()
    {
        return new UploadFragment();
    }


}
