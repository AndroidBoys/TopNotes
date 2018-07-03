package topnotes.nituk.com.topnotes;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AboutUsFragment extends Fragment {

    Activity activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        activity=getActivity();
        View view=inflater.inflate(R.layout.about_us,container,false);
        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SubjectListActivity)activity).setActionBarTitle("About Us");
    }
}
