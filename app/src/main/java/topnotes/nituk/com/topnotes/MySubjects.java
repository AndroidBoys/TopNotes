package topnotes.nituk.com.topnotes;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
        MyCustomArrayAdapter myCustomArrayAdapter = new MyCustomArrayAdapter(mySubjectContext, getResources().getStringArray(R.array.subjectList));
        subjectListView.setAdapter(myCustomArrayAdapter);
        fragment=new Dialog_fragment();

        subjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // to set a custom animation in fragment
                fragmentTransaction.setCustomAnimations(R.anim.fragment_open_enter,
                        R.anim.fragment_open_exit, R.anim.fragment_close_enter,
                        R.anim.fragment_close_exit);

                if(listViewClickCount==0) {
                    listViewClickCount=1;
                    subjectListView.setAlpha((float) 0.4);// make subjectlistView transparent

                    Log.d("subjectlistclicked", "sdfasdfasdfasd");


                    fragmentTransaction.replace(R.id.Dialog_fragment_frameLayout, fragment).commit();
                }

                else{
                    listViewClickCount=0;
                    subjectListView.setAlpha((float) 1);// make subjectlistView transparent
                    fragmentTransaction.remove(fragment).commit();//removing existing fragment
                }
            }
        });


        return view;
    }


}