package com.discair.intuigames.discair

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.support.v7.widget.Toolbar
import com.discair.intuigames.discair.api.RetrofitClient
import com.discair.intuigames.discair.api.StackServiceInterface
import com.discair.intuigames.discair.api.airports.Airport
import com.discair.intuigames.discair.session.SessionManager
import kotlinx.android.synthetic.main.activity_airport_terminal.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author RHA
 */
class AirportTerminalActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var sessionManager: SessionManager
    private lateinit var airportNameSpinner: Spinner
    private lateinit var airportTerminalSpinner: Spinner
    private lateinit var airportValidateButton: Button
    private lateinit var airportsList : List<Airport>
    private var airportName: String = ""
    private var airportTerminal: String = ""
    private var missionNumber: Int = 0
    private lateinit var builder : AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_airport_terminal)

        // Initialize a new instance of builder
        builder = AlertDialog.Builder(this@AirportTerminalActivity)

        missionNumber = intent.getIntExtra("missionNumber", 0)

        if (2 == missionNumber) {
            // Set the alert dialog title
            builder.setTitle(R.string.mission_information)

            // Display a message on alert dialog
            builder.setMessage(R.string.second_mission_tips)

            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton(R.string.ok_string) { _, _ ->
                // do nothing
            }

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            // Display the alert dialog on app interface
            dialog.show()
        } else if (4 == missionNumber){
            // Set the alert dialog title
            builder.setTitle(R.string.mission_information)

            // Display a message on alert dialog
            builder.setMessage(R.string.fourth_mission_tips)

            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton(R.string.ok_string) { _, _ ->
                // do nothing
            }

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            // Display the alert dialog on app interface
            dialog.show()
        }

        sessionManager = SessionManager(applicationContext)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        fab.setOnClickListener {
            drawerLayout.openDrawer(Gravity.START)
        }

        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)


        airportNameSpinner = findViewById(R.id.airportNameSpinner)
        airportTerminalSpinner = findViewById(R.id.airportTerminalSpinner)
        airportValidateButton = findViewById(R.id.airportValidateButton)

        // Validate button listener
        airportValidateButton.setOnClickListener {
            try {
                if(2 != missionNumber) {
                    // get the information from spinners component
                    airportName = airportNameSpinner.selectedItem.toString()
                    airportTerminal = airportTerminalSpinner.selectedItem.toString()

                    // To pass data to next activity
                    // change view and pass parameters
                    val intent = Intent(this@AirportTerminalActivity, FlightsActivity::class.java)
                    intent.putExtra("airportName", airportName)
                    intent.putExtra("airportTerminal", airportTerminal)
                    intent.putExtra("missionNumber", missionNumber)

                    // start next activity
                    startActivity(intent)
                } else {
                    // Set the alert dialog title
                    builder.setTitle(R.string.end_second_mission_title)

                    // Display a message on alert dialog
                    builder.setMessage(R.string.end_second_mission_message)

                    // Set a positive button and its click listener on alert dialog
                    builder.setPositiveButton(R.string.ok_string){_, _ ->
                        // validate the end of mission and send to the mission complete view
                        val intent = Intent(applicationContext, MissionCompletedActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    // Finally, make the alert dialog using builder
                    val dialog: AlertDialog = builder.create()

                    // Display the alert dialog on app interface
                    dialog.show()
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                Toast.makeText(this@AirportTerminalActivity, R.string.incorrect_information, Toast.LENGTH_SHORT).show()
            }
        }

        airportNameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Terminal name
                val airportTerminalsArray: Array<String> = Array<String>(airportsList.get(position).terminals!!.size, { "$it" } )
                for(i in 0..(airportsList.get(position).terminals!!.size-1)){
                    airportTerminalsArray[i] = airportsList.get(position).terminals!![i].name!!
                }

                // Application of the Array to the Spinner
                val spinnerArrayAdapter = ArrayAdapter<String>(this@AirportTerminalActivity, android.R.layout.simple_spinner_item, airportTerminalsArray)
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // The drop down view
                airportTerminalSpinner.adapter = spinnerArrayAdapter

                airportTerminalSpinner.visibility = View.VISIBLE
                airportTerminalTextView.visibility = View.VISIBLE
            }

        }

        getAirportsNameList()

        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun getAirportsNameList(){
        val mService = RetrofitClient.getConnection()!!.create(StackServiceInterface::class.java)

        mService.getAirports().enqueue(object : Callback<List<Airport>> {
            override fun onFailure(call: Call<List<Airport>>, t: Throwable) {
                Toast.makeText(this@AirportTerminalActivity, R.string.connection_failure, Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }

            override fun onResponse(call: Call<List<Airport>>, response: Response<List<Airport>>) =
                if (response.isSuccessful) {
                    airportsList = response.body()!!
                    if (airportsList.isNotEmpty()) {
                        //Airports name
                        val airportsNameArray = Array<String>(airportsList.size, { "$it" } )
                        for (i in 0..(airportsList.size-1)){
                            airportsNameArray[i] = airportsList[i].name!!
                        }

                        // Application of the Array to the Spinner
                        val spinnerArrayAdapter = ArrayAdapter<String>(this@AirportTerminalActivity, android.R.layout.simple_spinner_item, airportsNameArray)
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // The drop down view
                        airportNameSpinner.setAdapter(spinnerArrayAdapter)
                    } else{
                        Toast.makeText(this@AirportTerminalActivity, "Airport or terminal incorrect", Toast.LENGTH_SHORT).show()
                    }
                } else{
                    Toast.makeText(this@AirportTerminalActivity, "Data didn't fetch", Toast.LENGTH_SHORT).show()
                }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_mission1 -> {

            }
            /*
            R.id.nav_mission2 -> {

            }
            R.id.nav_mission3 -> {

            }*/
            R.id.nav_deconnection -> {
                val intent = Intent(applicationContext, MissionCompletedActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
