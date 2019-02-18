package com.discair.intuigames.discair

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
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
    private lateinit var airportNameTextView: TextView
    private lateinit var clientReservedTextView: TextView
    private lateinit var clientRegistrededTextView: TextView
    private lateinit var clientEmbeddedTextView: TextView
    private lateinit var availableSeatsPremiereTextView: TextView
    private lateinit var availableSeatsBusinessTextView: TextView
    private lateinit var availableSeatsEconomyTextView: TextView
    private lateinit var customerSpecificityUmTextView: TextView
    private lateinit var customerSpecificityPmrTextView: TextView
    private lateinit var customerSpecificityBabiesTextView: TextView
    private lateinit var gpConfirmedTextView: TextView
    private lateinit var gpServiceSeatTextView: TextView
    private lateinit var gpWaitingListTextView: TextView

    private lateinit var flightTime: String
    private lateinit var destination: String
    private lateinit var flight: String
    private lateinit var boarding: String
    private lateinit var status: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight)

        airportNameTextView = findViewById(R.id.airportDepartureArrivalTextView)
        clientReservedTextView = findViewById(R.id.totalClientsReservationTextView)
        clientRegistrededTextView = findViewById(R.id.totalClientsRegistredTextView)
        clientEmbeddedTextView = findViewById(R.id.totalClientsOnboardTextView)
        availableSeatsPremiereTextView = findViewById(R.id.availableSeatsPremiereTextView)
        availableSeatsBusinessTextView = findViewById(R.id.availableSeatsBusinessTextView)
        availableSeatsEconomyTextView = findViewById(R.id.availableSeatsEconomyTextView)
        customerSpecificityUmTextView = findViewById(R.id.umTextView)
        customerSpecificityPmrTextView = findViewById(R.id.pmrTextView)
        customerSpecificityBabiesTextView = findViewById(R.id.babiesTextView)
        gpConfirmedTextView = findViewById(R.id.confirmedTextView)
        gpServiceSeatTextView = findViewById(R.id.seatServicesTextView)
        gpWaitingListTextView = findViewById(R.id.waitingListTextView)

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

        mService.getFlightInformations(flightTime, destination, flight, boarding, status).enqueue(object : Callback<List<Airport>> {
            override fun onFailure(call: Call<List<Airport>>, t: Throwable) {
                Toast.makeText(this@FlightActivity, "Problème de connexion", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }

            override fun onResponse(call: Call<List<Airport>>, response: Response<List<Airport>>) =
                    if (response.isSuccessful) {
                        val airport : Airport = response.body()!![0]
                        setTextView(airport)
                    } else{
                        Toast.makeText(this@FlightActivity, "Data didn't fetch", Toast.LENGTH_SHORT).show()
                    }
        })
    }

    private fun setTextView(airport: Airport) {
        airportNameTextView.text = airport.name + " -> " + airport.flight!!.destination
        //clientReservedTextView.text = airport.
        //clientRegistrededTextView.text = airport.
        //clientEmbeddedTextView.text = airport.
        availableSeatsPremiereTextView.text = airport.flight!!.seats!!.premiere + " Premiere"
        availableSeatsBusinessTextView.text = airport.flight!!.seats!!.business + " Business"
        availableSeatsEconomyTextView.text = airport.flight!!.seats!!.eco + " Eco"
        //customerSpecificityUmTextView.text = airport.
        //customerSpecificityPmrTextView.text = airport.
        //customerSpecificityBabiesTextView.text = airport.
        //gpConfirmedTextView.text = airport.
        //gpServiceSeatTextView.text = airport.
        //gpWaitingListTextView.text = airport.
    }
}
