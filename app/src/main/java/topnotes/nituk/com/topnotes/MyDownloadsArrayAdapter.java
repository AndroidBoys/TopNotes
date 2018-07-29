package topnotes.nituk.com.topnotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.snatik.storage.Storage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MyDownloadsArrayAdapter extends ArrayAdapter<String> {


    private List<Content> contentList;
    private Context context;
    private int choosenSubject;
    private int choosenType;

    public MyDownloadsArrayAdapter(Context context,List<Content> contentList,List<String> contentTitleList,int choosenSubject,int choosenType) {
        super(context,-1,contentTitleList);
        this.contentList = contentList;
        this.context=context;
        this.choosenSubject=choosenSubject;
        this.choosenType=choosenType;
    }


    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.downloads_listview_raw_layout, parent, false);

        TextView notesNameTextView=rowView.findViewById(R.id.notesNameTextView);
        TextView authorsNameTextView=rowView.findViewById(R.id.authorsNameTextView);

        LinearLayout clickableLinearLayout = rowView.findViewById(R.id.clickableLinearLayout);

        ImageView deleteImageView = rowView.findViewById(R.id.delete);
        ImageView shareImageView = rowView.findViewById(R.id.share);


        clickableLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View parentRow = (View)view.getParent();
                ListView listView = (ListView) parentRow.getParent();
                int choosenFile = listView.getPositionForView(parentRow);

                Log.i("choosenFile+open",Integer.toString(choosenFile));

                openFile(MyApplication.getApp().subjectNames.get(choosenSubject)+"/"
                        +context.getResources().getStringArray(R.array.categoryList)[choosenType]
                       +"/" +contentList.get(choosenFile).getFileName());
                Log.i("clicked:", "" + choosenFile);
                Log.i("file:", contentList.get(choosenFile).getFileName());
            }
        });

        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View parentView= (View)view.getParent();
                LinearLayout linearLayout = (LinearLayout) parentView.getParent();
                LinearLayout outerLinearLayout= (LinearLayout) linearLayout.getParent();
                ListView listView =(ListView) outerLinearLayout.getParent();
                int choosenFile = listView.getPositionForView(parentView);
                Log.i("choosenFile+delete",Integer.toString(choosenFile));

                deleteAction("/TopNotes/"+MyApplication.getApp().subjectNames.get(choosenSubject)+"/"
                        +context.getResources().getStringArray(R.array.categoryList)[choosenType]+"/"
                        +contentList.get(choosenFile).getFileName(),choosenFile);
            }
        });

        shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View parentView= (View)view.getParent();
                LinearLayout linearLayout = (LinearLayout) parentView.getParent();
                LinearLayout outerLinearLayout= (LinearLayout) linearLayout.getParent();
                ListView listView =(ListView) outerLinearLayout.getParent();
                int choosenFile = listView.getPositionForView(parentView);
                Log.i("choosenFile+share",Integer.toString(choosenFile));

                shareFile("TopNotes/"+MyApplication.getApp().subjectNames.get(choosenSubject)+"/"
                        +context.getResources().getStringArray(R.array.categoryList)[choosenType],choosenFile);//+"/"
                        //+downloadsNotesNameArray.get(position)+".pdf");
            }
        });

        notesNameTextView.setText(contentList.get(position).getTitle());
        authorsNameTextView.setText("Author : "+contentList.get(position).getAuthor());

        return rowView;
    }
//
//    private void openFile(String file)
//    {
//
//        File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath(),"TopNotes/"+file);
//        Log.i("pdfFile:",pdfFile.toString());
//        Uri path = Uri.fromFile(pdfFile);
//        Log.i("uri:",path.toString());
//
//        // Setting the intent for the file
//        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
//
//
//
//        // for opening pdf files
//        pdfIntent.setDataAndType(path, "application/pdf");
//        //pdfIntent.setDataAndType(path, "application/pdf");
//        // for opening images
//        //pdfIntent.setDataAndType(path, "image/*");
//        // If the instance of pdf reader already exists
//        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        // for api>24
//        pdfIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//        context.startActivity(Intent.createChooser(pdfIntent,"Open with"));
//    }

    private void openFile(String file)
    {
        File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath(),"TopNotes/"+file);
        Log.i("pdfFile:",pdfFile.toString());
        Uri path = Uri.fromFile(pdfFile);
        Log.i("uri:",path.toString());

        Intent intent = new Intent(Intent.ACTION_VIEW);
        String mimeType = getMimeType(path.getPath());
        Log.i("path",path.getPath());
