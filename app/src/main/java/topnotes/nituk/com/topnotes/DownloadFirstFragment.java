package topnotes.nituk.com.topnotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

//  Fragment showing the downloaded Subject of the user

public class DownloadFirstFragment extends Fragment {


    private ListView mDownloadedSubjectListView;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_download, container, false);


        mDownloadedSubjectListView = view.findViewById(R.id.downloadedfilelistview);

        MyDownloadAnotherArrayAdapter myDownloadsAnotherArrayAdapter = new MyDownloadAnotherArrayAdapter(getActivity(), getResources().getStringArray(R.array.subjectList));
        mDownloadedSubjectListView.setAdapter(myDownloadsAnotherArrayAdapter);
        mDownloadedSubjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                FragmentManager fragmentManager = getFragmentManager();
                DownloadPopUpDialogFragment downloadPopUpDialogFragment = DownloadPopUpDialogFragment.getInstance(i);
                downloadPopUpDialogFragment.show(fragmentManager, "download_dialog");
            }


        });
        return view;
    }
    public static DownloadFirstFragment getInstance()
    {
        return new DownloadFirstFragment();
    }
}