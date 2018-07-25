package topnotes.nituk.com.topnotes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;

import static android.content.Context.INPUT_METHOD_SERVICE;

/***
PRO TIP: WHEN NETWORKING ON FRAGMENT AND TRYING TO ACCESS THE SYSTEM RESOURCES , BE CAREFUL WITH THE CONTEXT..
 AS THE HOSTING ACTIVITY MAY HAVE BEEN DETACHED ON COMPLETION OF NETWORK TASK
 ***/
public class UploadDialogFragment extends DialogFragment implements View.OnClickListener {


    private static final int FILE_SELECT_CODE = 0;
    public  static final int REQ_CODE =1 ;
    private ImageView chooseFileImageViewButton;
    private Button uploadButton;
    private StorageReference mStorageRef;
    private Uri fileUri;

    private ImageView uploadUserImageView;
    private TextView uploadUserNameTextView;
    private int choosenSubject;
    private int choosenType;
    private Activity activity;
    private LinearLayout superLinearLayout;
    private Spinner subjectSpinner,categorySpinner;
    private String notesNameType;
    private String subjectNameType;



    private EditText titleEditText;

    public UploadDialogFragment()
    {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog=super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//       dialog.getWindow().setLocalFocus(true,true);
        return dialog;
        //
    }

    public static UploadDialogFragment getInstance()
    {
        return new UploadDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.upload_dialog_fragment,container,true);
      // This context should be use throughout the fragment to access any resources related to the hosting activity
        // There was a bug in uploading due to null (getActivity) which occurs due to completion of HTTP request and detachment of
        // the base activity
        activity=getActivity();

        // setup the uploader profile on the upload fragment
        uploadUserImageView=view.findViewById(R.id.uploadUserImageView);
        uploadUserNameTextView=view.findViewById(R.id.uploadUserName);
        uploadUserNameTextView.setText(User.getUser().getName());
        Picasso.get().load(User.getUser().getImageUrl()).into(uploadUserImageView);
        superLinearLayout=view.findViewById(R.id.superUploadFragmentLinearLayout);


        uploadButton=view.findViewById(R.id.uploadButton);
        chooseFileImageViewButton=view.findViewById(R.id.chooseFileImageViewButton);
        mStorageRef= FirebaseStorage.getInstance().getReference();

