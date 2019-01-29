package com.discair.intuigames.discair.fragment

import android.content.Intent
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
import com.discair.intuigames.discair.api.airports.Airport
import com.discair.intuigames.discair.api.airports.Flight
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.TableLayout
import android.widget.TextView
import com.discair.intuigames.discair.FlightActivity

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
            aeroportName = bundle.getString("airportName").toString()
            aeroportTerminal = bundle.getString("airportTerminal").toString()
        }

        // Set airport and terminal value on the view
        val flightTerminalTextView: TextView = rootView.findViewById(R.id.terminalValueTextView)
        val terminalValueTextView: TextView = rootView.findViewById(R.id.valueFlightListLastRefreshTextView)
        flightTerminalTextView.text = aeroportName
        terminalValueTextView.text = aeroportTerminal

        // Get flights list
        val mService = RetrofitClient.getConnection()!!.create(StackServiceInterface::class.java)

        mService.getFlights(aeroportName, aeroportTerminal).enqueue(object : Callback<List<Airport>>{
            override fun onResponse(call: Call<List<Airport>>, response: Response<List<Airport>>) {
                if (response.isSuccessful) {
                    try {
                        val listSize: Int = response.body()!!.size

                        val flightTableLayout: TableLayout = rootView.findViewById(R.id.fragementFlightTableLayout)

                        // For every flights create associated View Component
                        for (i in 0..(listSize-1)){
                            val flight: Flight = response.body()!![i].flight!!
                            val tableRow = TableRow(rootView.context)
                            tableRow.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)

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

                            tableRow.isClickable = true
                            tableRow.setOnClickListener(object : View.OnClickListener {
                                override fun onClick(view: View) {
                                    val tableRowSelected = view as TableRow
                                    val flightTimeTextView = tableRowSelected.getChildAt(0)  as TextView
                                    val destinationTextView = tableRowSelected.getChildAt(1)  as TextView
                                    val flightTextView = tableRowSelected.getChildAt(2)  as TextView
                                    val boardingTextView = tableRowSelected.getChildAt(3)  as TextView
                                    val statusTextView = tableRowSelected.getChildAt(4)  as TextView

                                    // change view and pass parameters
                                    val intent = Intent(rootView.context, FlightActivity::class.java)

                                    // To pass data to next activity
                                    intent.putExtra("flightTimeTextView", flightTimeTextView.text)
                                    intent.putExtra("destinationTextView", destinationTextView.text)
                                    intent.putExtra("flightTextView", flightTextView.text)
                                    intent.putExtra("boardingTextView", boardingTextView.text)
                                    intent.putExtra("statusTextView", statusTextView.text)

                                    // start next activity
                                    startActivity(intent)
                                }
                            })

                            // Add the table row to the view
                            flightTableLayout.addView(tableRow)
                        }
                    }catch(exception: Exception){
                        exception.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<List<Airport>>, t: Throwable) {
                t.printStackTrace()
            }
        })

        // Inflate the layout for this fragment
        return rootView
    }
}