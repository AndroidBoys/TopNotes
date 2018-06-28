package topnotes.nituk.com.topnotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DownloadDialogFragment extends Dialog_fragment{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.download_dialog_fragment,container,false);
        Button downloadButton=view.findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "download", Toast.LENGTH_SHORT).show();
            }
        });

        return view;

    }

    public DownloadDialogFragment() {
    }

    public static DownloadDialogFragment getInstance(){
        return new DownloadDialogFragment();
    }
}