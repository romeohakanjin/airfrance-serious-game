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
class FlightActivity : AppCompatActivity() {
    lateinit var flightTime: String
    lateinit var destination: String
    lateinit var flight: String
    lateinit var boarding: String
    lateinit var status: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight)

        // Get intent extra
        val intent = intent
        if (intent != null) {
            flightTime = intent.getStringExtra("flightTimeTextView").toString()
            destination = intent.getStringExtra("destinationTextView").toString()
            flight = intent.getStringExtra("flightTextView").toString()
            boarding = intent.getStringExtra("boardingTextView").toString()
            status = intent.getStringExtra("statusTextView").toString()

            getFlightInformations()
        } else{
            Toast.makeText(this@FlightActivity, "No data passed", Toast.LENGTH_SHORT).show()
        }


    }
    private fun getFlightInformations(){
        val mService = RetrofitClient.getConnection()!!.create(StackServiceInterface::class.java)

       /* mService.getAirport(airportName, airportTerminal).enqueue(object : Callback<List<Airport>> {
            override fun onFailure(call: Call<List<Airport>>, t: Throwable) {
                Toast.makeText(this@FlightActivity, "Problème de connexion", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }

            override fun onResponse(call: Call<List<Airport>>, response: Response<List<Airport>>) =
                    if (response.isSuccessful) {

                    } else{
                        Toast.makeText(this@FlightActivity, "Data didn't fetch", Toast.LENGTH_SHORT).show()
                    }
        })*/
    }
}
