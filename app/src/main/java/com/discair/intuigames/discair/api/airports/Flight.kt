package com.discair.intuigames.discair.api.airports

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Flight {
    @SerializedName("num_flight")
    @Expose
    var numFlight: String? = null

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

    @SerializedName("gate")
    @Expose
    var gate: String? = null

    @SerializedName("seats")
    @Expose
    var seats: Seats? = null

    @SerializedName("escale")
    @Expose
    var escale: Escale? = null

    @SerializedName("passenger")
    @Expose
    var passenger: List<Passenger>? = null

    @SerializedName("status")
    @Expose
    var status: Status? = null
}