package com.discair.intuigames.discair.fragment

import android.content.Intent
import android.graphics.Typeface
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.discair.intuigames.discair.FlightActivity
import com.discair.intuigames.discair.R
import com.discair.intuigames.discair.api.RetrofitClient
import com.discair.intuigames.discair.api.StackServiceInterface
import com.discair.intuigames.discair.api.airports.Airport
import com.discair.intuigames.discair.api.airports.Flight
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author RHA
 */
class FlightsArrivalFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
    }

    private val simpleDateFormat = SimpleDateFormat("hh:mm:ss")

    private lateinit var swipeToRefreshLayout: SwipeRefreshLayout

    private lateinit var rootView: View
    private lateinit var airportName: String
    private lateinit var airportTerminal: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.fragment_flights_arrival, container, false)

        swipeToRefreshLayout = rootView.findViewById(R.id.swipeToRefreshLayoutArrival)

        // Setup refresh listener which triggers new data loading
        swipeToRefreshLayout.setOnRefreshListener {
            // Refresh the list here.
            activity!!.runOnUiThread {
                val flightTableLayout: TableLayout = rootView.findViewById(R.id.fragementFlightTableLayout)
                flightTableLayout.removeAllViews()

                getArrivalFlightsAndSetTextviews()

                swipeToRefreshLayout.isRefreshing = false
            }

            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            swipeToRefreshLayout.isRefreshing = false
        }

        getArrivalFlightsAndSetTextviews()

        // Inflate the layout for this fragment
        return rootView
    }

    /**
     *
     */
    private fun getArrivalFlightsAndSetTextviews(){
        // Get intent extra
        val bundle = this.arguments
        if (bundle != null) {
            airportName = bundle.getString("airportName").toString()
            airportTerminal = bundle.getString("airportTerminal").toString()

            val currentDate = simpleDateFormat.format(Date())

            // Set airport and terminal value on the view
            val flightTerminalTextView: TextView = rootView.findViewById(R.id.terminalValueTextView)
            val lastRefreshValueTextView: TextView = rootView.findViewById(R.id.valueFlightListLastRefreshTextView)
            flightTerminalTextView.text = airportName
            lastRefreshValueTextView.text = currentDate.toString()
        }

        // Get flights list
        val mService = RetrofitClient.getConnection()!!.create(StackServiceInterface::class.java)

        mService.getFlightsArrival(airportName).enqueue(object : Callback<List<Airport>>{
            override fun onResponse(call: Call<List<Airport>>, response: Response<List<Airport>>) {
                if (response.isSuccessful) {
                    val listSize: Int = response.body()!!.size

                    val flightTableLayout: TableLayout = rootView.findViewById(R.id.fragementFlightTableLayout)

                    // set the default line labels
                    val defaultTableRow: TableRow = setFirstRowLabels()
                    flightTableLayout.addView(defaultTableRow)

                    // For every flights create associated View Component
                    for (i in 0..(listSize-1)){
                        val flight: Flight = response.body()!![i].flight!!
                        val tableRow = TableRow(rootView.context)
                        tableRow.layoutParams = (TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT))

                        val flightTimeTextView = TextView(rootView.context)
                        flightTimeTextView.text = flight.arrivalTime.toString()
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
                                intent.putExtra("airportTerminal", airportTerminal)
                                intent.putExtra("airportTerminal", airportTerminal)

                                // start next activity
                                startActivity(intent)
                            }
                        })

                        // Add the table row to the view
                        flightTableLayout.addView(tableRow)
                    }
                }
            }
            override fun onFailure(call: Call<List<Airport>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    /**
     *
     */
    private fun setFirstRowLabels(): TableRow {
        val tableRow = TableRow(rootView.context)
        tableRow.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)

        val flightTimeTextView = TextView(rootView.context)
        flightTimeTextView.text = getString(R.string.flight_time)
        flightTimeTextView.gravity = Gravity.CENTER
        flightTimeTextView.setTypeface(flightTimeTextView.getTypeface(), Typeface.BOLD);

        val destinationTextView = TextView(rootView.context)
        destinationTextView.text = getString(R.string.flight_destination)
        destinationTextView.gravity = Gravity.CENTER
        destinationTextView.setTypeface(destinationTextView.getTypeface(), Typeface.BOLD);

        val flightTextView = TextView(rootView.context)
        flightTextView.text = getString(R.string.flight_number)
        flightTextView.gravity = Gravity.CENTER
        flightTextView.setTypeface(flightTextView.getTypeface(), Typeface.BOLD);

        val boardingTextView = TextView(rootView.context)
        boardingTextView.text =getString( R.string.flight_boarding)
        boardingTextView.gravity = Gravity.CENTER
        boardingTextView.setTypeface(boardingTextView.getTypeface(), Typeface.BOLD);

        val statusTextView = TextView(rootView.context)
        statusTextView.text = getString(R.string.flight_status)
        statusTextView.gravity = Gravity.CENTER
        statusTextView.setTypeface(statusTextView.getTypeface(), Typeface.BOLD);

        tableRow.run {
            addView(flightTimeTextView)
            addView(destinationTextView)
            addView(flightTextView)
            addView(boardingTextView)
            addView(statusTextView)
        }

        return tableRow
    }
}