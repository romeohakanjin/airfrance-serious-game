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
import com.discair.intuigames.discair.api.aeroports.Aeroport
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author RHA
 */
class AeroportTerminalActivity : AppCompatActivity() {
    lateinit var aeroportNameEditText: EditText
    lateinit var aeroportTerminalEditText: EditText
    lateinit var aeroportValidateButton: Button
    var aeroportName: String = ""
    var aeroportTerminal: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aeroport_terminal)

        // Get the view components
        aeroportNameEditText = findViewById(R.id.aeroportNameEditText)
        aeroportTerminalEditText = findViewById(R.id.aeroportTerminalEditText)
        aeroportValidateButton = findViewById(R.id.aeroportValidateButton)


        // Validate button listener
        aeroportValidateButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                try {
                    // Verification on the aeroport information
                    aeroportName = aeroportNameEditText.text.toString()
                    aeroportTerminal = aeroportTerminalEditText.text.toString()
                    checkAeroportTerminal(aeroportName,aeroportTerminal)
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    Toast.makeText(this@AeroportTerminalActivity, "Informations incorrectes", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    /**
     * Check aeroport terminal
     * @param aeroportName: aeroport name
     * @param aeroprotTerminal: aeroport terminal
     */
    fun checkAeroportTerminal(aeroportName: String, aeroportTerminal: String){
        val mService = RetrofitClient.getConnection()!!.create(StackServiceInterface::class.java)

        mService.getAeroport(aeroportName, aeroportTerminal).enqueue(object : Callback<List<Aeroport>> {
            override fun onFailure(call: Call<List<Aeroport>>, t: Throwable) {
                Toast.makeText(this@AeroportTerminalActivity, "Problème de connexion", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }

            override fun onResponse(call: Call<List<Aeroport>>, response: Response<List<Aeroport>>) =
                if (response.isSuccessful()) {
                    val aeroports : List<Aeroport> = response.body()!!
                    if (aeroports.size == 1) {
                        // change view and pass parameters
                        val intent : Intent = Intent(this@AeroportTerminalActivity, FlightsActivity::class.java)

                        // To pass data to next activity
                        intent.putExtra("aeroportName", aeroportName)
                        intent.putExtra("aeroportTerminal", aeroportTerminal)

                        // start next activity
                        startActivity(intent)
                    } else{
                        Toast.makeText(this@AeroportTerminalActivity, "Aeroport ou terminal incorrect", Toast.LENGTH_SHORT).show()
                    }
                } else{
                    Toast.makeText(this@AeroportTerminalActivity, "Data didn't fetch", Toast.LENGTH_SHORT).show()
                }
        })
    }
}
