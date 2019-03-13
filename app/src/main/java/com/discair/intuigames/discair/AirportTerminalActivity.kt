package com.discair.intuigames.discair

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.discair.intuigames.discair.api.RetrofitClient
import com.discair.intuigames.discair.api.StackServiceInterface
import com.discair.intuigames.discair.api.airports.Airport
import com.discair.intuigames.discair.api.airports.Terminal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_airport_terminal.*

/**
 * @author RHA
 */
class AirportTerminalActivity : AppCompatActivity() {
    lateinit var airportNameSpinner: Spinner
    lateinit var airportTerminalSpinner: Spinner
    lateinit var airportValidateButton: Button
    lateinit var airportsList : List<Airport>
    var airportName: String = ""
    var airportTerminal: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_airport_terminal)

        airportNameSpinner = findViewById(R.id.airportNameSpinner)
        airportTerminalSpinner = findViewById(R.id.airportTerminalSpinner)
        airportValidateButton = findViewById(R.id.airportValidateButton)

        // Validate button listener
        airportValidateButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                try {
                    // get the informations from spinners component
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
}
