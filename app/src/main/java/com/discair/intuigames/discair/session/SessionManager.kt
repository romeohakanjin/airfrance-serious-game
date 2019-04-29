package com.discair.intuigames.discair.session

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.discair.intuigames.discair.LoginActivity
import com.discair.intuigames.discair.MainActivity

/**
 * @author RHA
 */
class SessionManager{
    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var context: Context
    var PRIVATE_MODE: Int = 0

    constructor(context: Context){
        this.context = context
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = preferences.edit()
    }

    companion object {
        val PREF_NAME: String = "login"
        val IS_LOGIN: String = "isLoggedIn"
        val KEY_REGISTRATION_NUMBER: String = "registrationNumber"
        val KEY_PASSWORD : String = "password"
        val MISSION_NUMBER : String = "missionNumber"
        val IS_MISSION_COMPLETED : String = "isMissionCompleted"

    }

    fun createLoginSession(registrationNumber: Int, password: Int, missionNumber: Int, isMissionCompleted: Boolean){
        editor.putBoolean(IS_LOGIN, true)
        editor.putInt(KEY_REGISTRATION_NUMBER, registrationNumber)
        editor.putInt(KEY_PASSWORD, password)
        editor.putInt(MISSION_NUMBER, missionNumber)
        editor.putBoolean(IS_MISSION_COMPLETED, isMissionCompleted)
        editor.commit()
    }

    fun checkLogin(){
        if (!this.isLoggedIn()){
            var intent: Intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    fun getUserMissionNumber(): Int{
        var missionNumber : Int = preferences.getInt(MISSION_NUMBER, 0)
        return missionNumber
    }

    fun getIsMissionCompleted(): Boolean{
        var isMissionCompleted : Boolean = preferences.getBoolean(IS_MISSION_COMPLETED, false)
        return isMissionCompleted
    }

        fun getUserRegistrationNumber(): Int{
        var registrationNumber : Int = preferences.getInt(KEY_REGISTRATION_NUMBER, 0)
        return registrationNumber
    }

    fun getUserPassword(): Int{
        var password : Int = preferences.getInt(KEY_PASSWORD, 0)
        return password
    }

    fun logoutUser(){
        editor.clear()
        editor.commit()
    }

    fun isLoggedIn(): Boolean{
        return preferences.getBoolean(IS_LOGIN, false)
    }
}