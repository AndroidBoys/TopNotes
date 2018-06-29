package topnotes.nituk.com.topnotes;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class UploadDialogFragment extends DialogFragment {


    private static final int FILE_SELECT_CODE = 0;
    private ImageView chooseFileImageViewButton;
    private Button uploadButton;
    private StorageReference mStorageRef;
    private Uri fileUri;

    private ImageView uploadUserImageView;
    private TextView uploadUserNameTextView;
    private int choosenSubject;
    private int choosenType;

    private EditText titleEditText;

    public UploadDialogFragment()
    {
        if(fileUri!=null)
        {
            uploadFile(fileUri);
        }
        else
        {
            ////Toast.makeText(getContext(),"Please choose a file first",Toast.LENGTH_SHORT).show();
            //
        }
    }

    public static UploadDialogFragment getInstance(){
        return  new UploadDialogFragment();
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog=super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
        //
    }

//    String spinnerList[]={"mohan","rohan","sohan","dohan","gohangohan gohan gohan","johan","pagal","dagal"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.duplicate,container,true);

        // setup the uploader profile on the upload fragment
        uploadUserImageView=view.findViewById(R.id.uploadUserImageView);
        uploadUserNameTextView=view.findViewById(R.id.uploadUserName);
        uploadUserNameTextView.setText(User.getUser().getName());
        Picasso.get().load(User.getUser().getImageUrl()).into(uploadUserImageView);


        uploadButton=view.findViewById(R.id.uploadButton);
        chooseFileImageViewButton=view.findViewById(R.id.chooseFileImageViewButton);
        mStorageRef= FirebaseStorage.getInstance().getReference();


        titleEditText=view.findViewById(R.id.uploadTitleEditText);



        //To show dropdown list in our app we need to use spinner widget.
        final Spinner subjectSpinner=view.findViewById(R.id.subjectSpinner);
        ArrayAdapter<String> subjectSpinnerAdapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.spinnerSubjectList));
        subjectSpinner.setAdapter(subjectSpinnerAdapter);
        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                choosenSubject=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final Spinner categorySpinner=view.findViewById(R.id.categorySpinner);
        ArrayAdapter<String> categorysSpinnerAdapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.spinnerCategoryList));
        categorySpinner.setAdapter(categorysSpinnerAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                choosenType=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        //Below code will choose a file from android system
        chooseFileImageViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkCheck.isNetworkAvailable(getActivity())) {
                    chooseFile();
                }else{
                    InternetAlertDialogfragment internetAlertDialogfragment = new InternetAlertDialogfragment();
                    internetAlertDialogfragment.show(getFragmentManager().beginTransaction(), "dilog");
                }
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fileUri==null){
                    Toast.makeText(getActivity(), "Please first choose the file", Toast.LENGTH_SHORT).show();
                }
                else if(getResources().getStringArray(R.array.spinnerSubjectList)[subjectSpinner.getSelectedItemPosition()].equals("Select")){
                    Toast.makeText(getActivity(),"Please first Choose subject !",Toast.LENGTH_SHORT).show();
                }else if(getResources().getStringArray(R.array.spinnerCategoryList)[categorySpinner.getSelectedItemPosition()].equals("Select")){
                    Toast.makeText(getActivity(),"Please first Choose category !",Toast.LENGTH_SHORT).show();
                }else if(titleEditText.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Please Write the title name !",Toast.LENGTH_SHORT).show();
                }
                else{
                    uploadFile(fileUri);
                    DialogFragment dialog = (DialogFragment)getFragmentManager().findFragmentByTag("upload");
                    dialog.dismiss();
                }
            }
        });
        return view;
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

    private void uploadFile(Uri uri)
    {

        //Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        StorageReference riversRef = mStorageRef.child("courses")
                .child(getResources().getStringArray(R.array.subjectList)[choosenType])
                .child(getResources().getStringArray(R.array.categoryList)[choosenType])
                .child(titleEditText.getText().toString());
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        riversRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(getActivity(), new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(getActivity(),"File uploaded successfully, file url:"+uri.toString(),Toast.LENGTH_SHORT).show();
                                Log.i("Upload success, Url:",uri.toString());
                                makeEntryToFBDB(uri.toString());
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


                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage((int)progress+"% Uploaded");
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == FILE_SELECT_CODE && resultCode == Activity.RESULT_OK && data!=null)
        {
            fileUri = data.getData();
            //mPathTextView.setText(fileUri.getPath());
            Toast.makeText(getContext(),"File ready to upload  with uri:"+fileUri.getPath(),Toast.LENGTH_SHORT).show();

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    // The method saves the upload file's metadata to firebasedatabase
    public void makeEntryToFBDB(String url)
    {   UUID contentUUID = UUID.randomUUID();
        Content content = new Content();
        content.setTitle(titleEditText.getText().toString());
        content.setDate(DateFormat.getDateFormat(getActivity()).format(new Date()));
        content.setAuthor(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        content.setDownloadUrl(url);
        FirebaseDatabase.getInstance().getReference("courses").child(getResources().getStringArray(R.array.subjectToken)[choosenSubject])
                .child(getResources().getStringArray(R.array.typeToken)[choosenType])
                .child(contentUUID.toString())
                .setValue(content);
    }
}

