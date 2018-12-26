package com.discair.intuigames.discair.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author RHA
 */
object RetrofitClient {

    private val BASE_URL = "https://peaceful-castle-89767.herokuapp.com/"
    private var retrofit: Retrofit? = null

    fun getConnection() : Retrofit?{
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return retrofit
    }
}