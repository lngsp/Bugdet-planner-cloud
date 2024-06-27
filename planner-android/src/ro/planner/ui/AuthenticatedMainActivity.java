package ro.planner.ui;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ro.planner.clientapp.databinding.ActivityAuthenticatedMainBinding;

import ro.planner.clientapp.R;
import ro.planner.fragment.AddTransactionFragment;
import ro.planner.fragment.HistoryFragment;
import ro.planner.fragment.HomeUserFragment;
import ro.planner.fragment.ProfileFragment;
import ro.planner.fragment.ViewAllTransactionsFragment;


public class AuthenticatedMainActivity extends AppCompatActivity {
    private String username;

    ActivityAuthenticatedMainBinding binding;

    MediaPlayer buttonPressedSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ro.planner.clientapp.databinding.ActivityAuthenticatedMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        buttonPressedSound = MediaPlayer.create(getApplicationContext(), R.raw.button_pressed);

        username = getIntent().getStringExtra("username");


        replaceFragment(new HomeUserFragment(username));

        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.bottom_current_amount:
                    buttonPressedSound.start();
                    HomeUserFragment homeUserFragment = new HomeUserFragment();
                    replaceFragment(homeUserFragment);
                    break;
                case R.id.bottom_transactions:
                    buttonPressedSound.start();
                    ViewAllTransactionsFragment viewAllTransactionsFragment = new ViewAllTransactionsFragment();
                    replaceFragment(viewAllTransactionsFragment);
                    break;
                case R.id.bottom_add_transaction:
                    buttonPressedSound.start();
                    AddTransactionFragment addTransactionFragment = new AddTransactionFragment();
                    replaceFragment(addTransactionFragment);
                    break;
                case R.id.bottom_profile:
                    buttonPressedSound.start();
                    ProfileFragment profileFragment = new ProfileFragment();
                    replaceFragment(profileFragment);
                    break;
            }

            return true;
        });


    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Adăugați emailul și tokenul în Bundle
        Bundle args = new Bundle();
        args.putString("username", username);
        fragment.setArguments(args);

        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


}