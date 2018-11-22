package com.discair.intuigames.discair

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    lateinit var registrationNumberEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var loginButton: Button
    lateinit var registrationNumber: Number
    lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initialize view components
        registrationNumberEditText = findViewById(R.id.connection_registration_number_edittext);
        passwordEditText = findViewById(R.id.connection_password_edittext);
        loginButton = findViewById(R.id.connection_login_button);

        loginButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v : View?){
                try {
                    registrationNumber = registrationNumberEditText.text.toString().toInt()
                    password = passwordEditText.text.toString()

                    Toast.makeText(this@MainActivity, ""+registrationNumber, Toast.LENGTH_SHORT).show()
                    Toast.makeText(this@MainActivity, ""+password, Toast.LENGTH_SHORT).show()
                } catch (exception: Exception){
                    Toast.makeText(this@MainActivity, "Informations incorrectes", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }
}
