package com.health.beefit.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.health.beefit.R
import com.health.beefit.activities.fragments.*
import com.health.beefit.utils.JWTUtils

class HomeActivity : AppCompatActivity() {

    private lateinit var token: String
    private lateinit var userId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get token
        token = intent.getStringExtra("token") ?: ""

        // Decode the JWT token
        val decodedJWT = JWTUtils.decodeJWT(token)

        // Check if decoding was successful and the payload is not null
        if (decodedJWT != null) {
            // Extract user ID from the decoded JWT payload
            userId = decodedJWT.getString("userId")

            // Log user ID
            Log.d("UserID", "User ID: $userId")
        } else {
            // Decoding failed or invalid JWT token
            Log.d("UserID", "Fail to obtain user id from token")
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.homeBottomNavigationView)
        val menu = bottomNavigationView.menu
        menu.findItem(R.id.chatMenu).isVisible = true
        menu.findItem(R.id.storeMenu).isVisible = true
        menu.findItem(R.id.settingsMenu).isVisible = true

        // Set menu item selection listener
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.chatMenu -> {
                    // Replace the current fragment with the plans fragment
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.homeFragmentContainerView, ChatFragment())
                        .commit()
                    true
                }
                R.id.storeMenu -> {
                    // Replace the current fragment with the rewards fragment
                    val storeFragment = StoreFragment.newInstance(userId)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.homeFragmentContainerView, storeFragment)
                        .commit()
                    true
                }
                R.id.settingsMenu -> {
                    // Replace the current fragment with the settings fragment
                    val settingsFragment = SettingsFragment.newInstance(userId)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.homeFragmentContainerView, settingsFragment)
                        .commit()
                    true
                }
                R.id.homeMenu -> {
                    // Replace the current fragment with the home fragment
                    val homeFragment = HomeFragment.newInstance(userId)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.homeFragmentContainerView, homeFragment)
                        .commit()
                    true
                }
                else -> false
            }
        }

        // Replace the initial fragment with the HomeFragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.homeFragmentContainerView, HomeFragment.newInstance(userId))
                .commit()
        }
    }

    override fun onBackPressed() {
        // Check if the back stack has fragments
        if (supportFragmentManager.backStackEntryCount > 0) {
            // If there are fragments in the back stack, pop the back stack
            supportFragmentManager.popBackStack()
        } else {
            // If there are no fragments in the back stack, ask the user if they want to exit
            AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit beeFit?")
                .setPositiveButton("Yes") { _, _ ->
                    finishAffinity() // Exit the app COMPLETELY
                    super.onBackPressed()
                }
                .setNegativeButton("No", null) // Do nothing if "No" is clicked
                .show()
        }
    }


}