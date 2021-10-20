package com.kimjjing1004.seoulapplication.main.camera

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kimjjing1004.seoulapplication.databinding.ActivityCameraBinding
import com.kimjjing1004.seoulapplication.databinding.ActivityLoadingBinding
import com.kimjjing1004.seoulapplication.intro.SetLanguageActivity
import com.kimjjing1004.seoulapplication.main.maps.MapsActivity

class LoadingActivity : AppCompatActivity(){

    private lateinit var binding: ActivityLoadingBinding

    var english=""
    var korea=""
    lateinit var pictureValue : ByteArray
    var rotationValue = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        if (intent.hasExtra("EnglishKey") && intent.hasExtra("PictureKey") && intent.hasExtra("RotationKey")) {
            english = intent.getStringExtra("EnglishKey").toString()
            korea = ""
            pictureValue = intent.getByteArrayExtra("PictureKey")!!
            binding.loadDB.text = "Recognizing the image"
            rotationValue = intent.getStringExtra("RotationKey").toString()

        } else if (intent.hasExtra("KoreaKey") && intent.hasExtra("PictureKey") && intent.hasExtra("RotationKey")) {
            korea = intent.getStringExtra("KoreaKey").toString()
            english = ""
            pictureValue = intent.getByteArrayExtra("PictureKey")!!
            binding.loadDB.text = "이미지 인식중"
            rotationValue = intent.getStringExtra("RotationKey").toString()
        } else {
            Toast.makeText(this, "there isn't transferred name", Toast.LENGTH_SHORT).show()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            if(english=="EnglishData"){
                val intent = Intent(this, CameraActivity::class.java)
                intent.putExtra("English", "EnglishData")
                intent.putExtra("Camera","CameraData")
                intent.putExtra("Picture",pictureValue)
                intent.putExtra("Rotation",rotationValue)
                startActivity(intent)
                finish()

            }
            else if(korea=="KoreaData"){
                val intent = Intent(this, CameraActivity::class.java)
                intent.putExtra("Korea", "KoreaData")
                intent.putExtra("Camera","CameraData")
                intent.putExtra("Picture",pictureValue)
                intent.putExtra("Rotation",rotationValue)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this, "application error!!", Toast.LENGTH_SHORT).show()
            }
        }, 3000)



    }

}