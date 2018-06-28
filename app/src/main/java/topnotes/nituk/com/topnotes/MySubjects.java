package topnotes.nituk.com.topnotes;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MySubjects extends Fragment {

    Context mySubjectContext;

    private ListView subjectListView;
    private int listViewClickCount=0;
     Fragment fragment;
    //private ArrayList<String> allSubjects=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_subjects, container, false);
        subjectListView = view.findViewById(R.id.subjectListView);
        MySubjectArrayAdapter mySubjectArrayAdapter = new MySubjectArrayAdapter(getActivity(), getResources().getStringArray(R.array.subjectList));
        subjectListView.setAdapter(mySubjectArrayAdapter);

        subjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Dialog_fragment dialog_fragment=Dialog_fragment.getInstance();
                dialog_fragment.show(getFragmentManager(),"dialog_fragment");

            }
        });


        return view;
    }
    public static MySubjects getInstance(){
        return new MySubjects();
    }


}