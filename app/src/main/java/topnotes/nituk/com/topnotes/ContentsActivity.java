package topnotes.nituk.com.topnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ContentsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ContentAdapter mContentAdapter;
    private List<Content> contents;
    private Content mContent;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        mRecyclerView= findViewById(R.id.contentsRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // set Adapter to the recycler view with appropriate dataset
        Log.i("onCreate::","withing contentActivity");
        updateUI();
        // get the firebase storage
        firebaseStorage = FirebaseStorage.getInstance();


    }



    // ViewHolder for the recycler view which inflates our own view
    private class ContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView,mAuthorTextView,mDateTextView;

        public ContentHolder(LayoutInflater inflater, ViewGroup container)
       {
           super(inflater.inflate(R.layout.recyclerview_content_raw_layout,container,false));
           itemView.setOnClickListener(this);
           // get reference to the views using the viewholder when the viewholders are created here

           mTitleTextView=itemView.findViewById(R.id.recyclerNotesNameTextView);
           mAuthorTextView=itemView.findViewById(R.id.recyclerAuthorNameTextView);
           mDateTextView=itemView.findViewById(R.id.uploadDateTextView);

       }
        // The method binds the data to the viewholder
        public void bind(Content content)
        {
            mContent = content;
            // bind your data to the views here
            mTitleTextView.setText(content.getTitle());
            mAuthorTextView.setText("Author : "+content.getAuthor());
            mDateTextView.setText("Upload Date : "+content.getDate());
        }

        // implement the recycler view list item click action here
        @Override
        public void onClick(View view) {
            new DownloadDialogFragment().show(getSupportFragmentManager(),"Download dialog");

            }


    }
    // Adapter for recycler view

    private class ContentAdapter extends RecyclerView.Adapter<ContentHolder>  {

        private List mContents;
        public ContentAdapter(List<Content> contents)
        {
           mContents =  contents;
        }

        @NonNull
        @Override
        public ContentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(ContentsActivity.this);
            return new ContentHolder(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ContentHolder holder, int position) {
          Content content = (Content)mContents.get(position);
          holder.bind(content);

        }

        @Override
        public int getItemCount() {
            return mContents.size();
        }


    }
    // The method gets the list of all Content objects
    public void updateUI()
    {
       ContentLab contentLab = ContentLab.getInstance(this);
       List<Content> contents = contentLab.getContents();
       Log.i("updating ui",contents.toString());
       mContentAdapter = new ContentAdapter(contents);
       mRecyclerView.setAdapter(mContentAdapter);
    }

}
