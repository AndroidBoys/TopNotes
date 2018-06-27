package topnotes.nituk.com.topnotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        notesTextView=view.findViewById(R.id.notes);
        questionPaperTextView=view.findViewById(R.id.questionPaper);
        resourceTextView=view.findViewById(R.id.resources);
        practicalFileTextView=view.findViewById(R.id.practicalFiles);

        notesTextView.setOnClickListener(this);
        notesTextView.setOnClickListener(this);
        notesTextView.setOnClickListener(this);
        notesTextView.setOnClickListener(this);
        return view;
    }

    public Dialog_fragment() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.notes:
                Toast.makeText(getActivity(), "notes", Toast.LENGTH_SHORT).show();
                break;

            case R.id.resources:
                Toast.makeText(getActivity(), "notes", Toast.LENGTH_SHORT).show();
                break;
            case R.id.practicalFiles:
                Toast.makeText(getActivity(), "notes", Toast.LENGTH_SHORT).show();
                break;

            case R.id.questionPaper:
                Toast.makeText(getActivity(), "notes", Toast.LENGTH_SHORT).show();
                break;

        }

    }
}
