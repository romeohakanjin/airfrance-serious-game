package com.discair.intuigames.discair.api

import com.discair.intuigames.discair.api.aeroports.Aeroport
import com.discair.intuigames.discair.api.agents.Agents
import retrofit2.Call
import retrofit2.http.*

/**
 * @author RHA
 */
interface StackServiceInterface {

    /**
     * Get a list of agents
     * @return Call<List<Agents>>: List of agents
     */
    @GET("agents")
    fun getAgents(): Call<List<Agents>>

    /**
     * Get one agent with corresponding reigstration number and password
     * @param registration_number: registration number of the agent
     * @param password: password of the agent
     * @return Call<List<Agents>>: List of agents
     */
    @GET("agents/{registration_number}/{password}")
    fun getAgent(@Path("registration_number") registration_number: Int, @Path("password") password: Int): Call<List<Agents>>

    /**
     * Get aaeroport list by name and terminal
     * @param aeroportName: aeroport name
     * @param aeroportTerminal: aeroport terminal
     */
    @GET("aeroports/{name}/{terminal}")
    fun getAeroport(@Path("name") aeroportName: String, @Path("terminal") aeroportTerminal: String): Call<List<Aeroport>>

    /**
     * get Aeroport WITH flight(s) list by aeroport name and terminal name
     * @param aeroportName: aeroport name
     * @param aeroportTerminal: aeroport terminal
     */
    @GET("aeroports/flights/{name}/{terminal}")
    fun getFlights(@Path("name") aeroportName: String, @Path("terminal") aeroportTerminal: String): Call<List<Aeroport>>

}