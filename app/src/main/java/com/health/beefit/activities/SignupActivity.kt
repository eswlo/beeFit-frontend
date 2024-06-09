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
import com.health.beefit.api.ApiService
import com.health.beefit.data.UserData
import com.health.beefit.data.LoginResponse
import com.health.beefit.api.NetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
//    private lateinit var serverUrl: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Initialize ApiService
        apiService = NetworkService.apiService

        val registerButton = findViewById<Button>(R.id.registerButton)
        val firstNameEditText = findViewById<EditText>(R.id.firstNameEditText)
        val lastNameEditText = findViewById<EditText>(R.id.lastNameEditText)
        val phoneEditText = findViewById<EditText>(R.id.phoneEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)


        // click listener for the register button
        registerButton.setOnClickListener {
            // Get input values
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val phoneNumber = phoneEditText.text.toString()
            val email = emailEditText.text.toString()
            val userName = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // log the user input
            Log.d("SignupActivity", "First Name: $firstName")
            Log.d("SignupActivity", "Last Name: $lastName")
            Log.d("SignupActivity", "Phone: $phoneNumber")
            Log.d("SignupActivity", "Email: $email")
            Log.d("SignupActivity", "Username: $userName")
            Log.d("SignupActivity", "Password: $password")

            // Check if any of the fields are empty
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || userName.isEmpty() || password.isEmpty()) {
                // Display a warning message
                Log.w("SignupActivity", "One or more fields are empty!")
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener  // Return to prevent further execution
            }

            val userData = UserData(firstName, lastName, phoneNumber, email, userName, password)
            // Call the signUp method defined in ApiService
            val call = apiService.signUp(userData)
            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val registrationResponse = response.body()
                        val token = registrationResponse!!.token
                        // Handle successful response
                        Toast.makeText(this@SignupActivity, "You're all set!", Toast.LENGTH_SHORT).show()

                        // Navigate to the user page
                        val intent = Intent(this@SignupActivity, HomeActivity::class.java)
                        intent.putExtra("token", token)
                        startActivity(intent)

                        // Finish the current activity (optional)
                        finish()
                    } else {
                        // Handle error response
                        Log.e("SignupActivity", "Signup failed with code: ${response.code()}")
                        Toast.makeText(this@SignupActivity, "Signup failed. Please try again", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    // Handle failure
                    Log.e("SignupActivity", "Failed to send signup request", t)
                    Toast.makeText(this@SignupActivity, "Failed to send signup request", Toast.LENGTH_SHORT).show()
                }
            })

        }
    }
}