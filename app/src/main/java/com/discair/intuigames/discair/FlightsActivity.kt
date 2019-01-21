package com.discair.intuigames.discair

import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.discair.intuigames.discair.api.RetrofitClient
import com.discair.intuigames.discair.api.StackServiceInterface
import com.discair.intuigames.discair.api.aeroports.Aeroport
import com.discair.intuigames.discair.api.aeroports.Flight
import com.discair.intuigames.discair.fragment.FlightsArrivalFragment
import com.discair.intuigames.discair.fragment.FlightsDepartureFragment

import kotlinx.android.synthetic.main.activity_flights.*
import kotlinx.android.synthetic.main.fragment_flights_departure.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author RHA
 */
class FlightsActivity : AppCompatActivity() {
    var aeroportName: String = ""
    var aeroportTerminal: String = ""
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
        aeroportName = intent.getStringExtra("aeroportName").toString()
        aeroportTerminal = intent.getStringExtra("aeroportTerminal").toString()

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
            bundle.putString("aeroportName", aeroportName)
            bundle.putString("aeroportTerminal", aeroportTerminal)

            if(position == 0) {
                val flightDepartureFragment = FlightsDepartureFragment()
                flightDepartureFragment.arguments = bundle
                return flightDepartureFragment
            } else if(position == 1) {
                val flightsArrivalFragment = FlightsArrivalFragment()
                flightsArrivalFragment.arguments = bundle
                return flightsArrivalFragment
            } else {
                return null
            }
        }

        override fun getCount(): Int {
            // Show 2 total pages.
            return 2
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_flights_departure, container, false)
            rootView.flightTerminalTextView.text = getString(R.string.section_format, arguments?.getInt(ARG_SECTION_NUMBER))
            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                System.out.println(sectionNumber)
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }
}
