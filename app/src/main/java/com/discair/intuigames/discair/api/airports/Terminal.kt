package com.discair.intuigames.discair.api.airports

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Terminal {
    @SerializedName("name")
    @Expose
    var name: String? = null
}
