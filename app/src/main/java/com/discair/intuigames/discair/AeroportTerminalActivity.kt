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
import com.discair.intuigames.discair.api.aeroports.Terminal
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

        aeroportNameEditText = findViewById(R.id.aeroportNameEditText)
        aeroportTerminalEditText = findViewById(R.id.aeroportTerminalEditText)
        aeroportValidateButton = findViewById(R.id.aeroportValidateButton)


        // Validate button listener
        aeroportValidateButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                try {
                    // verification on the aeroport information
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
    fun checkAeroportTerminal(aeroportName: String, aeroportTerminal: String){
        val terminal = Terminal()
        terminal.name = aeroportTerminal
        val terminals : MutableList<Terminal> = mutableListOf(terminal)

        val mService = RetrofitClient.getConnection()!!.create(StackServiceInterface::class.java)

        mService.getAeroport(aeroportName).enqueue(object : Callback<List<Aeroport>> {
            override fun onFailure(call: Call<List<Aeroport>>, t: Throwable) {
                Toast.makeText(this@AeroportTerminalActivity, "Problème de connexion", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }

            override fun onResponse(call: Call<List<Aeroport>>, response: Response<List<Aeroport>>) {
                if (response.isSuccessful()) {
                    if (response.body()!!.size == 1) {
                        val aeroport = response.body()!![0]
                        var terminalValue : String = ""

                        if (aeroport.terminals!!.size > 1){
                            for (terminal in aeroport.terminals!!){
                                var terminalValueFromList : String = terminal.name.toString()
                                if (terminalValueFromList.equals(aeroportTerminal)){
                                    terminalValue = terminal.name.toString()
                                }
                            }
                        } else {
                            terminalValue = aeroport.terminals!![0].name.toString()
                        }

                        if (terminalValue.equals(aeroportTerminal)){
                            // change view and pass parameters
                            val intent : Intent
                            intent = Intent(this@AeroportTerminalActivity, FlightsActivity::class.java)

                            // To pass data to next activity
                            intent.putExtra("aeroportName", aeroportName)
                            intent.putExtra("aeroportTerminal", aeroportTerminal)

                            // start next activity
                            startActivity(intent)
                        } else{
                            Toast.makeText(this@AeroportTerminalActivity, "Terminal incorrect", Toast.LENGTH_SHORT).show()
                        }
                    } else{
                        Toast.makeText(this@AeroportTerminalActivity, "Aeroport incorrect", Toast.LENGTH_SHORT).show()
                    }
                } else{
                    Toast.makeText(this@AeroportTerminalActivity, "Data didn't fetch", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
