package com.kimjjing1004.seoulapplication.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.kimjjing1004.seoulapplication.databinding.ActivityFirebaseBinding
import com.kimjjing1004.seoulapplication.main.ui.MainActivity

class FirebaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirebaseBinding
    private lateinit var firebaseAuth: FirebaseAuth

    var english = ""
    var korea = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirebaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        if (intent.hasExtra("EnglishKey")) {
            english = intent.getStringExtra("EnglishKey").toString()
            korea=""
        } else if (intent.hasExtra("KoreaKey")) {
            korea = intent.getStringExtra("KoreaKey").toString()
            english=""
        }
//        else {
//            Toast.makeText(this, "there isn't transferred name", Toast.LENGTH_SHORT).show()
//        }

        if (english=="EnglishData") {
            binding.textView.text = "You're Logged in as"
            binding.logoutBtn.text = "Logout"
        } else if (korea=="KoreaData") {
            binding.textView.text = "로그인 중"
            binding.logoutBtn.text = "로그아웃"
        }

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        // handle click, logout user
        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }
    }

    private fun checkUser() {
        // get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            // user not logged in
            if (english=="EnglishData") {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("EnglishKey", "EnglishData")
                startActivity(intent)
            }
            else if(korea=="KoreaData") {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("KoreaKey", "KoreaData")
                startActivity(intent)
            }

            finish()
        } else {
            // user logged in
            // get user info
            val myEmail = firebaseUser.email

            // set email
            binding.emailTv.text = myEmail
        }

        Handler(Looper.getMainLooper()).postDelayed({
            if (english=="EnglishData") {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("EnglishKey", "EnglishData")
                startActivity(intent)
            }
            else if(korea=="KoreaData") {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("KoreaKey", "KoreaData")
                startActivity(intent)
            }
            finish()
        }, 3000)
    }


}