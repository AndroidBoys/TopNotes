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
import androidx.fragment.app.Fragment;

public class Dialog_fragment extends DialogFragment implements View.OnClickListener {

    protected TextView notesTextView,questionPaperTextView,resourceTextView,practicalFileTextView;
    private int choosenSubject;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //by using view you can find the id of the widgets present in R.layout.dialog_fragment

        View view=inflater.inflate(R.layout.dialog_fragment,container,false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//it will set the window of dialog transparent.
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);//This dialog doesn't contain any title
        getDialog().getWindow().setWindowAnimations(R.style.dialog_animation_fade);

        notesTextView=view.findViewById(R.id.notes);
        questionPaperTextView=view.findViewById(R.id.questionPaper);
        resourceTextView=view.findViewById(R.id.resources);
        practicalFileTextView=view.findViewById(R.id.practicalFiles);

        notesTextView.setOnClickListener(this);
        questionPaperTextView.setOnClickListener(this);
        resourceTextView.setOnClickListener(this);
        practicalFileTextView.setOnClickListener(this);

        // retrieve the choosen subject number passed to this fragment
        choosenSubject=getArguments().getInt("subject");

        return view;
    }

    public Dialog_fragment() {
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.notes:

                Toast.makeText(getActivity(), "notes selected", Toast.LENGTH_SHORT).show();
                moveToContentActivity(0);
                break;

            case R.id.questionPaper:
                Toast.makeText(getActivity(), "questionpaper selected", Toast.LENGTH_SHORT).show();
                moveToContentActivity(1);
                break;

            case R.id.practicalFiles:

                Toast.makeText(getActivity(), "practicalFiles selected", Toast.LENGTH_SHORT).show();
                //new DownloadDialogFragment().show(getFragmentManager(),"Download dialog");
                moveToContentActivity(2);
                break;

            case R.id.resources:
                Toast.makeText(getActivity(), "resources selected", Toast.LENGTH_SHORT).show();
                moveToContentActivity(3);
                break;
                }

    }

    public void moveToContentActivity(int type)
    {
        //This below code will dismiss the dialogFragment after pressing the button
        DialogFragment dialog = (DialogFragment)getFragmentManager().findFragmentByTag("dialog_fragment");
        if(NetworkCheck.isNetworkAvailable(getActivity())){
            //Move the user into notes activity
            Log.i("moving...","to contentActivity with context"+getActivity());
            Intent intent = new Intent(getActivity(),ContentsActivity.class);
            intent.putExtra("type",type);
            intent.putExtra("subject",choosenSubject);
            startActivity(intent);
            //This below code will dismiss the dialogFragment after pressing the button
            dialog.dismiss();//it will dismiss the fragment
        }
        else {
            InternetAlertDialogfragment internetAlertDialogfragment = new InternetAlertDialogfragment();
            internetAlertDialogfragment.show(getFragmentManager().beginTransaction(), "net_dialog");
        }
    }

    static Dialog_fragment getInstance(int i)
    {
        Bundle bundle = new Bundle();
        bundle.putInt("subject",i);
        Dialog_fragment fragment = new Dialog_fragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
