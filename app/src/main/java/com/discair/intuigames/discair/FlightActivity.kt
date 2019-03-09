package com.discair.intuigames.discair

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import com.discair.intuigames.discair.fragment.FlightInformationsFragment
import com.discair.intuigames.discair.fragment.PassengersListFragment
import kotlinx.android.synthetic.main.activity_flight.*

/**
 * @author RHA
 */
class FlightActivity : AppCompatActivity() {
    private var airportTerminal: String = ""
    private var airportName: String = ""
    private var flightTimeTextView: String = ""
    private var destinationTextView: String = ""
    private var flightTextView: String = ""
    private var boardingTextView: String = ""
    private var statusTextView: String = ""

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
        setContentView(R.layout.activity_flight)

        //get intent extra
        airportTerminal = intent.getStringExtra("airportTerminal").toString()
        airportName = intent.getStringExtra("airportName").toString()
        flightTimeTextView = intent.getStringExtra("flightTimeTextView").toString()
        destinationTextView = intent.getStringExtra("destinationTextView").toString()
        flightTextView = intent.getStringExtra("flightTextView").toString()
        boardingTextView = intent.getStringExtra("boardingTextView").toString()
        statusTextView = intent.getStringExtra("statusTextView").toString()

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(flightActivityTableLayout))
        flightActivityTableLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            // getItem is called to instantiate the fragment for the given page.
            val bundle = Bundle()
            bundle.putString("airportTerminal", airportTerminal)
            bundle.putString("airportName", airportName)
            bundle.putString("flightTimeTextView", flightTimeTextView)
            bundle.putString("destinationTextView", destinationTextView)
            bundle.putString("flightTextView", flightTextView)
            bundle.putString("boardingTextView", boardingTextView)
            bundle.putString("statusTextView", statusTextView)

            when (position) {
                0 -> {
                    val flightInformationsFragment = FlightInformationsFragment()
                    flightInformationsFragment.arguments = bundle
                    return flightInformationsFragment
                }
                1 -> {
                    val passengersListFragment = PassengersListFragment()
                    passengersListFragment.arguments = bundle
                    return passengersListFragment
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

