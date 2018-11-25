package com.discair.intuigames.discair

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.discair.intuigames.discair.session.SessionManager

/**
 * @author RHA
 */
class MainActivity : AppCompatActivity() {
    lateinit var sessionManager: SessionManager
    lateinit var registrationNumberEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var loginButton: Button
    lateinit var registrationNumber: String
    var password: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initialize Session Manager
        sessionManager = SessionManager(applicationContext)

        if (sessionManager.isLoggedIn()){
           // redirection to home view
            redirectionHomePage()
        } else {
            // initialize view components
            registrationNumberEditText = findViewById(R.id.connection_registration_number_edittext);
            passwordEditText = findViewById(R.id.connection_password_edittext);
            loginButton = findViewById(R.id.connection_login_button);

            // login button listener
            loginButton.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    try {
                        // verification on the connection information
                        registrationNumber = registrationNumberEditText.text.toString()
                        password = passwordEditText.text.toString().toInt()

                        // connection information validate
                        sessionManager.createLoginSession(registrationNumber, password)

                        // redirection on home page
                        redirectionHomePage()
                    } catch (exception: Exception) {
                        Toast.makeText(this@MainActivity, "Informations incorrectes", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    fun redirectionHomePage(){
        var intent: Intent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
