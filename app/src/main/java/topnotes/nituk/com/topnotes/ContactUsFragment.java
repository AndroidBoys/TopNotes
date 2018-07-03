package topnotes.nituk.com.topnotes;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class ContactUsFragment extends Fragment implements View.OnClickListener{

    public ContactUsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.contact_us,container,false);
        final Button sendFeedBackButton=view.findViewById(R.id.sendFeedbackButton);
        LinearLayout linearLayout=view.findViewById(R.id.superContentLayout);
        linearLayout.setOnClickListener(this);
        sendFeedBackButton.setOnClickListener(this);

        return view;
    }

    public static ContactUsFragment getInstance(){
        return new ContactUsFragment();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.superContentLayout:
                InputMethodManager inputMethodManager=(InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);

                break;
            case R.id.sendFeedbackButton:
                //intent to gmail
                break;
        }
    }
}
