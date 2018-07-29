package topnotes.nituk.com.topnotes;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NothingToDisplayDialogFragment extends Fragment {
    private String updown;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.nothing_to_fetch,container,false);
        ImageView updownImageView=view.findViewById(R.id.updownImageView);
        TextView updownTextView=view.findViewById(R.id.updownTextView);
        if(updown.equals("upload")) {
            updownImageView.setImageResource(R.drawable.my_uploads);
            updownTextView.setText("Please upload a file first!");
        }
        else if(updown.equals("download")) {
            updownImageView.setImageResource(R.drawable.my_downloads);
            updownTextView.setText("Please download a file first!");
        }

       return view;
    }

    public NothingToDisplayDialogFragment() {
     }

    @SuppressLint("ValidFragment")
    public NothingToDisplayDialogFragment(String updown) {
        this.updown = updown;
    }
}
