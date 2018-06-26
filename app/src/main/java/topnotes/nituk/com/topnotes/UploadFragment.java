package topnotes.nituk.com.topnotes;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UploadFragment extends Fragment {


    private static final int FILE_SELECT_CODE = 0;
    private Button mChooseButton;
    private Button mUploadButton;
    private TextView mPathTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_upload,container,false);
        mChooseButton= view.findViewById(R.id.choosefilebutton);
        mUploadButton = view.findViewById(R.id.uploadbutton);
        mChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             chooseFile();
            }
        });
        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mPathTextView=view.findViewById(R.id.pathtextView);
        return view;
    }

    public UploadFragment()
    {

    }

    // Static method to get an instance of this fragment
    public static UploadFragment getInstance()
    {
        return new UploadFragment();
    }
    private void chooseFile()
    {
         Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
         intent.setType("*/*");
         intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog

            Toast.makeText(getContext(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void uploadFile()
    {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == FILE_SELECT_CODE && resultCode == Activity.RESULT_OK && data!=null)
        {
            Uri uri = data.getData();
            mPathTextView.setText(uri.getPath());
            Toast.makeText(getContext(),"File ready to upload  with uri:"+uri.getPath(),Toast.LENGTH_SHORT).show();

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
