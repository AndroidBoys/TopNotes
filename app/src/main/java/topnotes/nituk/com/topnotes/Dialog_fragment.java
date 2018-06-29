package topnotes.nituk.com.topnotes;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class Dialog_fragment extends DialogFragment implements View.OnClickListener {

    protected TextView notesTextView,questionPaperTextView,resourceTextView,practicalFileTextView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            //by using view you can find the id of the widgets present in R.layout.dialog_fragment
        View view=inflater.inflate(R.layout.dialog_fragment,container,false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setWindowAnimations(R.style.dialog_animation_fade);

        notesTextView=view.findViewById(R.id.notes);
        questionPaperTextView=view.findViewById(R.id.questionPaper);
        resourceTextView=view.findViewById(R.id.resources);
        practicalFileTextView=view.findViewById(R.id.practicalFiles);

        notesTextView.setOnClickListener(this);
        questionPaperTextView.setOnClickListener(this);
        resourceTextView.setOnClickListener(this);
        practicalFileTextView.setOnClickListener(this);

        return view;
    }

    public Dialog_fragment() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.notes:
                if(NetworkCheck.isNetworkAvailable(getActivity())){
                    //Move the user into notes Page
                }
                else {
                    InternetAlertDialogfragment internetAlertDialogfragment = new InternetAlertDialogfragment();
                    internetAlertDialogfragment.show(getFragmentManager().beginTransaction(), "dilog");
                }
                break;

            case R.id.resources:
                if(NetworkCheck.isNetworkAvailable(getActivity())){
                    //Move the user into resource Page
                    moveToContentActivity();
                }
                else {
                    InternetAlertDialogfragment internetAlertDialogfragment = new InternetAlertDialogfragment();
                    internetAlertDialogfragment.show(getFragmentManager().beginTransaction(), "dilog");
                }
                //Toast.makeText(getActivity(), "notes", Toast.LENGTH_SHORT).show();

                break;
            case R.id.practicalFiles:
                if(NetworkCheck.isNetworkAvailable(getActivity())){
                    //Move the user into notes activity
                    new DownloadDialogFragment().show(getFragmentManager(),"Download dialog");
                }
                else {
                    InternetAlertDialogfragment internetAlertDialogfragment = new InternetAlertDialogfragment();
                    internetAlertDialogfragment.show(getFragmentManager().beginTransaction(), "dilog");
                }

                break;

            case R.id.questionPaper:
                if(NetworkCheck.isNetworkAvailable(getActivity())){
                    //Move the user into notes activity
                }
                else {
                    InternetAlertDialogfragment internetAlertDialogfragment = new InternetAlertDialogfragment();
                    internetAlertDialogfragment.show(getFragmentManager().beginTransaction(), "dilog");
                }
               // Toast.makeText(getActivity(), "notes", Toast.LENGTH_SHORT).show();


        }

    }

    public void moveToContentActivity()
    {   Log.i("moving...","to contentActivity with context"+getActivity());
        Intent intent = new Intent(getActivity(),ContentsActivity.class);
        startActivity(intent);
    }

    static Dialog_fragment getInstance(){
        return new Dialog_fragment();
    }
}
