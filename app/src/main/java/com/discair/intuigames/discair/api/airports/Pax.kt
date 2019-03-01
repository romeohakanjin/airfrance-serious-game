package com.discair.intuigames.discair.api.airports

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Pax {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("pax_status")
    @Expose
    var status: String? = null

    @SerializedName("pax_type")
    @Expose
    var type: String? = null
}
