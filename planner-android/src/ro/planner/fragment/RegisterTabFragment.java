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

public class RegisterTabFragment extends Fragment {
    private EditText email, password, username;
    private Button registerBtn;


    private int duration = 800;
    private int translationX = 800;
    private float alpha = 0;

    private AuthAPI authAPI = new AuthAPI();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_register_tab, container,false);

        //remove the status bar
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final MediaPlayer errorSound = MediaPlayer.create(getContext(), R.raw.error);

        email = root.findViewById(R.id.etxtRegisterEmail);
        password = root.findViewById(R.id.etxtRegisterPassword);
        username = root.findViewById(R.id.etxtRegisterUsername);
        registerBtn = root.findViewById(R.id.btnRegister);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() || !validateEmail() || !validatePassword()) {
                    errorSound.start();
                    return;
                }
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                String usernameString = username.getText().toString();

                authAPI.register(getContext(), emailString, passwordString, usernameString);
            }
        });


        return root;
    }

    //VALIDATE PARAMETERS

    public boolean validateUsername(){
        String fn = username.getText().toString();
        if(fn.isEmpty()){
            username.setError("First name connot be empty!");
            return false;
        }
        else if(fn.length()<3){
            username.setError("First name must contain at least 3 characters!");
            return false;
        }
        else if(fn.length()>50){
            username.setError("First name must contain at most 50 characters!");
            return false;
        }
        else{
            username.setError(null);
            return true;
        }
    }

    public boolean containDigit(String inputString){  //verify if the name contain digits
        char[] chars = inputString.toCharArray();
        for(char character : chars){
            if(Character.isDigit(character)){
                return true;
            }
        }
        return false;
    }


    public boolean validateEmail(){
        String eml = email.getText().toString();
        String checkEmail  = "[a-zA-Z0-9._-]+@(gmail|yahoo|hotmail|outlook|icloud)\\.(com|net|org|edu|info|me|co|gov)";
        if(eml.isEmpty()){
            email.setError("Email connot be empty!");
            return false;
        }else if(eml.length()<5){
            email.setError("Email must contain at least 5 characters!");
            return false;
        }
        else if(eml.length()>50){
            email.setError("Email must contain at most 50 characters!");
            return false;
        }
        else if(!eml.matches(checkEmail )){
            email.setError("Please enter a valid email!");
            return false;
        }
        else{
            email.setError(null);
            return true;
        }
    }

    public boolean validatePassword(){
        String passwd = password.getText().toString();

        if(passwd.isEmpty()){
            password.setError("Password connot be empty!");
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

    //END OF VALIDATE PARAMETERS
}
