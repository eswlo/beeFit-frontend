package com.health.beefit.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.health.beefit.R
import com.health.beefit.data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.health.beefit.utils.*


class LoginActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.76:3000") // backend API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val usernameEditText = findViewById<EditText>(R.id.usernameLoginEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordLoginEditText)


        // click listener for the login button
        loginButton.setOnClickListener {
            // Get input values
            val userName = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Check if any of the fields are empty
            if (userName.isEmpty() || password.isEmpty()) {
                // Display a warning message
                Log.w("LoginActivity", "One or more fields are empty!")
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener  // Return to prevent further execution
            }

            val loginRequest = LoginRequest(userName, password)
            // Call the login method defined in ApiService
            val call = apiService.logIn(loginRequest)
            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val statusCode = response.code()
                    when (statusCode) {
                        200 -> {
                            // Successful login
                            val loginResponse = response.body()
                            if (loginResponse != null) {
                                val token = loginResponse.token
                                Log.d("LoginActivity", "Username: $userName")
                                Log.d("LoginActivity", "Password: $password")
//                                Log.d("LoginActivity", "Token: $token")
                                Toast.makeText(this@LoginActivity, "Login Succeeded!", Toast.LENGTH_SHORT).show()

                                // Start the homepage activity and pass the token as an extra
                                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                                intent.putExtra("token", token)
                                startActivity(intent)
                                finish() // Finish the current activity to prevent going back to the login screen
                            } else {
                                // Handle the case where response body is null
                            }
                        }
                        404 -> {
                            // User not found
                            Toast.makeText(this@LoginActivity, "User not found", Toast.LENGTH_SHORT).show()
                        }
                        401 -> {
                            // Invalid username or password
                            Toast.makeText(this@LoginActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
                        }
                        500 -> {
                            // Internal server error
                            Toast.makeText(this@LoginActivity, "Internal server error occurred", Toast.LENGTH_SHORT).show()
                        }
                        // Add more cases as needed for other status codes
                        else -> {
                            // Handle other status codes if necessary
                            Toast.makeText(this@LoginActivity, "Unexpected error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    // Handle network errors or exceptions
                    Log.e("LoginActivity", "Failed to send login request", t)
                    Toast.makeText(this@LoginActivity, "Failed to send login request", Toast.LENGTH_SHORT).show()
                }
            })

        }
    }
}