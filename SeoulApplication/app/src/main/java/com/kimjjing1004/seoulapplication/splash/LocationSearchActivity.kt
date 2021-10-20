package com.kimjjing1004.seoulapplication.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.kimjjing1004.seoulapplication.databinding.ActivityLocationSearchBinding
import com.kimjjing1004.seoulapplication.main.maps.MapsActivity

class LocationSearchActivity : AppCompatActivity() {

    var english = ""
    var korea = ""
    var title = ""

    private lateinit var binding: ActivityLocationSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        if (intent.hasExtra("EnglishKey")) {
            english = intent.getStringExtra("EnglishKey").toString()
            korea = ""

        } else if (intent.hasExtra("KoreaKey")) {
            korea = intent.getStringExtra("KoreaKey").toString()
            english = ""
        } else {
            Toast.makeText(this, "there isn't transferred name", Toast.LENGTH_SHORT).show()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            if(english=="EnglishData"){
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra("EnglishKey", "EnglishData")
                startActivity(intent)
                finish()

            }
            else if(korea=="KoreaData"){
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra("KoreaKey", "KoreaData")
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this, "application error!!", Toast.LENGTH_SHORT).show()
            }
            println("sibal")
        }, 3000)
    }
}