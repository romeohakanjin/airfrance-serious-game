package com.discair.intuigames.discair

import android.content.Intent
import android.support.v7.app.AppCompatActivity

import android.os.Bundle
import android.view.View
import android.widget.*
import com.discair.intuigames.discair.api.RetrofitClient
import com.discair.intuigames.discair.api.StackServiceInterface
import com.discair.intuigames.discair.api.airports.Airport
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    lateinit var choiceSearchSpinner : Spinner
    lateinit var searchValidateButton : Button
    lateinit var numSearchEditText : EditText
    lateinit var choiceSearchArray: Array<String>
    var choiceSearch: String = ""
    var num_for_search: String = ""

    var airportName: String = ""
    var airportTerminal: String = ""
    var flightTime: String = ""
    var destination: String = ""
    var flightNum: String = ""
    var boarding: String = ""
    var status: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        choiceSearchSpinner = findViewById(R.id.choiceSearchSpinner)
        numSearchEditText = findViewById(R.id.number_search_edittext)
        searchValidateButton = findViewById(R.id.searchValidateButton)

        this.displaySpinner()

        searchValidateButton.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                try{
                    //Get the information from spinner component
                    choiceSearch = choiceSearchSpinner.selectedItem.toString()
                    num_for_search = numSearchEditText.text.toString()

                    when (choiceSearch) {
                        "Numéro de vol" -> {
                            var intent = Intent(applicationContext, FlightActivity::class.java)
                            getFlightInfoByNumFlight(num_for_search.toInt())
                            if(airportName != ""){
                                intent.putExtra("flightTimeTextView", flightTime)
                                intent.putExtra("destinationTextView", destination)
                                intent.putExtra("flightTextView", flightNum)
                                intent.putExtra("boardingTextView", boarding)
                                intent.putExtra("statusTextView", status)
                                intent.putExtra("airportTerminal", airportTerminal)
                                intent.putExtra("airportName", airportName)
                                startActivity(intent)

                            }

                        }
                        "Numéro de référence passager" -> {
                            val intent = Intent(applicationContext, PassengerActivity::class.java)
                            intent.putExtra("referenceNumber", num_for_search.toInt())
                            startActivityForResult(intent, 0)
                        }
                    }


                } catch (exception : Exception){
                    exception.printStackTrace()
                    Toast.makeText(this@SearchActivity, "404 Not Found", Toast.LENGTH_SHORT).show()
                }
            }
        })


    }

    fun displaySpinner(){
        choiceSearchArray = Array<String>(2,  { "$it" })
        choiceSearchArray[0] = "Numéro de vol"
        choiceSearchArray[1] = "Numéro de référence passager"

        val spinnerArrayAdapter = ArrayAdapter<String>(this@SearchActivity, android.R.layout.simple_spinner_dropdown_item, choiceSearchArray)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        choiceSearchSpinner.setAdapter(spinnerArrayAdapter)
        choiceSearchSpinner.visibility = View.VISIBLE

        choiceSearchTextView.visibility = View.VISIBLE
    }

    fun getFlightInfoByNumFlight(numFlight: Int){

        val mService = RetrofitClient.getConnection()!!.create(StackServiceInterface::class.java)
        mService.getFlightByFlightNumber(numFlight).enqueue(object : Callback<List<Airport>> {
            override fun onResponse(call: Call<List<Airport>>, response: Response<List<Airport>>) {
                if (response.isSuccessful()) {
                    if (response.body()!!.size == 1){
                        val airport = response.body()!![0]
                        val flight = airport.flight

                        airportName = airport!!.name.toString()
                        flightTime = flight!!.departureTime.toString()
                        destination = flight!!.destination.toString()
                        flightNum = flight!!.numFlight.toString()
                        boarding = flight!!.terminal.toString()
                        status = flight!!.status.toString()
                    }
                }
            }

            override fun onFailure(call: Call<List<Airport>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

}
