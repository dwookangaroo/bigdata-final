package com.kimjjing1004.seoulapplication.main.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kimjjing1004.seoulapplication.main.home.HomeActivity

class HomeFragment : Fragment() {

    var english = ""
    var korea = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val language = arguments?.getString("LanguageKey")
        val intent = Intent (this@HomeFragment.context, HomeActivity::class.java)

        if (language == "EnglishData") {
            english = language
            korea = ""
            intent.putExtra("EnglishKey", "EnglishData")
        } else if (language == "KoreaData") {
            korea = language
            english = ""
            intent.putExtra("KoreaKey", "KoreaData")
        } else {
            Toast.makeText(this@HomeFragment.context, "there isn't transferred name", Toast.LENGTH_SHORT).show()
        }
//        startActivity(intent)
    }
}