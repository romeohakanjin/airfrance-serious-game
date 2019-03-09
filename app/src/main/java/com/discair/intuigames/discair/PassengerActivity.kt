package com.discair.intuigames.discair

import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_passenger.*

/**
 * @author SLI
 */
class PassengerActivity : AppCompatActivity() {
    private var referenceNumber: String = ""
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
        setContentView(R.layout.activity_passenger)

        //get intent extra
        referenceNumber = intent.getIntExtra("referenceNumber", 0).toString()

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(passengerActivityTableLayout))
        passengerActivityTableLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            // getItem is called to instantiate the fragment for the given page.
            val bundle = Bundle()
            bundle.putString("referenceNumber", referenceNumber)

            when (position) {
                0 -> {
                    val passengerInfoFragment = PassengerInfoFragment()
                    passengerInfoFragment.arguments = bundle
                    return passengerInfoFragment
                }
                1 -> {
                    val personalInfoFragment = PersonalInfoFragment()
                    personalInfoFragment.arguments = bundle
                    return personalInfoFragment
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