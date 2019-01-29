package com.discair.intuigames.discair.api.airports

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Escale {

    @SerializedName("_id")
    @Expose
    var id: String? = null
    @SerializedName("departure_time")
    @Expose
    var departureTime: String? = null
    @SerializedName("destination")
    @Expose
    var destination: String? = null
    @SerializedName("arrival_time")
    @Expose
    var arrivalTime: String? = null
    @SerializedName("place_departure")
    @Expose
    var placeDeparture: String? = null

}
