package topnotes.nituk.com.topnotes;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class UploadDialogFragment extends DialogFragment {


    private static final int FILE_SELECT_CODE = 0;
    private ImageView chooseFileImageViewButton;
    private Button uploadButton;
    private StorageReference mStorageRef;
    private Uri fileUri;

    public UploadDialogFragment()
    {
        if(fileUri!=null)
        {
            uploadFile(fileUri);
        }
        else
        {
            //Toast.makeText(getContext(),"Please choose a file first",Toast.LENGTH_SHORT).show();
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

        uploadButton=view.findViewById(R.id.uploadButton);
        chooseFileImageViewButton=view.findViewById(R.id.chooseFileImageViewButton);
        mStorageRef= FirebaseStorage.getInstance().getReference();


        //To show dropdown list in our app we need to use spinner widget.
        Spinner subjectSpinner=view.findViewById(R.id.subjectSpinner);
        ArrayAdapter<String> subjectSpinnerAdapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.subjectList));
        subjectSpinner.setAdapter(subjectSpinnerAdapter);

        Spinner categorySpinner=view.findViewById(R.id.categorySpinner);
        ArrayAdapter<String> categorysSpinnerAdapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.categoryList));
        categorySpinner.setAdapter(categorysSpinnerAdapter);



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
                if(fileUri!=null) {
                    uploadFile(fileUri);
                }else {
                    Toast.makeText(getActivity(), "Please first choose the file", Toast.LENGTH_SHORT).show();
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
        StorageReference riversRef = mStorageRef.child("Notes/test1");
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
}

