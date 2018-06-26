package topnotes.nituk.com.topnotes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MySubjects extends Fragment {

    Context mySubjectContext;

    private ListView subjectListView;
    //private ArrayList<String> allSubjects=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.my_subjects,container,false);
        subjectListView=view.findViewById(R.id.subjectListView);
        MyCustomArrayAdapter myCustomArrayAdapter=new MyCustomArrayAdapter(getActivity().getApplicationContext(),getResources().getStringArray(R.array.subjectList));
        subjectListView.setAdapter(myCustomArrayAdapter);
        return view;
    }

    public static MySubjects getInstance()
    {
        return new MySubjects();
    }


}