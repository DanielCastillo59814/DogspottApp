package com.a000webhostapp.dogspott.dogspottapp;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.a000webhostapp.dogspott.dogspottapp.retro.models.Dog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.w3c.dom.Text;

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

    public static String DOG_ID = "3327";
    public static String KEY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(details);
        dog_img = findViewById(R.id.dog_img);
        details_txt = findViewById(R.id.details_txt);
        comments_field = findViewById(R.id.comments);

        String url = "http://dogspott.000webhostapp.com/detalles.php?key=" + KEY + "&dog_id=" + DOG_ID;


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
                    }

                    InputStream is = null;
                    try {
                        is = (InputStream) new URL(dog_url).getContent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    dog_img.setImageBitmap(BitmapFactory.decodeStream(is));
                    details_txt.setText("Nombre: " + name + ". Likes: " + likes);
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
                parameters.put("key", KEY);
                parameters.put("dog_id", DOG_ID);
                return super.getParams();
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}