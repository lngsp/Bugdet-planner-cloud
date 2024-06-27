package ro.planner.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ro.planner.clientapp.R;
import ro.planner.api.UserAPI;
import ro.planner.ui.MainActivity;

public class ProfileFragment extends Fragment {

    private Button btnLogout, btnNewsletters;
    private EditText eTxtEmail, eTxtUsername;
    private String username;
    private UserAPI userAPI = new UserAPI();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //remove the status bar
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bundle args = getArguments();
        if (args != null) {
            username = args.getString("username");
        }

        eTxtEmail = view.findViewById(R.id.eTxtProfileEmail);
        eTxtUsername = view.findViewById(R.id.eTxtProfileUsername);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnNewsletters = view.findViewById(R.id.btnNewsletters);


        getUserData();



        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnNewsletters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userAPI.newsletters(getContext(), username, eTxtEmail.toString());
            }
        });

        return view;
    }

    private void getUserData(){
        userAPI.getUserEmail(getContext(), username, new UserAPI.UserCallback() {
            @Override
            public void onSuccess(Double totalAmount) {

            }

            @Override
            public void onSuccessEmail(String response) {
                eTxtEmail.setText(response);
                eTxtUsername.setText(username);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}