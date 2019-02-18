package com.discair.intuigames.discair.api.airports

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Gp {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

}
