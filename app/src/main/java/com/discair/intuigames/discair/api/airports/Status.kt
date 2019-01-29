package com.discair.intuigames.discair.api.airports

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Status {

    @SerializedName("_id")
    @Expose
    var id: String? = null
    @SerializedName("wording")
    @Expose
    var wording: String? = null

}
