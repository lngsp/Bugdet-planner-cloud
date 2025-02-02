package ro.planner.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ro.planner.model.HistoryModel;

import java.util.ArrayList;

import ro.planner.clientapp.R;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    Context context;    //for inflating out layout
    ArrayList<HistoryModel> historyLocationModels;


    public HistoryAdapter(Context context, ArrayList<HistoryModel> historyLocationModels){
        this.context = context;
        this.historyLocationModels = historyLocationModels;
    }

    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the layout (giving a look to out rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);

        return new HistoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.MyViewHolder holder, int position) {
        //assigning values to the views we created in the recycler_view_row layout file
        //based on the position of the recycler view
        String address = historyLocationModels.get(position).getAddress();

        holder.txtViewAddress.setText(address);
        holder.txtViewArrTime.setText(historyLocationModels.get(position).getArrival_time());
        holder.txtViewDeptTime.setText(historyLocationModels.get(position).getDeparture_time());
        holder.txtViewEmail.setText(historyLocationModels.get(position).getEmail());
        Double latitude = historyLocationModels.get(position).getLatitude();
        Double longitude = historyLocationModels.get(position).getLongitude();
        System.out.println(">>>>>>>>>>>>>> HistoryAdapter" + latitude + " " + longitude);

        holder.btnViewLocOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, ViewLocOnMapActivity.class);
//                intent.putExtra("address", address);
//                intent.putExtra("longitude", longitude);
//                intent.putExtra("latitude", latitude);
//                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        //the recycler view just wants to know the number of items you want displayed
        return historyLocationModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //grabbing the views from our recycler_view_row layout file

        ImageView imageView;
        TextView txtViewAddress, txtViewArrTime, txtViewDeptTime, txtViewEmail;
        Button btnViewLocOnMap;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewLocHistory);
            txtViewAddress = itemView.findViewById(R.id.txtViewAddress);
            txtViewArrTime = itemView.findViewById(R.id.txtViewArrTime);
            txtViewDeptTime = itemView.findViewById(R.id.txtViewDeptTime);
            txtViewEmail = itemView.findViewById(R.id.txtViewEmail);
            btnViewLocOnMap = itemView.findViewById(R.id.btnViewLocOnMap);
        }
    }

}
