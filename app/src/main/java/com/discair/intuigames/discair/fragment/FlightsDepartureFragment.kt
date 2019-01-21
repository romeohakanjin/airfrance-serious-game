package com.discair.intuigames.discair.fragment

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.discair.intuigames.discair.R
import com.discair.intuigames.discair.api.RetrofitClient
import com.discair.intuigames.discair.api.StackServiceInterface
import com.discair.intuigames.discair.api.aeroports.Aeroport
import com.discair.intuigames.discair.api.aeroports.Flight
import kotlinx.android.synthetic.main.fragment_flights_arrival.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView



/**
 * @author RHA
 */
class FlightsDepartureFragment : Fragment() {
    private lateinit var rootView: View
    private lateinit var aeroportName: String
    private lateinit var aeroportTerminal: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.fragment_flights_departure, container, false)

        // Get intent extra
        val bundle = this.arguments
        if (bundle != null) {
            aeroportName = bundle.getString("aeroportName").toString()
            aeroportTerminal = bundle.getString("aeroportTerminal").toString()
        }

        // Set airport and terminal value on the view
        val flightTerminalTextView: TextView = rootView.findViewById(R.id.terminalValueTextView)
        val terminalValueTextView: TextView = rootView.findViewById(R.id.valueFlightListLastRefreshTextView)
        flightTerminalTextView.text = aeroportName
        terminalValueTextView.text = aeroportTerminal

        // Get flights list
        val mService = RetrofitClient.getConnection()!!.create(StackServiceInterface::class.java)

        mService.getFlights(aeroportName, aeroportTerminal).enqueue(object : Callback<List<Aeroport>>{
            override fun onResponse(call: Call<List<Aeroport>>, response: Response<List<Aeroport>>) {
                if (response.isSuccessful) {
                    try {
                        val listSize: Int = response.body()!!.size

                        val flightTableLayout: TableLayout = rootView.findViewById(R.id.fragementFlightTableLayout)

                        // For every flights create associated View Component
                        for (i in 0..(listSize-1)){
                            val flight: Flight = response.body()!![i].flight!!
                            val tableRow = TableRow(rootView.context)
                            tableRow.layoutParams = (TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT))

                            val flightTimeTextView = TextView(rootView.context)
                            flightTimeTextView.text = flight.departureTime.toString()
                            flightTimeTextView.gravity = Gravity.CENTER

                            val destinationTextView = TextView(rootView.context)
                            destinationTextView.text = flight.destination.toString()
                            destinationTextView.gravity = Gravity.CENTER

                            val flightTextView = TextView(rootView.context)
                            flightTextView.text = flight.numFlight.toString()
                            flightTextView.gravity = Gravity.CENTER

                            val boardingTextView = TextView(rootView.context)
                            boardingTextView.text = flight.terminal.toString()
                            boardingTextView.gravity = Gravity.CENTER

                            val statusTextView = TextView(rootView.context)
                            statusTextView.text = flight.status!!.wording.toString()
                            statusTextView.gravity = Gravity.CENTER

                            // Add all the elements to the table row
                            tableRow.run {
                                addView(flightTimeTextView)
                                addView(destinationTextView)
                                addView(flightTextView)
                                addView(boardingTextView)
                                addView(statusTextView)
                            }

                            // Add the table row to the view
                            flightTableLayout.addView(tableRow)
                        }
                    }catch(exception: Exception){
                        exception.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<List<Aeroport>>, t: Throwable) {
                t.printStackTrace()
            }
        })

        // Inflate the layout for this fragment
        return rootView
    }
}