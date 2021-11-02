package com.kimjjing1004.seoulapplication.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.kimjjing1004.seoulapplication.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()


        // 화면에 표시해주는 스플래시 기능이며 필요한 파일은 json은 형태로 Lottie에서 받아오면 된다.
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, SetLanguageActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}