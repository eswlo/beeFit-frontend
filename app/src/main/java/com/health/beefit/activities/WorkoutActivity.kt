package com.health.beefit.activities

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.health.beefit.R
import com.health.beefit.data.*
import com.health.beefit.api.ApiService
import com.health.beefit.api.NetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs

class WorkoutActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var userId: String
    private var earnedPoints: Int = 0
    private lateinit var apiService: ApiService
    private lateinit var sensorManger: SensorManager
    private var sensorStarted = false
    private var previousSides: Float = 0.0f // Variable to store the previous value of sides
    private var previousUpDown: Float = 0.0f // Variable to store the previous value of upDown
    private val epsilon: Float = 0.03f // Small threshold to account for very minor changes; can be used for intensity setting

    //var for counter
    private var isRunning = false
    private var counterSeconds = 0
    private var counterPoints = 0
    private var earnedPointText = "0"
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            counterSeconds++
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_workout)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //get userId & points
        userId = intent.getStringExtra("userId") ?: ""
        earnedPoints = intent.getIntExtra("earnedPoints", 0)

        // Initialize ApiService
        apiService = NetworkService.apiService

        // Ensure the app remain in light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val workoutBeginBtn = findViewById<Button>(R.id.workoutBeginButton)
        val workoutEndBtn = findViewById<Button>(R.id.workoutEndButton)

        workoutBeginBtn.setOnClickListener {
            if(!sensorStarted) {
                runSensor()
                sensorStarted = true
            }
        }

        workoutEndBtn.setOnClickListener {
            if(sensorStarted) {
                sensorStarted = false
                // Unregister sensor listener
                sensorManger.unregisterListener(this)
            }

            // Convert counterSeconds into minutes for points
            // May change the formula depending the need
//            counterPoints = (counterSeconds % 3600) / 60
//            earnedPoints += counterPoints

            earnedPoints+= counterSeconds
            val updatePoints = UpdatePoints(earnedPoints)
            val call = apiService.updateEarnedPoints(userId, updatePoints)

            call.enqueue(object : Callback<UserData> {
                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                    if (response.isSuccessful) {
//                        val responseCode = response.code()
//                        println(responseCode)
//                        val responseBody = response.body()
//                        println(responseBody!!.earnedPoints!!.toString())
                        // Handle successful response
                        Toast.makeText(this@WorkoutActivity, "Points updated!", Toast.LENGTH_SHORT).show()
                    } else {
                        // Handle error response
                        Log.e("WorkoutActivity", "Points update failed with code: ${response.code()}")
                        Toast.makeText(this@WorkoutActivity, "Points update failed.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserData>, t: Throwable) {
                    // Handle failure
                    Log.e("WorkoutActivity", "Failed to update points", t)
                    Toast.makeText(this@WorkoutActivity, "Failed to update points", Toast.LENGTH_SHORT).show()
                }
            })
            //finish the current workout activity and return the HomeFragment
            finish()
        }
    }


    private fun startCounter() {
        if(!isRunning) {
            handler.postDelayed(runnable, 1000)
            isRunning = true
        }
    }

    private fun pauseCounter() {
        if(isRunning) {
            handler.removeCallbacks(runnable)
            isRunning = false
        }
    }

    private fun runSensor() {
        sensorManger = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorManger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManger.registerListener(this,
                it,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val sides = event.values[0]
            val upDown = event.values[1]

            // Calculate the difference between the current and previous sides & upDown value
            val sidesDifference = abs(sides - previousSides)
            val upDownDifference = abs(upDown - previousUpDown)

            // Update the variables for the next iteration
            previousSides = sides
            previousUpDown = upDown

            // Determine the status based on sensor values
            if (sidesDifference > epsilon || upDownDifference > epsilon) {
                startCounter()
                earnedPointText = counterSeconds.toString()
//                earnedPointText = "Moving"
            } else {
                pauseCounter()
                earnedPointText = counterSeconds.toString()
//                earnedPointText = "Stationary"
            }

            // Update the status text view
            findViewById<TextView>(R.id.earnedPointText).text = earnedPointText

            Log.d("STATUS_TEXT", "Status Text: $earnedPointText")
            Log.d("COUNTER_SECONDS", "Counter Seconds: $counterSeconds")

//            Log.d("SENSOR", "Acceleration: sides=$sides, upDown=$upDown")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}