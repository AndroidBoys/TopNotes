package topnotes.nituk.com.topnotes;

import android.app.Activity;
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
    //private ArrayList<String> allSubjects=new ArrayList<>();
    private Activity actvity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        actvity=getActivity();
        View view = inflater.inflate(R.layout.my_subjects, container, false);
        subjectListView = view.findViewById(R.id.subjectListView);
        MySubjectArrayAdapter mySubjectArrayAdapter = new MySubjectArrayAdapter(getActivity(), getResources().getStringArray(R.array.subjectList));
        subjectListView.setAdapter(mySubjectArrayAdapter);
        //getActivity().getActionBar().setTitle("My Subjects");
        //((SubjectListActivity)actvity).setActionBarTitle("My Subjects");
        subjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Dialog_fragment dialog_fragment=Dialog_fragment.getInstance(i);
                dialog_fragment.show(getFragmentManager(),"dialog_fragment");

            }
        });


        return view;
    }
    public static MySubjects getInstance(){
        return new MySubjects();
    }

    @Override
    public void onResume() {
        super.onResume();
       ((SubjectListActivity)actvity).setActionBarTitle("My Subjects");
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if(context!=null)
//        ((SubjectListActivity)context).setActionBarTitle("My Subjects");
//    }
}