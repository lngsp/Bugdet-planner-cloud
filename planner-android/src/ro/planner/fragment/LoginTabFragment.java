package ro.planner.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import ro.planner.clientapp.R;
import ro.planner.api.AuthAPI;

public class LoginTabFragment extends Fragment {
    private EditText username, password;
    private Button loginBtn;
    private int duration = 800;
    private int translationX = 800;
    private float alpha = 0;
    private AuthAPI authAPI = new AuthAPI();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_login_tab, container,false);

        //remove the status bar
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        username = root.findViewById(R.id.etxtLoginUsername);
        password = root.findViewById(R.id.etxtLoginPassword);
        loginBtn = root.findViewById(R.id.btnLogin);

        final MediaPlayer errorSound = MediaPlayer.create(getContext(), R.raw.error);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() || !validatePassword()) {
                    errorSound.start();
                    return;
                }
                String usernameString = username.getText().toString();
                String passwordString = password.getText().toString();
                authAPI.login(getActivity(), usernameString, passwordString);
            }
        });

        return root;
    }

    //VALIDATE PARAMETERS

    public boolean validateUsername(){
        String usr = username.getText().toString();
        if(usr.isEmpty()){
            username.setError("Username connot be empty!");
            return false;
        }else if(usr.length()<5){
            username.setError("Username must contain at least 5 characters!");
            return false;
        }
        else if(usr.length()>50){
            username.setError("Username must contain at most 50 characters!");
            return false;
        }
        else{
            username.setError(null);
            return true;
        }
    }

    public boolean validatePassword(){
        String passwd = password.getText().toString();

        if(passwd.isEmpty()){
            password.setError("Password connot be empty!");
            return false;
        }
        else if(passwd.length()<5){
            password.setError("Password must contain at least 5 characters!");
            return false;
        }
        else if(passwd.length()>50){
            password.setError("Password must contain at most 50 characters!");
            return false;
        }
                else{
            password.setError(null);
            return true;
        }
    }
}
