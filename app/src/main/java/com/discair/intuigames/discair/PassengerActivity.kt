package com.discair.intuigames.discair

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.discair.intuigames.discair.api.RetrofitClient
import com.discair.intuigames.discair.api.StackServiceInterface
import com.discair.intuigames.discair.api.airports.Airport
import com.discair.intuigames.discair.api.airports.Passenger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author SLI
 */
class PassengerActivity : AppCompatActivity() {
    private var referenceNumber: String = ""
    private lateinit var referenceTextView: TextView
    private lateinit var lastNameTextView: TextView
    private lateinit var firstNameTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var mobileTextView: TextView
    private lateinit var mailTextView: TextView
    private lateinit var statusTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passenger)

        referenceTextView = findViewById(R.id.refTextView)
        lastNameTextView = findViewById(R.id.lastNameTextView)
        firstNameTextView = findViewById(R.id.firstNameTextView)
        addressTextView = findViewById(R.id.addressTextView)
        mobileTextView = findViewById(R.id.mobileTextView)
        mailTextView = findViewById(R.id.mailTextView)
        statusTextView = findViewById(R.id.statusTextView)

        //get intent extra
        referenceNumber = intent.getIntExtra("referenceNumber", 0).toString()
        this.getPassengerInfoByRefNumber(referenceNumber)
    }

    /**
     * Get the list of passenger of the flight with the flight number
     * and display the selected passenger on the screen
     */
    private fun getPassengerInfoByRefNumber(referenceNum: String){
        val mService = RetrofitClient.getConnection()!!.create(StackServiceInterface::class.java)
        mService.getPassengerByReferenceNumber(referenceNum).enqueue(object : Callback<List<Airport>> {
            override fun onResponse(call: Call<List<Airport>>, response: Response<List<Airport>>) {
                if (response.isSuccessful) {
                    if (response.body()!!.size == 1){
                        val airport = response.body()!![0]
                        val flight = airport.flight
                        if(flight!!.passenger!!.isNotEmpty()){
                            for(pass in flight.passenger as List<Passenger>){
                                if((pass.referenceNumber) == referenceNum){
                                    referenceTextView.text = pass.referenceNumber
                                    lastNameTextView.text = pass.lastName
                                    firstNameTextView.text = pass.firstName
                                    addressTextView.text = pass.address
                                    mobileTextView.text = pass.mobile
                                    mailTextView.text = pass.mail
                                    statusTextView.text = pass.status!!.wording
                                }
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Airport>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}