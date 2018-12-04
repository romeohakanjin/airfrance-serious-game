package com.discair.intuigames.discair.api.agents

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Agents {

    @SerializedName("_id")
    @Expose
    var id: String? = null
    @SerializedName("first_name")
    @Expose
    var firstName: String? = null
    @SerializedName("last_name")
    @Expose
    var lastName: String? = null
    @SerializedName("registration_number")
    @Expose
    var registrationNumber: Int? = null
    @SerializedName("password")
    @Expose
    var password: Int? = null

}