package com.a000webhostapp.dogspott.dogspottapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/* La clase es para mostrar el contenido en un perfil después de haber
iniciado sesión exitosamente en una cuenta */

public class PrincipalActivity extends AppCompatActivity{

    /* Despliega el visor de contenido de una cuenta*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login);
    }
}
