package ro.planner.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ro.planner.clientapp.R;
import ro.planner.api.TransactionAPI;
import ro.planner.model.AllTransactionsModel;

public class RecyclerViewTransactionsAdapter extends RecyclerView.Adapter<RecyclerViewTransactionsAdapter.MyViewHolder>{
    Context context;    //for inflating out layout
    ArrayList<AllTransactionsModel> allTransactionsModels;
    private TransactionAPI transactionAPI = new TransactionAPI();

    private String username;

    public RecyclerViewTransactionsAdapter(Context context, ArrayList<AllTransactionsModel> allTransactionsModels){
        this.context = context;
        this.allTransactionsModels = allTransactionsModels;
    }


    @NonNull
    @Override
    public RecyclerViewTransactionsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the layout (giving a look to out rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row_transaction, parent, false);

        return new RecyclerViewTransactionsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewTransactionsAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //assigning values to the views we created in the recycler_view_row layout file
        //based on the position of the recycler view
        String description = allTransactionsModels.get(position).getDescription();
        Double amount = Double.valueOf(allTransactionsModels.get(position).getAmount());

        String completionDateString = allTransactionsModels.get(position).getCompletionDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date completionDate = null;
        try {
            completionDate = dateFormat.parse(completionDateString);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("#ViewAllTransactions", "Error parsing date: " + e.getMessage());
        }

        holder.txtDescription.setText(description);
        holder.txtAmount.setText(amount.toString());

        if (completionDate != null) {
            holder.txtCompletionDate.setText(dateFormat.format(completionDate));
        } else {
            holder.txtCompletionDate.setText("Data indisponibilă");
        }



        holder.btnCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transactionAPI.completedTransaction(context, description, username, true);
            }
        });

    }

    @Override
    public int getItemCount() {
        //the recycler view just wants to know the number of items you want displayed
        return allTransactionsModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //grabbing the views from our recycler_view_row layout file

        TextView txtDescription, txtAmount, txtCompletionDate;
        Button btnCompleted;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtCompletionDate = itemView.findViewById(R.id.txtCompletionDate);

            btnCompleted = itemView.findViewById(R.id.btnCompleted);
        }
    }

    public void updateData(List<AllTransactionsModel> newModels) {
        allTransactionsModels.clear();
        allTransactionsModels.addAll(newModels);
        notifyDataSetChanged();
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}