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

public class DownloadPopUpDialogFragment extends DialogFragment implements View.OnClickListener {

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

    public DownloadPopUpDialogFragment() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.notes:
                    //Go to downloaded notes  of user
                break;
            case R.id.resources:
                    //Go to downloaded Resources  of user
                break;
            case R.id.practicalFiles:
                    //Go to downloaded Practical Files  of user
                break;

            case R.id.questionPaper:
                //Go to downloaded question Paper  of user
                break;

        }

    }

    public static DownloadPopUpDialogFragment getInstance(){
        return new DownloadPopUpDialogFragment();
    }
}
