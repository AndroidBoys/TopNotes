package topnotes.nituk.com.topnotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class InternetAlertDialogfragment extends DialogFragment {


    public InternetAlertDialogfragment() {


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View  view=inflater.inflate(R.layout.internet_alert_dialog_fragment,container,false);
       Button retryButton;
       retryButton=view.findViewById(R.id.retryButton);
       retryButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Toast.makeText(getActivity(), "try again clicked", Toast.LENGTH_SHORT).show();
           }
       });
        return view;
    }
}
