package ro.planner.api;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import ro.planner.clientapp.R;

public class UserAPI {

    public interface UserCallback {
        void onSuccess(Double totalAmount);
        void onSuccessEmail(String email);
        void onError(String errorMessage);
    }


    public void getUserEmail(Context context, String username, UserCallback callback) {

        final MediaPlayer successSound = MediaPlayer.create(context, R.raw.success);
        final MediaPlayer errorSound = MediaPlayer.create(context, R.raw.error);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //The URL Posting TO:
        String url = context.getResources().getString(R.string.base_url) + "users/email/" + username;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String email = response;
                            callback.onSuccessEmail(email);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            callback.onError("Failed to parse email");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError("Failed to fetch email");
                    }
                }
        );

        requestQueue.add(stringRequest);
    }

    public void getTotalAmount(Context context, String username, UserCallback callback) {

        final MediaPlayer successSound = MediaPlayer.create(context, R.raw.success);
        final MediaPlayer errorSound = MediaPlayer.create(context, R.raw.error);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //The URL Posting TO:
        String url = context.getResources().getString(R.string.base_url) + "users/amount/" + username;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Double totalAmount = Double.parseDouble(response);
                            callback.onSuccess(totalAmount);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            callback.onError("Failed to parse total amount");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError("Failed to fetch total amount");
                    }
                }
        );

        requestQueue.add(stringRequest);
    }



    public void setTotalAmount(Context context, String username, Double newAmount) {
        final MediaPlayer successSound = MediaPlayer.create(context, R.raw.success);
        final MediaPlayer errorSound = MediaPlayer.create(context, R.raw.error);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //The URL Posting TO:
        String url = context.getResources().getString(R.string.base_url) + "users/amount";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("amount", newAmount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Amount updated successfully", Toast.LENGTH_SHORT).show();
                        successSound.start();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.toString());
//                        Toast.makeText(context, "Failed to update amount" + newAmount, Toast.LENGTH_SHORT).show();
                        errorSound.start();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }


    public void newsletters(Context context, String username, String email){
        final MediaPlayer successSound = MediaPlayer.create(context, R.raw.success);
        final MediaPlayer errorSound = MediaPlayer.create(context, R.raw.error);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //The URL Posting TO:
        String url = context.getResources().getString(R.string.base_url) + "users/newsletters/" + username;

        JSONObject requestBody = new JSONObject();
        try {
            // You would get the email associated with the username in your backend
            requestBody.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a JsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Successfully subscribed to newsletters!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Failed to subscribe to newsletters", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
}
