package com.kimjjing1004.seoulapplication.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.kimjjing1004.seoulapplication.databinding.ActivityAppStartBinding
import com.kimjjing1004.seoulapplication.intro.SetLanguageActivity

class AppStartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, SetLanguageActivity::class.java)
            startActivity(intent)
            finish()
        }, 6000)
    }
}