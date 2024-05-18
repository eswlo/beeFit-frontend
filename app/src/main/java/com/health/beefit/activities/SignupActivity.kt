package com.health.beefit.activities

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
import com.health.beefit.data.ApiService
import com.health.beefit.data.RegistrationData
import com.health.beefit.data.RegistrationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SignupActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://0.0.0.0:3000") // backend API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
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

            val registrationData = RegistrationData(firstName, lastName, phoneNumber, email, userName, password)
            // Call the signUp method defined in ApiService
            val call = apiService.signUp(registrationData)
            call.enqueue(object : Callback<RegistrationResponse> {
                override fun onResponse(call: Call<RegistrationResponse>, response: Response<RegistrationResponse>) {
                    if (response.isSuccessful) {
                        val registrationResponse = response.body()
                        // Handle successful response
                        Toast.makeText(this@SignupActivity, "You're all set!", Toast.LENGTH_SHORT).show()

//                        // Navigate to the user page
//                        val intent = Intent(this@SignupActivity, HomePageActivity::class.java)
//                        startActivity(intent)

                        // Finish the current activity (optional)
                        finish()
                    } else {
                        // Handle error response
                        Log.e("SignupActivity", "Signup failed with code: ${response.code()}")
                        Toast.makeText(this@SignupActivity, "Signup failed. Please try again", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                    // Handle failure
                    Log.e("SignupActivity", "Failed to send signup request", t)
                    Toast.makeText(this@SignupActivity, "Failed to send signup request", Toast.LENGTH_SHORT).show()
                }
            })

        }
    }
}