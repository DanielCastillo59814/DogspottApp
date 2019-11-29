package com.a000webhostapp.dogspott.dogspottapp;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import com.a000webhostapp.dogspott.dogspottapp.utilities.Properties;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.a000webhostapp.dogspott.dogspottapp.R.layout.details;

public class DetailsActivity extends AppCompatActivity {

    ImageView dog_img;
    TextView details_txt;
    TextView comments_field;

    public static final String DOG_ID = "dogid";

    private String dog_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dog_img = findViewById(R.id.dog_img);
        details_txt = findViewById(R.id.details_txt);
        comments_field = findViewById(R.id.comments);

        Intent params = getIntent();
        dog_id = params.getStringExtra(DOG_ID);

        String url = "http://dogspott.000webhostapp.com/detalles.php?key=" + Properties.Companion.getProperty(this, Properties.USER_KEY) + "&dog_id=" + dog_id;

        Log.d("jajajaj", "bbbbasdsa");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.contains("failed")) {
                    JsonObject obj = (JsonObject) new JsonParser().parse(response);
                    Integer likes = obj.get("likes").getAsInt();
                    String dog_url = obj.get("imagen").getAsString();
                    String name = obj.get("name").getAsString();
                    JsonArray comments = obj.get("comentarios").getAsJsonArray();

                    ArrayList<String> comentarios = new ArrayList<String>();
                    for (int i = 0; i < comments.size(); i++) {
                        JsonObject comment = (JsonObject) comments.get(i);
                        String s = "Usuario " + comment.get("user_id").getAsString() + " commento:\n" +
                                "      " + comment.get("text").getAsString() + ".";
                        comentarios.add(s);
                    };

                    InputStream is = null;
                    try {
                        dog_url = dog_url.replaceAll("\\/\\/", "//");
                        URL newurl = new URL(dog_url);

                        Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                        dog_img.setImageBitmap(mIcon_val);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    details_txt.setText("Nombre: " + name + ". Likes: " + likes);
                    if (comentarios.isEmpty())
                        comments_field.setText("No hay comentarios...");
                    for (String com : comentarios) {
                        comments_field.setText(comments_field.getText() + "\n" + com);
                    }
                } else {
                    Toast.makeText(DetailsActivity.this, "Usuario o perro invalido", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("key", Properties.Companion.getProperty(DetailsActivity.this, Properties.USER_KEY));
                parameters.put("dog_id", dog_id);
                return super.getParams();
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}