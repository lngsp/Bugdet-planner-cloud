package ro.planner.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import ro.planner.clientapp.R;
import ro.planner.api.UserAPI;

public class HomeUserFragment extends Fragment {
    //Variables
    private String username;
    private TextView txtAmount, txtEdit;
    private EditText etxtAmount;
    private Button btnSaveAmount;

    private UserAPI userAPI = new UserAPI();
    private Context fragmentContext;

    //Constructor
    public HomeUserFragment(){}

    public HomeUserFragment(String username){
        this.username = username;
    }

    //Methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_user, container, false);

        //remove the status bar
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bundle args = getArguments();
        if (args != null) {
            username = args.getString("username");
        }

        txtAmount = view.findViewById(R.id.txtAmount);
        txtEdit = view.findViewById(R.id.txtEdit);
        etxtAmount = view.findViewById(R.id.etxtAmount);
        btnSaveAmount = view.findViewById(R.id.btnSaveAmount);

        //get the amount if exits
        userAPI.getTotalAmount(fragmentContext, username, new UserAPI.UserCallback() {
            @Override
            public void onSuccess(Double totalAmount) {
                txtAmount.setText(totalAmount.toString());
            }

            @Override
            public void onSuccessEmail(String email) {

            }

            @Override
            public void onError(String errorMessage) {
                //etxtAmount.setError("Amount not found!");
            }
        });

        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAmount.setVisibility(View.GONE);
                etxtAmount.setVisibility(View.VISIBLE);
                btnSaveAmount.setVisibility(View.VISIBLE);
                etxtAmount.setText(txtAmount.getText());
            }
        });

        btnSaveAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAmount.setText(etxtAmount.getText().toString());
                txtAmount.setVisibility(View.VISIBLE);
                etxtAmount.setVisibility(View.GONE);
                btnSaveAmount.setVisibility(View.GONE);

                userAPI.setTotalAmount(fragmentContext, username, Double.valueOf(txtAmount.getText().toString()));
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentContext = null;
    }
}