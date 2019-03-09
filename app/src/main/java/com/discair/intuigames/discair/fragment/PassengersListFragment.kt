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
import java.text.SimpleDateFormat
import java.util.*

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.fragment_passengers, container, false)

        // Get intent extra
        val intent = this.arguments
        if (intent != null) {
            numFlight = intent.getString("flightTextView").toInt()
            getPassengersByFlightNumber(numFlight)
        }

        // Inflate the layout for this fragment
        return rootView
    }

    /**
     * Récupère la liste des passagers grâce au numéro de vol
     * Affiche les passagers dans la vue
     */
    fun getPassengersByFlightNumber(numFlight: Int){
        val mService = RetrofitClient.getConnection()!!.create(StackServiceInterface::class.java)
        mService.getPassengersByFLightNumber(numFlight).enqueue(object : Callback<List<Airport>> {
            override fun onResponse(call: Call<List<Airport>>, response: Response<List<Airport>>) {
                if (response.isSuccessful()) {
                    if (response.body()!!.size == 1){
                        val airport = response.body()!![0]
                        val flight = airport.flight;
                        if(flight!!.passenger!!.size >= 1){
                            passengerList = flight!!.passenger!!
                            if(passengerList!!.size != 0){
                                val passengersTableLayout = rootView.findViewById(R.id.fragementPassengersTableLayout) as TableLayout

                                for (pass in passengerList as List<Passenger>){
                                    val tableRowId = TextView.generateViewId()

                                    val row = TableRow(rootView.context)
                                    val lp = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
                                    row.id = tableRowId
                                    row.layoutParams=lp

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

                                    row.setOnClickListener(object : View.OnClickListener {
                                        override fun onClick(v: View?) {
                                            try {
                                                val trow = v as TableRow
                                                trow.getVirtualChildAt(trow.id)
                                                val textView : TextView = trow.getChildAt(0) as TextView
                                                val referenceNumber : Int = textView.text.toString().toInt()

                                                val intent = Intent(rootView.context, PassengerActivity::class.java)
                                                intent.putExtra("referenceNumber", referenceNumber)
                                                startActivityForResult(intent, 0)
                                            } catch (exception: Exception) {
                                                Toast.makeText(rootView.context, "Click Broken", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    })
                                    passengersTableLayout.addView(row)
                                }
                            }else{
                                errorMessage = "Aucun passager"
                                displayMessage(errorMessage)
                            }
                        }
                    } else{
                        errorMessage = "Aucun passager"
                        displayMessage(errorMessage)
                    }
                }else{
                    errorMessage = "Erreur "+response.code()+" - "+response.message()+". Veuillez réessayer ultérieurement."
                    displayMessage(errorMessage)
                }
            }

            override fun onFailure(call: Call<List<Airport>>, t: Throwable) {
                errorMessage = "Erreur interne. Veuillez réessayer ultérieurement."
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