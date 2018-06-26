package topnotes.nituk.com.topnotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class Dialog_fragment extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            //by using view you can find the id of the widgets present in R.layout.dialog_fragment
        View view=inflater.inflate(R.layout.dialog_fragment,container,false);

        return view;
    }

    public Dialog_fragment() {
    }
}
