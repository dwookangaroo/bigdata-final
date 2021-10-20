package com.kimjjing1004.seoulapplication.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kimjjing1004.seoulapplication.databinding.ActivitySetLanguageBinding
import com.kimjjing1004.seoulapplication.login.LoginActivity
import com.kimjjing1004.seoulapplication.main.camera.LoadingActivity

class SetLanguageActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetLanguageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnKorea.setOnClickListener {
            val koreaIntent = Intent(this@SetLanguageActivity, LoginActivity::class.java)
            koreaIntent.putExtra("KoreaKey", "KoreaData")
            startActivity(koreaIntent)
        }

        binding.btnEnglish.setOnClickListener {
            val englishIntent = Intent(this@SetLanguageActivity, LoginActivity::class.java)
            englishIntent.putExtra("EnglishKey", "EnglishData")
            startActivity(englishIntent)
        }
    }
}