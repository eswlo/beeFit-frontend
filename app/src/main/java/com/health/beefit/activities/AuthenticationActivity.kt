package com.health.beefit.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.content.Intent
import com.health.beefit.R


class AuthenticationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_authentication)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        val serverUrl = "http://192.168.50.101:3000" // Specify backend API base URL here

        val signUpButton: Button = findViewById<Button>(R.id.signupButton)
        signUpButton.setOnClickListener {
            // Start the SignupActivity
            val intent = Intent(this@AuthenticationActivity, SignupActivity::class.java)
//            intent.putExtra("SERVER_URL", serverUrl)
            startActivity(intent)
        }

        val loginButton: Button = findViewById(R.id.loginButton)
        loginButton.setOnClickListener {
            // Start the LoginActivity
            val intent = Intent(this@AuthenticationActivity, LoginActivity::class.java)
//            intent.putExtra("SERVER_URL", serverUrl)
            startActivity(intent)
        }

    }
}