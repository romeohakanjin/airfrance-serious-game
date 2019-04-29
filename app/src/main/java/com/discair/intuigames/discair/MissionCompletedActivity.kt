package com.discair.intuigames.discair

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.discair.intuigames.discair.session.SessionManager

class MissionCompletedActivity : AppCompatActivity() {
    lateinit var sessionManager: SessionManager
    lateinit var missionSelectionButton : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mission_completed)

        // initialize Session Manager
        sessionManager = SessionManager(applicationContext)

        missionSelectionButton = findViewById(R.id.button)

        missionSelectionButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                sessionManager.logoutUser()
                var intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }
}
