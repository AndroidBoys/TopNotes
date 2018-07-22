package topnotes.nituk.com.topnotes;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class FeedbackList extends DialogFragment {
   ArrayList<String> feedbacks=new ArrayList<>();
   public static ArrayAdapter arrayAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.show_feedback_dialog_fragment,container,false);
        ListView feedbackListView=view.findViewById(R.id.feedbackListView);
        arrayAdapter=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,feedbacks);
        feedbackListView.setAdapter(arrayAdapter);
        return view;
    }

    @SuppressLint("ValidFragment")
    public FeedbackList(ArrayList<String> feedbacks) {
        this.feedbacks = feedbacks;
    }

     public FeedbackList(){

   }

}
