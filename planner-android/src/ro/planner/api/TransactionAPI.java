package ro.planner.api;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ro.planner.clientapp.R;
import ro.planner.model.PlanningTransaction;

public class TransactionAPI {
    public interface TransactionCallback {
        void onSuccess(List<PlanningTransaction> transactions);
        void onError(String errorMessage);
    }


    public void createTransaction(Context context, String description, double amount,
                                  boolean completed, Date completionDate, String username) {
        final MediaPlayer successSound = MediaPlayer.create(context, R.raw.success);
        final MediaPlayer errorSound = MediaPlayer.create(context, R.raw.error);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //The URL Posting TO:
        String url = context.getResources().getString(R.string.base_url) + "planning-transactions";

        JSONObject requestData = new JSONObject();
        try {
            requestData.put("description", description);
            requestData.put("amount", amount);
            requestData.put("completed", completed);

            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy");

            try {
                Date date = inputFormat.parse(completionDate.toString());
                String formattedDate = outputFormat.format(date);
                requestData.put("completionDate", formattedDate);
                Log.d("# Formatted Date", formattedDate); // Output: 28.05.2024
            } catch (ParseException e) {
                e.printStackTrace();
            }


            requestData.put("username", username);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Creare cerere POST cu Volley
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Transaction added!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tratarea erorii în cazul în care cererea a eșuat

                        Toast.makeText(context, "Failed to create planning transaction", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(request);
    }

    public void completedTransaction(Context context, String description, String username, boolean completed) {
        final MediaPlayer successSound = MediaPlayer.create(context, R.raw.success);
        final MediaPlayer errorSound = MediaPlayer.create(context, R.raw.error);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //The URL Posting TO:
        String url = context.getResources().getString(R.string.base_url) + "planning-transactions/completed";

        JSONObject requestData = new JSONObject();
        try {
            requestData.put("username", username);
            requestData.put("description", description);
            requestData.put("completed", completed);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        Log.d("VolleyResponse", "Transaction completed successfully!");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Log.e("VolleyError", "Failed to update transaction: " + error.toString());
                    }
                }
        );

        requestQueue.add(request);
    }


    public void getTransactionsByUsername(Context context, String username, TransactionCallback callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String url = context.getResources().getString(R.string.base_url) + "planning-transactions/user/" + username;

        List<PlanningTransaction> areaList = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        List<PlanningTransaction> transactions = new ArrayList<>();
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                PlanningTransaction planningTransaction = new PlanningTransaction();

                                JSONObject jsonObject = response.getJSONObject(i);

                                String description = jsonObject.getString("description");
                                Double amount = jsonObject.getDouble("amount");
                                Boolean completed = jsonObject.getBoolean("completed");

                                // Eliminați partea de timp din șirul de dată
                                String dateWithoutTime = jsonObject.getString("completionDate").substring(0, 10);
                                // Formatați data în formatul dorit
                                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date formattedDate = null;
                                try {
                                    formattedDate = inputFormat.parse(dateWithoutTime);
                                    System.out.println("Data formatată: " + formattedDate);
                                    planningTransaction.setCompletionDate(formattedDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                planningTransaction.setDescription(description);
                                planningTransaction.setAmount(amount);
                                planningTransaction.setCompleted(completed);

                                transactions.add(planningTransaction);
                            }
                            callback.onSuccess(transactions);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // În caz de eroare la parsare, apelăm onError
                            callback.onError("Error parsing JSON");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // În caz de eroare la primirea răspunsului, apelăm onError
                        callback.onError("Volley error: " + error.getMessage());
                    }
                });

        // Adăugăm cererea la coadă
        requestQueue.add(jsonArrayRequest);
    }

}
