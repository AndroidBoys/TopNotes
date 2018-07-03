package topnotes.nituk.com.topnotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MyDownloadsArrayAdapter extends ArrayAdapter<String> {



    private List<String> downloadsNotesNameArray;
    private ArrayList<String> getDownloadsAuthorsNameArray;
    private Context context;
    private int choosenSubject;
    private int choosenType;
    private int choosenFile;

    public MyDownloadsArrayAdapter(Context context, List<String> downloadsNotesNameArray, ArrayList<String> getDownloadsAuthorsNameArray,int choosenSubject,int choosenType) {
        super(context,-1,downloadsNotesNameArray);
        this.downloadsNotesNameArray=downloadsNotesNameArray;
        this.getDownloadsAuthorsNameArray=getDownloadsAuthorsNameArray;
        this.context=context;
        this.choosenSubject=choosenSubject;
        this.choosenType=choosenType;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.downloads_listview_raw_layout, parent, false);

        TextView notesNameTextView=rowView.findViewById(R.id.notesNameTextView);
        TextView authorsNameTextView=rowView.findViewById(R.id.authorsNameTextView);

        LinearLayout clickableLinearLayout = rowView.findViewById(R.id.clickableLinearLayout);

        ImageView deleteImageView = rowView.findViewById(R.id.delete);
        ImageView shareImageView = rowView.findViewById(R.id.share);

        choosenFile=position;

        clickableLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile(context.getResources().getStringArray(R.array.subjectList)[choosenSubject]+"/"
                        +context.getResources().getStringArray(R.array.categoryList)[choosenType]+"/"
                        +downloadsNotesNameArray.get(position)+".pdf");
                Log.i("clicked:", "" + position);
                Log.i("file:", downloadsNotesNameArray.get(position));
            }
        });

        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            // deleteAction();
            }
        });

        shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                shareFile(context.getResources().getStringArray(R.array.subjectList)[choosenSubject]+"/"
//                        +context.getResources().getStringArray(R.array.categoryList)[choosenType]+"/"
//                        +downloadsNotesNameArray.get(position)+".pdf");
            }
        });

        notesNameTextView.setText(downloadsNotesNameArray.get(position));
        authorsNameTextView.setText("Author : "+getDownloadsAuthorsNameArray.get(position));

        return rowView;
    }

    public  void openFile(String file)
    {

        File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath(),"TopNotes/"+file);
        Log.i("pdfFile:",pdfFile.toString());
        Uri path = Uri.fromFile(pdfFile);
        Log.i("uri:",path.toString());

        // Setting the intent for the file
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        // for opening pdf files
        pdfIntent.setDataAndType(path, "application/pdf");
        // for opening images
        //pdfIntent.setDataAndType(path, "image/*");
        // If the instance of pdf reader already exists
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // for api>24
        pdfIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


        context.startActivity(pdfIntent);
    }
    public void deleteAction()
    {
        new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_launcher_background)
                .setTitle("Delete")
                .setMessage("Do you really want to delete?")
                .setPositiveButton("Yes",new AlertDialog.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            deleteFile();
                    }
                })
                .setNegativeButton("No",null)
                .show();
    }

    public void deleteFile()
    {
//        File file = new File("Documents"+"/TopNotes/"+context.getResources().getStringArray(R.array.subjectList)[choosenSubject]+"/"
//                +context.getResources().getStringArray(R.array.categoryList)[choosenType]+"/"
//                +downloadsNotesNameArray.get(choosenFile)+".pdf");
//        Uri fileUri = FileProvider.getUriForFile(context,
//                context.getApplicationContext().getPackageName() +
//                        ".provider", file);
//        ContentResolver contentResolver = context.getContentResolver();
//        contentResolver.delete(fileUri, null, null);

//        if(!file.exists())
//        {
//            Toast.makeText(context,"File deleted successfully!",Toast.LENGTH_SHORT).show();
//            deleteFromListView();
//            deleteFromDB();
//        }
//        else
//        {
//            Toast.makeText(context,"File deletion unsuccessful!",Toast.LENGTH_SHORT).show();
//
//        }
    }
    public void deleteFromDB()
    {

    }

    public void deleteFromListView()
    {
        this.remove(this.getItem(choosenFile));
        this.notifyDataSetChanged();
    }
    public void shareFile(String file)
    {
//        Log.d(TAG, "initializing share notes");
//        File newFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath(),"TopNotes/"+file);
//        String authority =BuildConfig.APPLICATION_ID + ".fileprovider";
//        Log.d(TAG, "authority: " + authority);
//        Uri contentUri = FileProvider.getUriForFile(context, authority,newFile);
//
//        if (contentUri != null) {
//            Intent shareIntent = ShareCompat.IntentBuilder.from((Activity) context)
//                    .setType("Application/pdf")
//                    .setStream(contentUri)
//                    .setSubject("TopNotes")
//                    .setText("Sent using TopNotes")
//                    .getIntent();
//            shareIntent.setData(contentUri);
//            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            context.startActivity(Intent.createChooser(
//                    shareIntent, "Share Notes"));
//        }

        File newFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),"TopNotes/"+file);
        Uri fileUri=FileProvider.getUriForFile(context,context.getPackageName()+".fileprovider",newFile);
        if(fileUri!=null)
        {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM,fileUri)
                       .setType("Application/pdf");
            context.startActivity(shareIntent);

        }

    }
}
