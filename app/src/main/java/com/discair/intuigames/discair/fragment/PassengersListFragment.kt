package com.discair.intuigames.discair.fragment

import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.discair.intuigames.discair.PassengerActivity
import com.discair.intuigames.discair.R
import com.discair.intuigames.discair.api.RetrofitClient
import com.discair.intuigames.discair.api.StackServiceInterface
import com.discair.intuigames.discair.api.airports.Airport
import com.discair.intuigames.discair.api.airports.Passenger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author RHA
 */
class PassengersListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
    }

    private lateinit var rootView: View
    private var numFlight: Int = 0
    private var passengerList: List<Passenger>? = null
    private var errorMessage: String = ""
    private var missionNumber: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.fragment_passengers, container, false)

        // Get intent extra
        val intent = this.arguments
        if (intent != null) {
            numFlight = intent.getString("flightTextView").toInt()
            missionNumber = intent.getInt("missionNumber").toString().toInt()
            getPassengersByFlightNumber(numFlight)
        }

        // Inflate the layout for this fragment
        return rootView
    }

    /**
     * Get the list of passengers by flight number and print the passengers on the view
     */
    private fun getPassengersByFlightNumber(numFlight: Int){
        val mService = RetrofitClient.getConnection()!!.create(StackServiceInterface::class.java)
        mService.getPassengersByFLightNumber(numFlight).enqueue(object : Callback<List<Airport>> {
            override fun onResponse(call: Call<List<Airport>>, response: Response<List<Airport>>) {
                if (response.isSuccessful) {
                    if (response.body()!!.size == 1){
                        val airport = response.body()!![0]
                        val flight = airport.flight
                        if(flight!!.passenger!!.isNotEmpty()){
                            passengerList = flight.passenger!!
                            if(passengerList!!.isNotEmpty()){
                                val passengersTableLayout = rootView.findViewById(R.id.fragementPassengersTableLayout) as TableLayout

                                for (pass in passengerList as List<Passenger>){
                                    val tableRowId = TextView.generateViewId()

                                    val row = TableRow(rootView.context)
                                    val layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
                                    row.id = tableRowId
                                    row.layoutParams = layoutParams

                                    val refNumber = TextView(rootView.context)
                                    refNumber.tag = "refNumber"
                                    refNumber.text = pass.referenceNumber.toString()
                                    refNumber.gravity = Gravity.CENTER

                                    val lastName = TextView(rootView.context)
                                    lastName.tag = "lastNameTag"
                                    lastName.text = pass.lastName
                                    lastName.gravity = Gravity.CENTER

                                    val firstName = TextView(rootView.context)
                                    firstName.tag = "firstNameTag"
                                    firstName.text = pass.firstName
                                    firstName.gravity = Gravity.CENTER

                                    val mail = TextView(rootView.context)
                                    mail.tag = "mailTag"
                                    mail.text = pass.mail
                                    mail.gravity = Gravity.CENTER

                                    // Add all the elements to the table row
                                    row.run {
                                        addView(refNumber)
                                        addView(lastName)
                                        addView(firstName)
                                        addView(mail)
                                    }

                                    row.setOnClickListener { v ->
                                        try {
                                            val trow = v as TableRow
                                            trow.getVirtualChildAt(trow.id)
                                            val textView : TextView = trow.getChildAt(0) as TextView
                                            val referenceNumber : Int = textView.text.toString().toInt()

                                            val intent = Intent(rootView.context, PassengerActivity::class.java)
                                            intent.putExtra("referenceNumber", referenceNumber)
                                            intent.putExtra("missionNumber", missionNumber)
                                            startActivityForResult(intent, 0)
                                        } catch (exception: Exception) {
                                            Toast.makeText(rootView.context, R.string.click_broken, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    passengersTableLayout.addView(row)
                                }
                            }else{
                                errorMessage = R.string.no_passengers.toString()
                                displayMessage(errorMessage)
                            }
                        }
                    } else{
                        errorMessage = R.string.no_passengers.toString()
                        displayMessage(errorMessage)
                    }
                }else{
                    errorMessage = R.string.error.toString()+ "" +response.code()+" - "+response.message()+"."+ R.string.try_again_later.toString() +"."
                    displayMessage(errorMessage)
                }
            }

            override fun onFailure(call: Call<List<Airport>>, t: Throwable) {
                errorMessage = R.string.internal_error.toString() + "." + R.string.try_again_later.toString() + "."
                displayMessage(errorMessage)
                t.printStackTrace()
            }
        })
    }



    /**
     * Affiche le message en paramètre dans un TOAST
     */
    fun displayMessage(message: String){
        Toast.makeText(rootView.context, message, Toast.LENGTH_SHORT).show()
    }
}