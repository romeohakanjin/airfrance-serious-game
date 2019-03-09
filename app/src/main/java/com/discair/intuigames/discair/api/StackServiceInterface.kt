package com.discair.intuigames.discair.api

import com.discair.intuigames.discair.api.airports.Airport
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
     * Get airport list by name and terminal
     * @param airportName: airport name
     * @param airportTerminal: airport terminal
     */
    @GET("airports/{name}/{terminal}")
    fun getAirport(@Path("name") airportName: String, @Path("terminal") airportTerminal: String): Call<List<Airport>>

    /**
     * get Airport WITH flight(s) list by airport name and terminal name
     * @param airportName: airport name
     * @param airportTerminal: airport terminal
     */
    @GET("airports/flights/{name}/{terminal}")
    fun getFlights(@Path("name") airportName: String, @Path("terminal") airportTerminal: String): Call<List<Airport>>

    /**
     * get Airport WITH flight(s) for arrival list by airport name and terminal name
     * @param airportName: airport name
     */
    @GET("flights/{destination}/arrival")
    fun getFlightsArrival(@Path("destination") destination: String): Call<List<Airport>>

    /**
     *
     * @param flightTime
     * @param destination
     * @param flight
     * @param boarding
     * @param status
     */
        @GET("flight/{flighttime}/{destination}/{flight}/{boarding}/{status}")
    fun getFlightInformations(@Path("flighttime") flightTime: String, @Path("destination") destination: String, @Path("flight") flight: String, @Path("boarding") boarding: String, @Path("status") status: String): Call<List<Airport>>

    @GET("passengers/{num_flight}")
    fun getPassengersByFLightNumber(@Path("num_flight") numFlight: Int): Call<List<Airport>>

    @GET("passenger/{reference_number}")
    fun getPassengerByReferenceNumber(@Path("reference_number") referenceNumber: String): Call<List<Airport>>
}