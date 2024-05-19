package com.health.beefit.activities

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.health.beefit.R
import com.health.beefit.activities.fragments.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.homeBottomNavigationView)
        val menu = bottomNavigationView.menu
        menu.findItem(R.id.plansMenu).isVisible = true
        menu.findItem(R.id.rewardsMenu).isVisible = true
        menu.findItem(R.id.settingsMenu).isVisible = true

        // Set menu item selection listener
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.plansMenu -> {
                    // Replace the current fragment with the plans fragment
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.homeFragmentContainerView, PlansFragment())
                        .commit()
                    true
                }
                R.id.rewardsMenu -> {
                    // Replace the current fragment with the rewards fragment
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.homeFragmentContainerView, RewardsFragment())
                        .commit()
                    true
                }
                R.id.settingsMenu -> {
                    // Replace the current fragment with the settings fragment
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.homeFragmentContainerView, SettingsFragment())
                        .commit()
                    true
                }
                R.id.homeMenu -> {
                    // Replace the current fragment with the home fragment
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.homeFragmentContainerView, HomeFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }


    }
}