package com.discair.intuigames.discair

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.discair.intuigames.discair.api.RetrofitClient
import com.discair.intuigames.discair.api.StackServiceInterface
import com.discair.intuigames.discair.api.airports.Airport
import com.discair.intuigames.discair.api.airports.Passenger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.TextView
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow

class PersonalInfoFragment : Fragment() {
    /**
     * Variables
     */
    private lateinit var fragment: View
    private lateinit var referenceNumber: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragment =  inflater.inflate(R.layout.fragment_personal_info, container, false)

        // Get intent extra
        val bundle = this.arguments
        if (bundle != null) {
            referenceNumber = bundle.getString("referenceNumber")
        }
        this.getPassengeInfoByRefNumber(referenceNumber)
        return fragment
    }

    /**
     * Récupère la liste des passagers grâce au numéro de vol
     * Affiche les passagers dans la vue
     */
    fun getPassengeInfoByRefNumber(referenceNum: String){
        val mService = RetrofitClient.getConnection()!!.create(StackServiceInterface::class.java)
        mService.getPassengerByReferenceNumber(referenceNum).enqueue(object : Callback<List<Airport>> {
            override fun onResponse(call: Call<List<Airport>>, response: Response<List<Airport>>) {
                if (response.isSuccessful()) {
                    if (response.body()!!.size == 1){
                        val airport = response.body()!![0]
                        val flight = airport.flight
                        if(flight!!.passenger!!.size >= 1){
                            for(pass in flight!!.passenger as List<Passenger>){
                                if((pass.referenceNumber) == referenceNum){

                                    val personalInfoLayout = fragment.findViewById(R.id.PersonalInfo) as TableLayout
                                    val layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT)

                                    //ReferenceNumber
                                    val refNumRow = TableRow(fragment.context)
                                    refNumRow.id = TableRow.generateViewId()
                                    refNumRow.layoutParams = layoutParams

                                    val refNumberTitle = TextView(fragment.context)
                                    refNumberTitle.text = "Référence"
                                    refNumberTitle.gravity = Gravity.CENTER;
                                    refNumRow.addView(refNumberTitle)

                                    val refNumber = TextView(fragment.context)
                                    refNumber.tag = "refNumber"
                                    refNumber.text = pass.referenceNumber.toString()
                                    refNumber.gravity = Gravity.CENTER;
                                    refNumRow.addView(refNumber)

                                    //LastName
                                    val lastNameRow = TableRow(fragment.context)
                                    lastNameRow.id = TableRow.generateViewId()
                                    lastNameRow.layoutParams = layoutParams

                                    val lastNameTitle = TextView(fragment.context)
                                    lastNameTitle.text = "Nom"
                                    lastNameTitle.gravity = Gravity.CENTER;
                                    lastNameRow.addView(lastNameTitle)

                                    val lastName = TextView(fragment.context)
                                    lastName.tag = "lastName"
                                    lastName.text = pass.lastName.toString()
                                    lastName.gravity = Gravity.CENTER;
                                    lastNameRow.addView(lastName)

                                    //FirstName
                                    val firstNameRow = TableRow(fragment.context)
                                    firstNameRow.id = TableRow.generateViewId()
                                    firstNameRow.layoutParams = layoutParams

                                    val firstNameTitle = TextView(fragment.context)
                                    firstNameTitle.text = "Prénom"
                                    firstNameTitle.gravity = Gravity.CENTER;
                                    firstNameRow.addView(firstNameTitle)

                                    val firstName = TextView(fragment.context)
                                    firstName.tag = "firstName"
                                    firstName.text = pass.firstName.toString()
                                    firstName.gravity = Gravity.CENTER;
                                    firstNameRow.addView(firstName)

                                    //Address
                                    val addressRow = TableRow(fragment.context)
                                    addressRow.id = TableRow.generateViewId()
                                    addressRow.layoutParams = layoutParams

                                    val addressTitle = TextView(fragment.context)
                                    addressTitle.text = "Adresse"
                                    addressTitle.gravity = Gravity.CENTER;
                                    addressRow.addView(addressTitle)

                                    val address = TextView(fragment.context)
                                    address.tag = "address"
                                    address.text = pass.address.toString()
                                    address.gravity = Gravity.CENTER;
                                    addressRow.addView(address)

                                    //Mobile
                                    val mobileRow = TableRow(fragment.context)
                                    mobileRow.id = TableRow.generateViewId()
                                    mobileRow.layoutParams = layoutParams

                                    val mobileTitle = TextView(fragment.context)
                                    mobileTitle.text = "Mobile"
                                    mobileTitle.gravity = Gravity.CENTER;
                                    mobileRow.addView(mobileTitle)

                                    val mobile = TextView(fragment.context)
                                    mobile.tag = "mobile"
                                    mobile.text = pass.mobile.toString()
                                    mobile.gravity = Gravity.CENTER;
                                    mobileRow.addView(mobile)

                                    //Mail
                                    val mailRow = TableRow(fragment.context)
                                    mailRow.id = TableRow.generateViewId()
                                    mailRow.layoutParams = layoutParams

                                    val mailTitle = TextView(fragment.context)
                                    mailTitle.text = "Mail"
                                    mailTitle.gravity = Gravity.CENTER;
                                    mailRow.addView(mailTitle)

                                    val mail = TextView(fragment.context)
                                    mail.tag = "mail"
                                    mail.text = pass.mail.toString()
                                    mail.gravity = Gravity.CENTER;
                                    mailRow.addView(mail)

                                    //Status
                                    val statusRow = TableRow(fragment.context)
                                    statusRow.id = TableRow.generateViewId()
                                    statusRow.layoutParams = layoutParams

                                    val statusTitle = TextView(fragment.context)
                                    statusTitle.text = "Statut"
                                    statusTitle.gravity = Gravity.CENTER;
                                    statusRow.addView(statusTitle)

                                    val status = TextView(fragment.context)
                                    status.tag = "status"
                                    status.text = pass.status!!.wording.toString()
                                    status.gravity = Gravity.CENTER;
                                    statusRow.addView(status)

                                    //Add row to layout
                                    personalInfoLayout.run {
                                        addView(refNumRow)
                                        addView(lastNameRow)
                                        addView(firstNameRow)
                                        addView(addressRow)
                                        addView(mobileRow)
                                        addView(mailRow)
                                        addView(statusRow)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Airport>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}
