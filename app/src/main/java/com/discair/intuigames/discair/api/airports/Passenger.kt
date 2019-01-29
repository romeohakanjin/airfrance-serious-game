package com.discair.intuigames.discair.api.airports

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Passenger {

    @SerializedName("_id")
    @Expose
    var id: String? = null
    @SerializedName("reference_number")
    @Expose
    var referenceNumber: Int? = null
    @SerializedName("last_name")
    @Expose
    var lastName: String? = null
    @SerializedName("first_name")
    @Expose
    var firstName: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("mobile")
    @Expose
    var mobile: String? = null
    @SerializedName("mail")
    @Expose
    var mail: String? = null
    @SerializedName("status")
    @Expose
    var status: Status? = null
    @SerializedName("luggage")
    @Expose
    var luggage: Luggage? = null

}
