package com.health.beefit.activities

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.health.beefit.R

class WorkoutActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManger: SensorManager
    private var sensorStarted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_workout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
            //finish the current workout activity and return the HomeFragment
            finish()
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
            Log.d("SENSOR", "Acceleration: sides=$sides, upDown=$upDown")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}