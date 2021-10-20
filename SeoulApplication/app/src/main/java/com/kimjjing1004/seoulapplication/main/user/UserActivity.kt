package com.kimjjing1004.seoulapplication.main.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kimjjing1004.seoulapplication.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
    }
}