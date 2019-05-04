package com.discair.intuigames.discair

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
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
class LoginActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var registrationNumberEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private var missionNumber: Int = 0
    private var registrationNumber: Int = 0
    private var password: Int = 0
    private lateinit var builder : AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize a new instance of builder
        builder = AlertDialog.Builder(this@LoginActivity)

        // initialize Session Manager
        sessionManager = SessionManager(applicationContext)

        missionNumber = intent.getIntExtra("missionNumber", 0)

        if (1 == missionNumber) {
            // Set the alert dialog title
            builder.setTitle(R.string.mission_information)

            // Display a message on alert dialog
            builder.setMessage(R.string.first_mission_tips)

            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton(R.string.ok_string) { _, _ ->
                // do nothing
            }

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            // Display the alert dialog on app interface
            dialog.show()
        }

        if (sessionManager.isLoggedIn()){
            //disconnect the user
            sessionManager.logoutUser()
        } else {
            // initialize view components
            registrationNumberEditText = findViewById(R.id.connection_registration_number_edittext)
            passwordEditText = findViewById(R.id.connection_password_edittext)
            loginButton = findViewById(R.id.connection_login_button)

            // login button listener
            loginButton.setOnClickListener {
                try {
                    // verification on the connection information
                    registrationNumber = registrationNumberEditText.text.toString().toInt()
                    password = passwordEditText.text.toString().toInt()

                    checkLogin(registrationNumber, password)
                } catch (exception: Exception) {
                    Toast.makeText(this@LoginActivity, R.string.incorrect_information, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkLogin(registrationNumber: Int, password: Int){
        val mService = RetrofitClient.getConnection()!!.create(StackServiceInterface::class.java)

        mService.getAgent(registrationNumber, password).enqueue(object : Callback<List<Agents>> {
            override fun onResponse(call: Call<List<Agents>>, response: Response<List<Agents>>) {
                if (response.isSuccessful) {
                    val agents : List<Agents> = response.body()!!
                    if (agents.size == 1) {
                        if (agents[0].registrationNumber == registrationNumber && agents[0].password == password){
                            if (missionNumber == 1){
                                // Set the alert dialog title
                                builder.setTitle(R.string.end_first_mission_title)

                                // Display a message on alert dialog
                                builder.setMessage(R.string.end_first_mission_message)

                                // Set a positive button and its click listener on alert dialog
                                builder.setPositiveButton(R.string.ok_string){_, _ ->
                                    // validate the connection and send to the mission complete view
                                    validateConnection(true)
                                }

                                // Finally, make the alert dialog using builder
                                val dialog: AlertDialog = builder.create()

                                // Display the alert dialog on app interface
                                dialog.show()
                            } else {
                                validateConnection(false)
                            }
                        }
                    } else{
                        Toast.makeText(this@LoginActivity, R.string.incorrect_information, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Agents>>, t: Throwable) {
                Toast.makeText(this@LoginActivity, R.string.connection_failure, Toast.LENGTH_SHORT).show()
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
        val intent = Intent(applicationContext, AirportTerminalActivity::class.java)
        intent.putExtra("missionNumber", missionNumber)
        startActivity(intent)
        finish()
    }

    private fun redirectionMissionCompletedPage(){
        val intent = Intent(applicationContext, MissionCompletedActivity::class.java)
        startActivity(intent)
        finish()
    }
}