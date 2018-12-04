package com.discair.intuigames.discair

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.discair.intuigames.discair.session.SessionManager

/**
 * @author RHA
 */
class HomeActivity: AppCompatActivity() {
    lateinit var logoutButton: Button
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sessionManager = SessionManager(applicationContext)
        logoutButton = findViewById(R.id.connection_logout_button);

        // logout
        logoutButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                sessionManager.logoutUser()
            }
        })
    }
}
