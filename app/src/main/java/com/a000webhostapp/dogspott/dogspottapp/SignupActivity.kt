package com.a000webhostapp.dogspott.dogspottapp

import android.os.Bundle
import android.app.Activity
import android.app.AlertDialog
import android.telecom.ConnectionService
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.a000webhostapp.dogspott.dogspottapp.retro.ConnectionAdapter
import com.a000webhostapp.dogspott.dogspottapp.retro.models.SimpleResponse
import com.a000webhostapp.dogspott.dogspottapp.utilities.Properties
import com.a000webhostapp.dogspott.dogspottapp.utilities.State
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_signup.*

/**
 * Actividad par registrar usuarios
 */
class SignupActivity : AppCompatActivity() {
    // disposable para cancelar las peticiones en caso de terminar la aplicación
    val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        btnRegistrarse.setOnClickListener { if (validate()) signup() }
    }

    /**
     * Llamada a la API para registrar un nuevo usuario
     */
    fun signup() {
        layProgress.visibility = View.VISIBLE
        val se = {layProgress.visibility = View.GONE}
        if(State.isNetworkAvailable(this, rootlayout, this::signup, se)) {
            disposable.add(ConnectionAdapter.service
                .signup(txtUsuario.text.toString(), txtContra.text.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSignup){e -> ConnectionAdapter.handleError(e, rootlayout, se, this::signup)})
        }
    }

    /**
     * Handler de la llamada a la API
     */
    fun onSignup(response: SimpleResponse) {
        layProgress.visibility = View.GONE
        if (response.status == "ok") {
            AlertDialog.Builder(this)
                .setTitle(R.string.text_ok)
                .setMessage(R.string.text_usuario_registrado)
                .setPositiveButton(R.string.action_aceptar) {_,_ -> finish()}
                .show()
        } else {
            AlertDialog.Builder(this)
                .setTitle(R.string.text_something_wrong)
                .setMessage(response.message)
                .setPositiveButton(R.string.action_aceptar, null)
                .show()
        }
    }

    /**
     * Validación del formulario para registro
     */
    fun validate(): Boolean {
        var pasa = true
        if (txtUsuario.text.toString().isEmpty()) {
            txtUsuario.error = getString(R.string.validation_empty)
            pasa = false
        } else txtUsuario.error = null
        if (txtContra.text.toString().isEmpty()) {
            txtContra.error = getString(R.string.validation_empty)
            pasa = false
        } else txtContra.error = null
        return pasa
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
