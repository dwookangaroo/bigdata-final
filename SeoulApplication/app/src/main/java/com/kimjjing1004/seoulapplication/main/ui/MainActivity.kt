package com.kimjjing1004.seoulapplication.main.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.kimjjing1004.seoulapplication.R
import com.kimjjing1004.seoulapplication.databinding.ActivityMainBinding
import com.kimjjing1004.seoulapplication.util.setOnDebounceClickListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val list : List<Menus> by lazy {
        listOf(
            Menus(binding.clMainHome, binding.imgMainHome, HomeFragment()),
            Menus(binding.clMainSearch, binding.imgMainSearch, CameraFragment()),
            Menus(binding.clMainMap, binding.imgMainMap, MapsFragment()),
            Menus(binding.clMainMyPage, binding.imgMainMyPage, UserFragment())
        )
    }

    var english = ""
    var korea = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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

        setResult(Activity.RESULT_OK)
        initSetting()
    }

    private fun initSetting() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_main, HomeFragment())
            .commitAllowingStateLoss()

        binding.imgMainHome.isSelected = true

        list.forEachIndexed { index, menus ->
            menus.layout.setOnDebounceClickListener {
                changeSelected(index)
            }
        }
    }

    //selected
    private fun changeSelected(tabNum: Int) {
        val list = listOf(
            Menus(binding.clMainHome, binding.imgMainHome, HomeFragment()),
            Menus(binding.clMainSearch, binding.imgMainSearch, CameraFragment()),
            Menus(binding.clMainMap, binding.imgMainMap, MapsFragment()),
            Menus(binding.clMainMyPage, binding.imgMainMyPage, UserFragment())
        )

        list.forEachIndexed { index, menus ->
            menus.img.isSelected = index == tabNum
        }

        var fragment = list[tabNum].fragment
        var bundle = Bundle()

        if (english == "EnglishData") {
            bundle.putString("LanguageKey","EnglishData")
        } else if (korea == "KoreaData") {
            bundle.putString("LanguageKey","KoreaData")
        }
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_main, list[tabNum].fragment)
            .commitAllowingStateLoss()
        checkFragment = false
    }

    data class Menus(
        val layout: ConstraintLayout,
        val img: ImageView,
        val fragment: Fragment
    )

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }
    companion object{
        private var backKeyPressedTime : Long = 0
        private var checkFragment : Boolean = false
    }
}