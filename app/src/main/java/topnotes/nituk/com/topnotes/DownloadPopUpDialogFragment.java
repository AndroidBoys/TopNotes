package topnotes.nituk.com.topnotes;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class DownloadPopUpDialogFragment extends DialogFragment implements View.OnClickListener {

    protected TextView notesTextView,questionPaperTextView,resourceTextView,practicalFileTextView;
    private int choosenSubject;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //by using view you can find the id of the widgets present in R.layout.dialog_fragment
        View view=inflater.inflate(R.layout.dialog_fragment,container,false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setWindowAnimations(R.style.dialog_animation_fade);

        notesTextView=view.findViewById(R.id.notes);
        questionPaperTextView=view.findViewById(R.id.questionPaper);
        resourceTextView=view.findViewById(R.id.resources);
        practicalFileTextView=view.findViewById(R.id.practicalFiles);

        notesTextView.setOnClickListener(this);
        questionPaperTextView.setOnClickListener(this);
        resourceTextView.setOnClickListener(this);
        practicalFileTextView.setOnClickListener(this);

        choosenSubject=getArguments().getInt("subject");

        return view;
    }

    public DownloadPopUpDialogFragment() {
    }

    @Override
    public void onClick(View view) {
        //This below code will dismiss the dialogFragment after pressing the button
        DialogFragment dialog = (DialogFragment)getFragmentManager().findFragmentByTag("download_dialog");

        switch (view.getId()){

            case R.id.notes:
                //Go to downloaded notes  of user

                addDifferentFragments(DownloadfinalFragment.getInstance(choosenSubject,0));
               //it will dismiss the fragment

                break;

            case R.id.questionPaper:
                //Go to downloaded question Paper  of user
                //dialog.dismiss();//it will dismiss the fragment
                addDifferentFragments(DownloadfinalFragment.getInstance(choosenSubject,1));
                break;

            case R.id.practicalFiles:
                    //Go to downloaded Practical Files  of user
                //dialog.dismiss();//it will dismiss the fragment
                addDifferentFragments(DownloadfinalFragment.getInstance(choosenSubject,2));
                break;


            case R.id.resources:
                //Go to downloaded Resources  of user
                //dialog.dismiss();//it will dismiss the fragment
                addDifferentFragments(DownloadfinalFragment.getInstance(choosenSubject,3));
                break;



        }
        dialog.dismiss();

    }

    public static DownloadPopUpDialogFragment getInstance(int i){
        Bundle bundle = new Bundle();
        bundle.putInt("subject",i);
        DownloadPopUpDialogFragment fragment= new DownloadPopUpDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
   private void addDifferentFragments(Fragment replacableFragment){
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        // to set a custom animation in fragment
//        fragmentTransaction.setCustomAnimations(R.anim.fragment_open_enter,
//                R.anim.fragment_open_exit, R.anim.fragment_close_enter,
//                R.anim.fragment_close_exit);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.frameLayout,replacableFragment);
        fragmentTransaction.commit();
    }

}
