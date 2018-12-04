package com.discair.intuigames.discair.api

import com.discair.intuigames.discair.api.agents.Agents
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface StackServiceInterface {

    @GET("agents")
    fun getAgents(): Call<List<Agents>>

    @GET("agents")
    fun getAgent(@Query("registration_number") registration_number: Int, @Query("password") password: Int): Call<List<Agents>>

    @POST("createAgent")
    fun createAgent(agent: Agents): Call<ResponseBody>


}