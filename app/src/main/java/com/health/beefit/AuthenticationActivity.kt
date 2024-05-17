package com.health.beefit

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.content.Intent



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

        val loginButton: Button = findViewById<Button>(R.id.signupButton)
        loginButton.setOnClickListener {
            // Start the LoginActivity
            val intent = Intent(this@AuthenticationActivity, SignupActivity::class.java)
            startActivity(intent)
        }

//        val signUpButton: Button = findViewById(R.id.signupButton)
//        signUpButton.setOnClickListener {
//            // Start the SignUpActivity
//            val intent = Intent(this@AuthenticationActivity, SignUpActivity::class.java)
//            startActivity(intent)
//        }

    }
}