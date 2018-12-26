package com.discair.intuigames.discair

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.discair.intuigames.discair.api.RetrofitClient
import com.discair.intuigames.discair.api.StackServiceInterface
import com.discair.intuigames.discair.api.agents.Agents
import com.discair.intuigames.discair.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * @author RHA
 */
class MainActivity : AppCompatActivity() {
    lateinit var sessionManager: SessionManager
    lateinit var registrationNumberEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var loginButton: Button
    var registrationNumber: Int = 0
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
            registrationNumberEditText = findViewById(R.id.connection_registration_number_edittext)
            passwordEditText = findViewById(R.id.connection_password_edittext)
            loginButton = findViewById(R.id.connection_login_button)

            // login button listener
            loginButton.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    try {
                        // verification on the connection information
                        registrationNumber = registrationNumberEditText.text.toString().toInt()
                        password = passwordEditText.text.toString().toInt()

                        checkIdentifiants(registrationNumber, password)
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                        Toast.makeText(this@MainActivity, "Informations incorrectes", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    fun validateConnection(){
        // connection information validate
        sessionManager.createLoginSession(registrationNumber, password)

        // redirection on home page
        redirectionHomePage()
    }

    fun redirectionHomePage(){
        var intent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun checkIdentifiants(registrationNumber: Int, password: Int){
        val mService = RetrofitClient.getConnection()!!.create(StackServiceInterface::class.java)

        mService.getAgent(registrationNumber, password).enqueue(object : Callback<List<Agents>> {
            override fun onResponse(call: Call<List<Agents>>, response: Response<List<Agents>>) {
                if (response.isSuccessful()) {
                    System.out.println(response.body()!!.size)
                    if (response.body()!!.size == 1){
                        //val agent = response.body()!![0]
                        validateConnection()
                    } else{
                        Toast.makeText(this@MainActivity, "Informations incorrectes !", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Agents>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Problème de connexion", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }
}
