package com.discair.intuigames.discair

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.*
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
    lateinit var sessionManager: SessionManager
    lateinit var airportNameSpinner: Spinner
    lateinit var airportTerminalSpinner: Spinner
    lateinit var airportValidateButton: Button
    lateinit var airportsList : List<Airport>
    var airportName: String = ""
    var airportTerminal: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_airport_terminal)

        sessionManager = SessionManager(applicationContext)

        val drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout

        fab.setOnClickListener { view ->
            drawerLayout.openDrawer(Gravity.START)
        }

        airportNameSpinner = findViewById(R.id.airportNameSpinner)
        airportTerminalSpinner = findViewById(R.id.airportTerminalSpinner)
        airportValidateButton = findViewById(R.id.airportValidateButton)

        // Validate button listener
        airportValidateButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                try {
                    // get the information from spinners component
                    airportName = airportNameSpinner.selectedItem.toString()
                    airportTerminal = airportTerminalSpinner.selectedItem.toString()

                    // To pass data to next activity
                    // change view and pass parameters
                    val intent = Intent(this@AirportTerminalActivity, FlightsActivity::class.java)
                    intent.putExtra("airportName", airportName)
                    intent.putExtra("airportTerminal", airportTerminal)

                    // start next activity
                    startActivity(intent)
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    Toast.makeText(this@AirportTerminalActivity, "Informations incorrectes", Toast.LENGTH_SHORT).show()
                }
            }
        })

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
                airportTerminalSpinner.setAdapter(spinnerArrayAdapter)

                airportTerminalSpinner.visibility = View.VISIBLE
                airportTerminalTextView.visibility = View.VISIBLE
            }

        }

        getAeroportsNameList()


        navigationView.setNavigationItemSelectedListener(this)
    }

    fun getAeroportsNameList(){
        val mService = RetrofitClient.getConnection()!!.create(StackServiceInterface::class.java)

        mService.getAirports().enqueue(object : Callback<List<Airport>> {
            override fun onFailure(call: Call<List<Airport>>, t: Throwable) {
                Toast.makeText(this@AirportTerminalActivity, "Problème de connexion", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }

            override fun onResponse(call: Call<List<Airport>>, response: Response<List<Airport>>) =
                if (response.isSuccessful) {
                    airportsList = response.body()!!
                    if (airportsList.isNotEmpty()) {
                        //Airports name
                        val airportsNameArray = Array<String>(airportsList.size, { "$it" } )
                        for (i in 0..(airportsList.size-1)){
                            airportsNameArray[i] = airportsList.get(i).name!!
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
            R.id.nav_mission2 -> {

            }
            R.id.nav_mission3 -> {

            }
            R.id.nav_deconnection -> {
                System.out.println("dfs")
                sessionManager.logoutUser()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
