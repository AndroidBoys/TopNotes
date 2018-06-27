package topnotes.nituk.com.topnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ContentsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ContentAdapter mContentAdapter;
    private List<Content> mContents;
    private Content mContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        mRecyclerView= findViewById(R.id.contentsRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // set Adapter to the recycler view with appropriate dataset
        updateUI();


    }
    // ViewHolder for the recycler view which inflates our own view
    private class ContentHolder extends RecyclerView.ViewHolder{

       public ContentHolder(LayoutInflater inflater, ViewGroup container)
       {
           super(inflater.inflate(R.layout.activity_subject_list,container,false));
           // get reference to the views using the viewholder when the viewholders are created here

       }


    }
    // Adapter for recycler view

    private class ContentAdapter extends RecyclerView.Adapter<ContentHolder> implements View.OnClickListener {

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
          bind(content);

        }

        @Override
        public int getItemCount() {
            return mContents.size();
        }
        // implement the recycler view list item click action here
        @Override
        public void onClick(View view) {

        }
    }
    // The method gets the list of all Content objects
    public void updateUI()
    {
       ContentLab contentLab = ContentLab.getInstance(this);
       List<Content> contents = contentLab.getContents();
       mContentAdapter = new ContentAdapter(contents);
       mRecyclerView.setAdapter(mContentAdapter);
    }
    // The method binds the data to the viewholder
    public void bind(Content content)
    {
       mContent = content;
       // bind your data to the views here
    }
}
