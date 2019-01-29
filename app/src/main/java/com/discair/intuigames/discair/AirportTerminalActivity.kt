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
import com.discair.intuigames.discair.api.airports.Airport
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author RHA
 */
class AirportTerminalActivity : AppCompatActivity() {
    lateinit var airportNameEditText: EditText
    lateinit var airportTerminalEditText: EditText
    lateinit var airportValidateButton: Button
    var airportName: String = ""
    var airportTerminal: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_airport_terminal)

        airportNameEditText = findViewById(R.id.airportNameEditText)
        airportTerminalEditText = findViewById(R.id.airportTerminalEditText)
        airportValidateButton = findViewById(R.id.airportValidateButton)

        // Validate button listener
        airportValidateButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                try {
                    // verification on the airport information
                    airportName = airportNameEditText.text.toString()
                    airportTerminal = airportTerminalEditText.text.toString()
                    checkAirportTerminal(airportName,airportTerminal)
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    Toast.makeText(this@AirportTerminalActivity, "Informations incorrectes", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    fun checkAirportTerminal(airportName: String, airportTerminal: String){
        val mService = RetrofitClient.getConnection()!!.create(StackServiceInterface::class.java)

        mService.getAirport(airportName, airportTerminal).enqueue(object : Callback<List<Airport>> {
            override fun onFailure(call: Call<List<Airport>>, t: Throwable) {
                Toast.makeText(this@AirportTerminalActivity, "Problème de connexion", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }

            override fun onResponse(call: Call<List<Airport>>, response: Response<List<Airport>>) =
                if (response.isSuccessful) {
                    val airports : List<Airport> = response.body()!!
                    if (airports.size == 1) {
                        // change view and pass parameters
                        val intent = Intent(this@AirportTerminalActivity, FlightsActivity::class.java)

                        // To pass data to next activity
                        intent.putExtra("airportName", airportName)
                        intent.putExtra("airportTerminal", airportTerminal)

                        // start next activity
                        startActivity(intent)
                    } else{
                        Toast.makeText(this@AirportTerminalActivity, "Airport or terminal incorrect", Toast.LENGTH_SHORT).show()
                    }
                } else{
                    Toast.makeText(this@AirportTerminalActivity, "Data didn't fetch", Toast.LENGTH_SHORT).show()
                }
        })
    }
}
