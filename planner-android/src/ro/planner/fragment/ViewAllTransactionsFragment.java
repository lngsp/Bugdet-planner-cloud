package ro.planner.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ro.planner.clientapp.R;
import ro.planner.adapter.RecyclerViewTransactionsAdapter;
import ro.planner.api.TransactionAPI;
import ro.planner.model.AllTransactionsModel;
import ro.planner.model.PlanningTransaction;

public class ViewAllTransactionsFragment extends Fragment {
    private String username;

    private RecyclerViewTransactionsAdapter adapter;

    private TransactionAPI transactionAPI = new TransactionAPI();

    ArrayList<AllTransactionsModel> allTransactionsModels = new ArrayList<>();

    public ViewAllTransactionsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_all_transactions, container, false);

        //remove the status bar
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bundle args = getArguments();
        if (args != null) {
            username = args.getString("username");
        }

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewAllTransactions);

        allTransactionsModels.clear();

        getTransactions(new TransactionAPI.TransactionCallback() {
            @Override
            public void onSuccess(List<PlanningTransaction> transactions) {


                SimpleDateFormat serverDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                SimpleDateFormat desiredDateFormat = new SimpleDateFormat("dd.MM.yyyy");

                for(int i = 0; i<transactions.size();i++) {
                    Log.i("########### " , transactions.get(i).getDescription() + transactions.get(i).getAmount() + transactions.get(i).getCompletionDate());

                    try {
                        // Parsați data primită de la server
                        Date serverDate = serverDateFormat.parse(transactions.get(i).getCompletionDate().toString());

                        // Formatați data în noul format
                        String formattedDate = desiredDateFormat.format(serverDate);

                        // Adăugați tranzacția cu data formatată în modelul de toate tranzacțiile
                        allTransactionsModels.add(new AllTransactionsModel(
                                transactions.get(i).getDescription(),
                                transactions.get(i).getAmount().toString(),
                                formattedDate
                        ));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        // Gestionați cazurile în care nu puteți parsa data
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();

            }
        });

        adapter = new RecyclerViewTransactionsAdapter(getContext(), allTransactionsModels);
        adapter.setUsername(username);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    public void getTransactions(final TransactionAPI.TransactionCallback callback){
        if (!isAdded()) {
            // Fragmentul nu este atașat la activitatea curentă, deci nu putem continua
            Log.i("####### MSG ", "Fragmentul nu este atasat!");
            return;
        }
        transactionAPI.getTransactionsByUsername(getContext(), username, new TransactionAPI.TransactionCallback() {
            @Override
            public void onSuccess(List<PlanningTransaction> transactions) {
                if (isAdded()) {
                    callback.onSuccess(transactions);
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

}
