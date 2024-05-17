package com.health.beefit

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private lateinit var splashImg: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById<View>(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        splashImg = findViewById(R.id.splash)
        splashImg.alpha = 0f
        splashImg.animate().alpha(1f).duration = 4000

        handler = Handler()
        handler.postDelayed({
            val dsp = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(dsp)
            finish()
        }, 4000)
    }
}
