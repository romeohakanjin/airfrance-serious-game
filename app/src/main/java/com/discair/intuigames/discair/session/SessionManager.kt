package com.discair.intuigames.discair.session

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.discair.intuigames.discair.MainActivity

/**
 * @author RHA
 */
public class SessionManager{
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
        val KEY_REGISTRATION_NUMBER: String = "registrationNummber"
        val KEY_PASSWORD : String = "password"

    }

    fun createLoginSession(registrationNumber: String, password: Int){
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_REGISTRATION_NUMBER, registrationNumber)
        editor.putInt(KEY_PASSWORD, password)
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

    fun getUserRegistrationNumber(): String{
        var registrationNumber : String = preferences.getString(KEY_REGISTRATION_NUMBER, null)
        return registrationNumber
    }

    fun getUserPassword(): Int{
        var password : Int = preferences.getInt(KEY_PASSWORD, 0)
        return password
    }

    fun logoutUser(){
        editor.clear()
        editor.commit()

        var intent: Intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun isLoggedIn(): Boolean{
        return preferences.getBoolean(IS_LOGIN, false)
    }
}