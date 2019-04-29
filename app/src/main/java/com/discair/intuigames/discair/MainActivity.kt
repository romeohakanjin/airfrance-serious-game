package com.discair.intuigames.discair

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    lateinit var mission1Button: Button
    lateinit var mission2Button: Button
    lateinit var mission3Button: Button
    lateinit var missionFreeModeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_missions)

        mission1Button = findViewById(R.id.btnMission1)
        mission2Button = findViewById(R.id.btnMission2)
        mission3Button = findViewById(R.id.btnMission3)
        missionFreeModeButton = findViewById(R.id.btnFreeMode)

        // mission 1 listener
        mission1Button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                try {
                    val intent = Intent(applicationContext, LoginActivity::class.java)

                    // To pass data to next activity
                    intent.putExtra("missionNumber", 1)

                    // start next activity
                    startActivity(intent)
                } catch (exception: Exception) {
                    Toast.makeText(this@MainActivity, "Informations incorrectes", Toast.LENGTH_SHORT).show()
                }
            }
        })

        // mission 2 listener
        mission2Button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                try {
                    val intent = Intent(applicationContext, LoginActivity::class.java)

                    // To pass data to next activity
                    intent.putExtra("missionNumber", 2)

                    // start next activity
                    startActivity(intent)
                } catch (exception: Exception) {
                    Toast.makeText(this@MainActivity, "Informations incorrectes", Toast.LENGTH_SHORT).show()
                }
            }
        })

        // mission 3 listener
        mission3Button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                try {
                    val intent = Intent(applicationContext, LoginActivity::class.java)

                    // To pass data to next activity
                    intent.putExtra("missionNumber", 3)

                    // start next activity
                    startActivity(intent)
                } catch (exception: Exception) {
                    Toast.makeText(this@MainActivity, "Informations incorrectes", Toast.LENGTH_SHORT).show()
                }
            }
        })

        // mission free mode
        missionFreeModeButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                try {
                    val intent = Intent(applicationContext, LoginActivity::class.java)

                    // To pass data to next activity
                    intent.putExtra("missionNumber", 4)

                    // start next activity
                    startActivity(intent)
                } catch (exception: Exception) {
                    Toast.makeText(this@MainActivity, "Informations incorrectes", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
