package ro.planner.api;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import ro.planner.clientapp.R;
import ro.planner.ui.AuthenticatedMainActivity;

public class AuthAPI {

    public void login(Context context, String username, String password){

        final MediaPlayer successSound = MediaPlayer.create(context, R.raw.success);
        final MediaPlayer errorSound = MediaPlayer.create(context, R.raw.error);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //The URL Posting TO:
        String url = context.getResources().getString(R.string.base_url) + "users/login";

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", username);
            requestBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Răspunsul cu succes de la server
                        Intent intent = new Intent(context, AuthenticatedMainActivity.class);
                        intent.putExtra("username", username);
                        context.startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tratarea erorii în cazul în care cererea a eșuat
                        Toast.makeText(context, "Authentication failed " + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Adăugarea cererii la coada de cereri Volley
        requestQueue.add(jsonObjectRequest);
    }

    //////REGISTER
    public void register(Context context, String email, String password, String username){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getResources().getString(R.string.base_url) + "users/register";

        final MediaPlayer successSound = MediaPlayer.create(context, R.raw.button_pressed);
        final MediaPlayer errorSound = MediaPlayer.create(context, R.raw.button_pressed);

        JSONObject userJson = new JSONObject();
        try {
            userJson.put("email", email);
            userJson.put("password", password);
            userJson.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                userJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Intent intent = new Intent(context, AuthenticatedMainActivity.class);
                intent.putExtra("username", username);
                context.startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorSound.start();
                if(error.networkResponse != null && error.networkResponse.data != null) {
                    String errorMessage = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Error connecting to server", Toast.LENGTH_SHORT).show();
                }
                error.printStackTrace();
                Log.e("AuthAPI", "Error in register: " + error.getMessage());
                errorSound.release();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjectRequest);
    }

    ////LOGOUT
//    public void logout(Context context, String username) {
//        RequestQueue queue = Volley.newRequestQueue(context);
//        String url = context.getResources().getString(R.string.base_url) + "auth/logout";
//
//        final MediaPlayer successSound = MediaPlayer.create(context, R.raw.button_pressed);
//        final MediaPlayer errorSound = MediaPlayer.create(context, R.raw.button_pressed);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        if (response.equals("Logout successfully")) {
//                            TokenManager.getInstance(context).clearToken();
//                            successSound.start();
//                        } else {
//                            errorSound.start();
//                            Log.e("AuthAPI", "Error in logout.");
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        errorSound.start();
//                        if (error.networkResponse != null && error.networkResponse.data != null) {
//                            String errorMessage = new String(error.networkResponse.data, StandardCharsets.UTF_8);
//                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
//                        } else {
//                            Toast.makeText(context, "Error connecting to server", Toast.LENGTH_SHORT).show();
//                        }
//                        error.printStackTrace();
//                        Log.e("AuthAPI", "Error in logout: " + error.getMessage());
//                        errorSound.release();
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() {
//                // add email to request param
//                Map<String, String> params = new HashMap<>();
//                params.put("email", username);
//                return params;
//            }
//        };
//
//        queue.add(stringRequest);
//    }
}