        superLinearLayout.setOnClickListener(this);
        titleEditText=view.findViewById(R.id.uploadTitleEditText);
        titleEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i==KeyEvent.KEYCODE_ENTER&&keyEvent.getAction()==KeyEvent.ACTION_DOWN)
                onClick(uploadButton);
                return false;
            }
        });

        Log.i("activity:::",getActivity().toString());



        //To show dropdown list in our app we need to use spinner widget.
        subjectSpinner=view.findViewById(R.id.subjectSpinner);
        ArrayAdapter<String> subjectSpinnerAdapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.spinnerSubjectList));
        subjectSpinner.setAdapter(subjectSpinnerAdapter);
        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // since the select option is also included
                Log.d("coosenSubject",""+i);
                choosenSubject=i-1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        categorySpinner=view.findViewById(R.id.categorySpinner);
        ArrayAdapter<String> categorysSpinnerAdapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.spinnerCategoryList));
        categorySpinner.setAdapter(categorysSpinnerAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               // since the select option is also included..
                Log.d("catagory",""+i);

                choosenType=i-1;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        chooseFileImageViewButton.setOnClickListener(this);

        uploadButton.setOnClickListener(this);

        return view;
    }


    private void chooseFile()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
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

    private void uploadFile(Uri uri)
    {
        subjectNameType=getResources().getStringArray(R.array.subjectList)[choosenSubject];

        notesNameType=getResources().getStringArray(R.array.categoryList)[choosenType];

        //using current time to set title so their will be no title of similar names
           final String dateTime=UUID.randomUUID().toString();
        //Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        StorageReference riversRef = mStorageRef.child("courses")
                .child(getResources().getStringArray(R.array.subjectList)[choosenSubject])
                .child(getResources().getStringArray(R.array.categoryList)[choosenType])
                .child(titleEditText.getText().toString()+"_"+dateTime);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Uploading...");
        //Log.i("activity again::",getActivity().toString());
        // context for the sucess listener
        progressDialog.show();
        riversRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content

                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(activity,new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(activity,"File uploaded successfully, file url:"+uri.toString(),Toast.LENGTH_SHORT).show();
                                Log.i("Upload success, Url:",uri.toString());
                                makeEntryToFBDB(uri.toString(),dateTime);
                                progressDialog.dismiss();
                                fileUri=null;
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(getActivity(),"File upload Failed,"+ exception.getMessage(),Toast.LENGTH_SHORT).show();
                        exception.printStackTrace();
                        progressDialog.dismiss();


                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage((int)progress+"% Uploaded");
            }
        });

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == FILE_SELECT_CODE && resultCode == Activity.RESULT_OK && data!=null)
        {
            fileUri = data.getData();
            Log.d("urifile",data.getData().toString());
            //mPathTextView.setText(fileUri.getPath());
            Toast.makeText(getContext(),"File ready to upload  with uri:"+fileUri.getPath(),Toast.LENGTH_SHORT).show();

        }

     }

    // The method saves the upload file's metadata to firebasedatabase
    public void makeEntryToFBDB(String url,String dateTime)
    {
        UUID contentUUID = UUID.randomUUID();
        Content content = new Content();
        content.setTitle(titleEditText.getText().toString()+"_"+dateTime);
        content.setDate(DateFormat.getDateFormat(activity).format(new Date()));
        content.setAuthor(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        content.setDownloadUrl(url);
        // update the myupload list
        sendResult(content);
        FirebaseDatabase.getInstance().getReference("courses").child(activity.getResources().getStringArray(R.array.subjectToken)[choosenSubject])
                .child(activity.getResources().getStringArray(R.array.typeToken)[choosenType])
                .child(contentUUID.toString())
                .setValue(content);
        createNotification();

    }
    private void sendResult(Content content)
    {
        Intent intent = new Intent();
        intent.putExtra("content",content);
        Log.i("content with title:",content.getTitle());

        //return the result to that fragment which create this fragment
        getTargetFragment().onActivityResult(getTargetRequestCode(),REQ_CODE,intent);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.uploadButton:

                if (fileUri == null) {
                    Toast.makeText(getActivity(), "Please first choose the file", Toast.LENGTH_SHORT).show();
                } else if (activity.getResources().getStringArray(R.array.spinnerSubjectList)[subjectSpinner.getSelectedItemPosition()].equals("Select")) {
                    Toast.makeText(getActivity(), "Please first Choose subject !", Toast.LENGTH_SHORT).show();
                } else if (activity.getResources().getStringArray(R.array.spinnerCategoryList)[categorySpinner.getSelectedItemPosition()].equals("Select")) {
                    Toast.makeText(getActivity(), "Please first Choose category !", Toast.LENGTH_SHORT).show();
                } else if (titleEditText.getText().toString().matches("")) {
                    Toast.makeText(getActivity(), "Please Write the title name !", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile(fileUri);
                    DialogFragment dialog = (DialogFragment) getFragmentManager().findFragmentByTag("upload");
                    dialog.dismiss();
                }
                break;

            case R.id.chooseFileImageViewButton:
                //Below code will choose a file from android system

                if (NetworkCheck.isNetworkAvailable(getActivity())) {
                    chooseFile();
                } else {
                    InternetAlertDialogfragment internetAlertDialogfragment = new InternetAlertDialogfragment();
                    internetAlertDialogfragment.show(getFragmentManager().beginTransaction(), "net_dialog");
                }
                break;
            case R.id.superUploadFragmentLinearLayout:
                InputMethodManager inputMethodManager=(InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(titleEditText.getWindowToken(),0);
                break;

        }
    }

    private void createNotification(){

        NotificationCompat.Builder builder=new NotificationCompat.Builder(activity);
        builder.setSmallIcon(R.drawable.notes);
        builder.setContentTitle("New Notes Uploaded");
        //builder.setContentText(getResources().getStringArray(R.array.subjectList)[choosenSubject] + getResources().getStringArray(R.array.subjectList)[choosenType] +" uploaded ..You can download it..");
        builder.setContentText(subjectNameType+" "+notesNameType+" uploaded ..You can download it..");
        Uri defaultRingtone=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(defaultRingtone);
        Intent intent=new Intent(activity,SubjectListActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(activity,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager=(NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());

    }

    }

