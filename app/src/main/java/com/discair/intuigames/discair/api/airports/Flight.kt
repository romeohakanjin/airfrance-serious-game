package com.discair.intuigames.discair.api.airports

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Flight {

    @SerializedName("_id")
    @Expose
    var id: String? = null
    @SerializedName("num_flight")
    @Expose
    var numFlight: Int? = null
    @SerializedName("destination")
    @Expose
    var destination: String? = null
    @SerializedName("departure_time")
    @Expose
    var departureTime: String? = null
    @SerializedName("arrival_time")
    @Expose
    var arrivalTime: String? = null
    @SerializedName("departure_date")
    @Expose
    var departureDate: String? = null
    @SerializedName("terminal")
    @Expose
    var terminal: String? = null
    @SerializedName("escale")
    @Expose
    var escale: Escale? = null
    @SerializedName("passenger")
    @Expose
    var passenger: List<Passenger>? = null
    @SerializedName("incident")
    @Expose
    var incident: List<Incident>? = null
    @SerializedName("status")
    @Expose
    var status: Status? = null
}
