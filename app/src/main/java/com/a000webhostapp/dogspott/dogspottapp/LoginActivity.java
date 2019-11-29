package com.a000webhostapp.dogspott.dogspottapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.a000webhostapp.dogspott.dogspottapp.utilities.Properties;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

import static com.a000webhostapp.dogspott.dogspottapp.R.layout.activity_login;


public class LoginActivity extends AppCompatActivity {

    EditText edtUsuario, edtPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Properties.Companion.containsProperty(this, Properties.USER_KEY)) {
            startActivity(new Intent(this, FeedActivity.class));
            finish();
        }
        setContentView(activity_login);
        edtUsuario = findViewById(R.id.edtUsuario);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        Button btnRegistrate = findViewById(R.id.btnSignup);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String URL;
                URL = "http://dogspott.000webhostapp.com/login.php?username=AQUI&password=ALLA";
                String usuario = edtUsuario.getText().toString();
                String contrasena = edtPassword.getText().toString();
                URL = URL.replace("AQUI", usuario);
                URL = URL.replace("ALLA", contrasena);
                validaUsuario(URL);
            }
        });
        btnRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    private void validaUsuario(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.contains("failed")) {
                    JsonObject obj = (JsonObject) new JsonParser().parse(response);
                    JsonElement keye = obj.get("key");
                    String key = keye.getAsString();
                    Properties.Companion.setProperty(LoginActivity.this, Properties.USER_KEY, key);
                    Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("user", edtUsuario.getText().toString());
                parameters.put("password", edtPassword.getText().toString());
                return super.getParams();
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
