package topnotes.nituk.com.topnotes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DownloadDialogFragment extends Dialog_fragment{

    private TextView titleTextView;
    private TextView subjectTextView;
    private TextView sizeTextView;
    private TextView downloadsTextView;
    private TextView authorTextView;
    private TextView dateTextView;
    private TextView creditsTextView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        //it will remove title bar from the dialog
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setWindowAnimations(R.style.dialog_animation_fade);

        View view=inflater.inflate(R.layout.download_dialog_fragment,container,false);
        Button downloadButton=view.findViewById(R.id.downloadButton);


        titleTextView =view.findViewById(R.id.title);
        subjectTextView = view.findViewById(R.id.subject);
        authorTextView = view.findViewById(R.id.author);
        dateTextView = view.findViewById(R.id.date);
        sizeTextView = view.findViewById(R.id.size);
        creditsTextView = view.findViewById(R.id.credits);



        final Content content = (Content)getArguments().getSerializable("content");
        titleTextView.setText(content.getTitle());
        subjectTextView.setText("Subject");
        authorTextView.setText(content.getAuthor());
        sizeTextView.setText("4.5mb");
        creditsTextView.setText("me :)");
        dateTextView.setText(content.getDate());

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Download the content
                Toast.makeText(getActivity(),"::Download process begins:: with url:"+content.getDownloadUrl(),Toast.LENGTH_SHORT).show();
                Log.i("url:",content.getDownloadUrl());
                new ContentDownloader(getActivity()).downloadFile(content.getDownloadUrl(),content.getTitle());
            }
        });

        return view;

    }

    public DownloadDialogFragment() {
    }

    public static DownloadDialogFragment getInstance(Content content){
        Bundle bundle = new Bundle();
        bundle.putSerializable("content",content);
        DownloadDialogFragment fragment=new DownloadDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}