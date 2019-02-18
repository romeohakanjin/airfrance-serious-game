package com.discair.intuigames.discair.api.airports

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Seats {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("premiere")
    @Expose
    var premiere: String? = null

    @SerializedName("business")
    @Expose
    var business: String? = null

    @SerializedName("eco")
    @Expose
    var eco: String? = null
}