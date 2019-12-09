package com.example.watcher

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.example.watcher.fragments.DashboardFragmentAdapter

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)

        val viewPager: ViewPager = findViewById(R.id.view_pager)
        var tabs: TabLayout = findViewById(R.id.tabs)
        viewPager.adapter =
            DashboardFragmentAdapter(supportFragmentManager, this, arrayOf("BroadCast", "Contacts", "Devices"))


        tabs = setupTabLayout(tabs)
        tabs.setupWithViewPager(viewPager)

    }

    private fun setUpViewPager(viewPager: ViewPager): ViewPager {
        return viewPager
    }

    private fun setupTabLayout(layout: TabLayout): TabLayout {
        layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        return layout
    }
}