//        Log.i("mimetype",mimeType);
        if(android.os.Build.VERSION.SDK_INT >=24) {
            Uri fileURI = FileProvider.getUriForFile(getContext(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    pdfFile);
            intent.setDataAndType(fileURI, mimeType);

        }else {
            intent.setDataAndType(path, mimeType);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(Intent.createChooser(intent,"Open with"));
        }catch (ActivityNotFoundException e){
            Toast.makeText(context, "No Application found to open this type of file.", Toast.LENGTH_LONG).show();

        }


    }


    private void deleteAction(final String path, final int choosenFile)
    {
        new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_launcher_background)
                .setTitle("Delete")
                .setMessage("Do you really want to delete?")
                .setPositiveButton("Yes",new AlertDialog.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            deleteFile(path,choosenFile);
                    }
                })
                .setNegativeButton("No",null)
                .show();
    }

    private void deleteFile(String path, int choosenFile)
    {
       File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),path);
//       File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),path);
        // File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"EEM.pdf");
        if(file.exists()) {
             Toast.makeText(context,"starting deletion!",Toast.LENGTH_SHORT).show();
             DocumentFile documentFile = DocumentFile.fromFile(file);
             if(documentFile.delete())//if file will delete then it will return true
             {   deleteFromListView(choosenFile);
                 Toast.makeText(context,"sucess!"+!file.exists(),Toast.LENGTH_SHORT).show();
             }
             else{
                 Toast.makeText(context,"No success!"+!file.exists(),Toast.LENGTH_SHORT).show();
             }
        } else {
         Toast.makeText(context,"it's weird file doesn't exist!",Toast.LENGTH_SHORT).show();
       }
    }


    private void deleteFromListView(int choosenFile)
    {

//        this.remove(this.getItem(choosenFile));
//        this.notifyDataSetChanged();//it will notify the adapter
        contentList.remove(choosenFile);
        notifyDataSetChanged();
    }

    private void shareFile(String filePath,int position) {

        //it is necessary to put two arguments in class File constructor one is parent and other one is child.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),filePath+"/"+contentList.get(position).getFileName());
       // File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"downloads/EEM.pdf");

        if(file.canRead()) {
            Log.i("yes:","true");
        }
        else {
            Log.i("no:","mrja");

        }

        Log.i("file:",file.toString());

           Uri uri = FileProvider.getUriForFile(context,context.getPackageName()+".provider",file);

            Log.i("path segments",uri.getPathSegments().toString());
            Log.i("uri:",uri.toString());
//            Intent share = new Intent();
//            share.setAction(Intent.ACTION_SEND);
//            share.setType("application/pdf");
//            share.putExtra(Intent.EXTRA_STREAM, uri);
//            context.grantUriPermission("com.whatsapp",uri,Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            context.revokeUriPermission(uri,Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            share.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
////            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
////            share.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            context.startActivity(Intent.createChooser(share, "Share File"));

//
        Intent intent = ShareCompat.IntentBuilder.from((Activity) context)
                .setStream(uri) // uri from FileProvider
                .setType("application/pdf")
                .getIntent()
                .setAction(Intent.ACTION_SEND) //Change if needed
                .setDataAndType(uri, "application/pdf")
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_STREAM,uri);

        //This below code is used to grantPermission for all other sharing apps
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        context.startActivity(intent);


    }

    public  String getMimeType(String url) {
        String type = null;
        String extension = url.substring(url.lastIndexOf(".") + 1);
        Log.i("extension",extension);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    @Override
    public int getCount() {
        return contentList.size();
    }
}
