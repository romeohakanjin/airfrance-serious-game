package com.discair.intuigames.discair

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.discair.intuigames.discair.api.RetrofitClient
import com.discair.intuigames.discair.api.StackServiceInterface
import com.discair.intuigames.discair.api.airports.Airport
import com.discair.intuigames.discair.api.airports.Passenger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import android.support.v4.widget.SwipeRefreshLayout

/**
 * @author RHA
 */
class FlightActivity : AppCompatActivity() {
    private val simpleDateFormat = SimpleDateFormat("hh:mm:ss")

    private lateinit var swipeToRefreshLayout: SwipeRefreshLayout

    private lateinit var terminalValueTextView: TextView
    private lateinit var valueFlightListLastRefreshTextView: TextView
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

    private lateinit var airportTerminal: String
    private lateinit var flightTime: String
    private lateinit var destination: String
    private lateinit var flight: String
    private lateinit var boarding: String
    private lateinit var status: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight)

        swipeToRefreshLayout = findViewById(R.id.swipeToRefreshLayout)
        terminalValueTextView = findViewById(R.id.terminalValueTextView)
        valueFlightListLastRefreshTextView = findViewById(R.id.valueFlightListLastRefreshTextView)
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
            airportTerminal = intent.getStringExtra("airportTerminal").toString()

            refreshAndSetFlightInformations()

        } else{
            Toast.makeText(this@FlightActivity, "No data passed", Toast.LENGTH_SHORT).show()
        }

        // Setup refresh listener which triggers new data loading
        swipeToRefreshLayout.setOnRefreshListener {
            // Refresh the list here.
            refreshAndSetFlightInformations()

            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            swipeToRefreshLayout.isRefreshing = false
        }
    }

    private fun refreshAndSetFlightInformations(){
        val mService = RetrofitClient.getConnection()!!.create(StackServiceInterface::class.java)

        mService.getFlightInformations(flightTime, destination, flight, boarding, status).enqueue(object : Callback<List<Airport>> {
            override fun onFailure(call: Call<List<Airport>>, t: Throwable) {
                Toast.makeText(this@FlightActivity, "Problème de connexion", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }

            override fun onResponse(call: Call<List<Airport>>, response: Response<List<Airport>>) =
                    if (response.isSuccessful) {
                        var numberOfPaxRegistred: Int = 0
                        var numberOfPaxOnboard: Int = 0
                        var numberOfPaxReserved: Int = 0
                        var numberOfGpConfirmed: Int = 0
                        var numberOfGpSeatService: Int = 0
                        var numberOfGpWaitingList: Int = 0
                        var numberOfUm: Int = 0
                        var numberOfPmr: Int = 0
                        var numberOfBabies: Int = 0
                        val airport : Airport = response.body()!![0]

                        val passengerList: List<Passenger> = airport.flight!!.passenger!!
                        for (passenger: Passenger in passengerList){
                            when (passenger.pax!!.status){
                                "reserved" -> {
                                    numberOfPaxReserved += 1
                                }
                                "onboard" -> {
                                    numberOfPaxOnboard += 1
                                }
                                "registred" -> {
                                    numberOfPaxRegistred += 1
                                }
                            }

                            when (passenger.pax!!.type){
                                "um" -> {
                                    numberOfUm += 1
                                }
                                "pmr" -> {
                                    numberOfPmr += 1
                                }
                                "babies" -> {
                                    numberOfBabies += 1
                                }
                            }

                            when (passenger.gp!!.status){
                                "confirmed" -> {
                                    numberOfGpConfirmed += 1
                                }
                                "seat service" -> {
                                    numberOfGpSeatService += 1
                                }
                                "waiting list" -> {
                                    numberOfGpWaitingList += 1
                                }
                            }
                        }

                        val currentDate = simpleDateFormat.format(Date())

                        terminalValueTextView.text = airportTerminal
                        valueFlightListLastRefreshTextView.text = currentDate.toString()
                        airportNameTextView.text = airport.name + " -> " + airport.flight!!.destination
                        clientReservedTextView.text = "$numberOfPaxReserved reserved"
                        clientRegistrededTextView.text = "$numberOfPaxRegistred registered"
                        clientEmbeddedTextView.text = "$numberOfPaxOnboard on board"
                        availableSeatsPremiereTextView.text = airport.flight!!.seats!!.premiere + " Premiere"
                        availableSeatsBusinessTextView.text = airport.flight!!.seats!!.business + " Business"
                        availableSeatsEconomyTextView.text = airport.flight!!.seats!!.eco + " Eco"
                        customerSpecificityUmTextView.text = "$numberOfUm um"
                        customerSpecificityPmrTextView.text = "$numberOfPmr pmr"
                        customerSpecificityBabiesTextView.text = "$numberOfBabies babies"
                        gpConfirmedTextView.text = "$numberOfGpConfirmed confirmed"
                        gpServiceSeatTextView.text = "$numberOfGpSeatService seat service"
                        gpWaitingListTextView.text = "$numberOfGpWaitingList waiting list"

                    } else{
                        Toast.makeText(this@FlightActivity, "Data didn't fetch", Toast.LENGTH_SHORT).show()
                    }
        })
    }
}
