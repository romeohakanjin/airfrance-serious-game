package com.discair.intuigames.discair

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import com.discair.intuigames.discair.api.RetrofitClient
import com.discair.intuigames.discair.api.StackServiceInterface
import com.discair.intuigames.discair.api.airports.Airport
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.TextView
import android.view.View
import com.discair.intuigames.discair.api.airports.Passenger


/**
 * @author SLI
 */
class PassengersActivity : AppCompatActivity() {
    /**
     * Variables
     */
    var passengerList: List<Passenger>? = null
    var errorMessage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passengers)
        //TODO: Remplacer le numFlight par le numFlight qui sera sélectionné par l'agent
        this.getPassengersByFlightNumber(548)
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
                                val passengersTableLayout = findViewById(R.id.PassengersTableLayout) as TableLayout

                                for (pass in passengerList as List<Passenger>){
                                    val tableRowId = TextView.generateViewId()

                                    val row = TableRow(this@PassengersActivity)
                                    val lp = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
                                    row.id = tableRowId
                                    row.layoutParams=lp

                                    val refNumber = TextView(this@PassengersActivity)
                                    refNumber.tag = "refNumber"
                                    refNumber.text = pass.referenceNumber.toString()
                                    refNumber.gravity = Gravity.CENTER;
                                    row.addView(refNumber)

                                    val lastName = TextView(this@PassengersActivity)
                                    lastName.tag = "lastNameTag"
                                    lastName.text = pass.lastName
                                    lastName.gravity = Gravity.CENTER;
                                    row.addView(lastName)

                                    val firstName = TextView(this@PassengersActivity)
                                    firstName.tag = "firstNameTag"
                                    firstName.text = pass.firstName
                                    firstName.gravity = Gravity.CENTER;
                                    row.addView(firstName)

                                    val mail = TextView(this@PassengersActivity)
                                    mail.tag = "mailTag"
                                    mail.text = pass.mail
                                    mail.gravity = Gravity.CENTER;
                                    row.addView(mail)

                                    row.setOnClickListener(object : View.OnClickListener {
                                        override fun onClick(v: View?) {
                                            try {
                                                val trow = v as TableRow
                                                trow.getVirtualChildAt(trow.id)
                                                val textView : TextView = trow.getChildAt(0) as TextView
                                                var referenceNumber : Int = textView.text.toString().toInt()

                                                val intent = Intent(baseContext, PassengerActivity::class.java)
                                                intent.putExtra("referenceNumber", referenceNumber)
                                                startActivityForResult(intent, 0)
                                            } catch (exception: Exception) {
                                                Toast.makeText(this@PassengersActivity, "Click Broken", Toast.LENGTH_SHORT).show()
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
        Toast.makeText(this@PassengersActivity, message, Toast.LENGTH_SHORT).show()
    }
}
