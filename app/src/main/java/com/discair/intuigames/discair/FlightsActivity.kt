package com.discair.intuigames.discair

import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.discair.intuigames.discair.fragment.FlightsArrivalFragment
import com.discair.intuigames.discair.fragment.FlightsDepartureFragment

import kotlinx.android.synthetic.main.activity_flights.*

/**
 * @author RHA
 */
class FlightsActivity : AppCompatActivity() {
    private var airportName: String = ""
    private var airportTerminal: String = ""
    private var missionNumber: Int = 0
    private lateinit var builder : AlertDialog.Builder
    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flights)

        //get intent extra
        airportName = intent.getStringExtra("airportName").toString()
        airportTerminal = intent.getStringExtra("airportTerminal").toString()
        missionNumber = intent.getIntExtra("missionNumber", 0)

        builder = AlertDialog.Builder(this@FlightsActivity)

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(flightActivityTableLayout))
        flightActivityTableLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        if (3 == missionNumber) {
            // Set the alert dialog title
            builder.setTitle(R.string.mission_information)

            // Display a message on alert dialog
            builder.setMessage(R.string.third_mission_tips)

            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton(R.string.ok_string) { _, _ ->
                // do nothing
            }

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            // Display the alert dialog on app interface
            dialog.show()
        }
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            // getItem is called to instantiate the fragment for the given page.
            val bundle = Bundle()
            bundle.putString("airportName", airportName)
            bundle.putString("airportTerminal", airportTerminal)
            bundle.putInt("missionNumber", missionNumber)

            when (position) {
                0 -> {
                    val flightDepartureFragment = FlightsDepartureFragment()
                    flightDepartureFragment.arguments = bundle
                    return flightDepartureFragment
                }
                1 -> {
                    val flightsArrivalFragment = FlightsArrivalFragment()
                    flightsArrivalFragment.arguments = bundle
                    return flightsArrivalFragment
                }
                else -> return null
            }
        }

        override fun getCount(): Int {
            // Show 2 total pages.
            return 2
        }
    }
}
