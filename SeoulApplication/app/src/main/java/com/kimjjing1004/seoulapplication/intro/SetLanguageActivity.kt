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
        // activity binding을 쓰면 화면 표시 뿐만 아니라 XML ID값 불러오기가 쉽다.
        super.onCreate(savedInstanceState)
        binding = ActivitySetLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 위에 있는 패키지 이름을 숨겨주는 것
        supportActionBar?.hide()

        // 버튼 클릭할 때 쓰는 메소드
        binding.btnKorea.setOnClickListener {
            val koreaIntent = Intent(this@SetLanguageActivity, LoginActivity::class.java)
            koreaIntent.putExtra("KoreaKey", "KoreaData")
            startActivity(koreaIntent)
        }

        binding.btnEnglish.setOnClickListener {
            val englishIntent = Intent(this@SetLanguageActivity, LoginActivity::class.java)
            englishIntent.putExtra("EnglishKey", "EnglishData")
            startActivity(englishIntent)
            // intetnt 조건을 줘서 다음 Acitivty로 넘어갈 때 사용하는 코드
            // 수신 Activity에서 반드시 hasExtra를 써야만 에러가 나지 않는다.
        }
    }
}