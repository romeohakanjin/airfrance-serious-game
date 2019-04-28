package com.discair.intuigames.discair

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
class LoginActivity : AppCompatActivity() {
    lateinit var sessionManager: SessionManager
    lateinit var registrationNumberEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var loginButton: Button
    var missionNumber: Int = 0
    var registrationNumber: Int = 0
    var password: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // initialize Session Manager
        sessionManager = SessionManager(applicationContext)

        missionNumber = intent.getIntExtra("missionNumber", 0)

        if (sessionManager.isLoggedIn()){
            //disconnect the user
            sessionManager.logoutUser()
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
                        Toast.makeText(this@LoginActivity, "Informations incorrectes", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

    }

    private fun checkIdentifiants(registrationNumber: Int, password: Int){
        val mService = RetrofitClient.getConnection()!!.create(StackServiceInterface::class.java)

        mService.getAgent(registrationNumber, password).enqueue(object : Callback<List<Agents>> {
            override fun onResponse(call: Call<List<Agents>>, response: Response<List<Agents>>) {
                if (response.isSuccessful()) {
                    val agents : List<Agents> = response.body()!!
                    if (agents.size == 1) {
                        if (agents[0].registrationNumber == registrationNumber && agents[0].password == password){
                            if (missionNumber == 1){
                                validateConnection(true)
                            }

                            if (missionNumber == 2){
                                validateConnection(false)
                            }

                            if (missionNumber == 3){
                                validateConnection(false)
                            }

                            if (missionNumber == 4){
                                validateConnection(false)
                            }
                        }
                    } else{
                        Toast.makeText(this@LoginActivity, "Informations incorrectes !", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Agents>>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Problème de connexion", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun validateConnection(isMissionCompleted: Boolean){
        // connection information validate
        sessionManager.createLoginSession(registrationNumber, password, missionNumber,isMissionCompleted)

        if (!isMissionCompleted){
            // redirection on home page
            redirectionAirportTerminalPage()
        } else {
            redirectionMissionCompletedPage()
        }
    }

    private fun redirectionAirportTerminalPage(){
        var intent = Intent(applicationContext, AirportTerminalActivity::class.java)
        intent.putExtra("missionNumber", missionNumber)
        startActivity(intent)
        finish()
    }

    private fun redirectionMissionCompletedPage(){
        var intent = Intent(applicationContext, MissionCompletedActivity::class.java)
        startActivity(intent)
        finish()
    }
}