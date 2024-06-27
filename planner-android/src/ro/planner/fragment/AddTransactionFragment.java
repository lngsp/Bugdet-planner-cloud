package ro.planner.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

import ro.planner.clientapp.R;
import ro.planner.api.TransactionAPI;

public class AddTransactionFragment extends Fragment {
    private String username;
    private DatePicker datePicker;
    private EditText etxtDescription, etxtAmount;
    private Button btnDefineTransaction;
    private Context fragmentContext;

    private TransactionAPI transactionAPI =  new TransactionAPI();

    public AddTransactionFragment(){}

    public AddTransactionFragment(String username){
        this.username = username;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_transaction, container, false);

        //remove the status bar
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bundle args = getArguments();
        if (args != null) {
            username = args.getString("username");
        }

        etxtDescription = view.findViewById(R.id.etxtDescription);
        etxtAmount = view.findViewById(R.id.etxtAmount);
        btnDefineTransaction = view.findViewById(R.id.btnDefineTransaction);
        datePicker = view.findViewById(R.id.datePicker);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, month, day, null);

        Calendar selectedDateCalendar = Calendar.getInstance();
        selectedDateCalendar.set(year, month, day);

        Date selectedDate = selectedDateCalendar.getTime();

        btnDefineTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transactionAPI.createTransaction(fragmentContext, etxtDescription.getText().toString(),
                        Double.valueOf(etxtAmount.getText().toString()),
                        false, selectedDate, username);
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