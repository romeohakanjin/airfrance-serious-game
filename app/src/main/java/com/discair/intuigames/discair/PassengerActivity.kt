package com.discair.intuigames.discair

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View

/**
 * @author SLI
 */
class PassengerActivity : AppCompatActivity() {
    var referenceNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passenger)

    }

    fun displayFragment(view: View) {
        // Get intent extra
        val bundle = intent.extras
        if (bundle != null) {
            referenceNumber = bundle.getInt("referenceNumber")
        }

        when (view.id) {
            R.id.PersonalInfo -> changeFragment(PersonalInfoFragment(), referenceNumber)
            R.id.PassengerInfo -> changeFragment(PassengerInfoFragment(), referenceNumber)
        }
    }

    fun changeFragment(fragment: Fragment, refNumber: Int) {
        System.out.println(""+fragment+"");
        val args = Bundle()
        args.putInt("referenceNumber", referenceNumber)
        fragment.arguments = args
        supportFragmentManager.beginTransaction().replace(R.id.PassengerActivityFragment, fragment).commit()
    }
}
