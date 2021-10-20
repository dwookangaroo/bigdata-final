package com.kimjjing1004.seoulapplication.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.kimjjing1004.seoulapplication.databinding.ActivityRegisterBinding
import com.kimjjing1004.seoulapplication.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {

    val register: Register? = null
    var english = ""
    var korea = ""

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        if (intent.hasExtra("EnglishKey")) {
            english = intent.getStringExtra("EnglishKey").toString()
            korea=""
            /* "nameKey"라는 이름의 key에 저장된 값이 있다면
               textView의 내용을 "nameKey" key에서 꺼내온 값으로 바꾼다 */

        }
        else if(intent.hasExtra("KoreaKey")) {
            korea = intent.getStringExtra("KoreaKey").toString()
            english=""
            /* "nameKey"라는 이름의 key에 저장된 값이 있다면
               textView의 내용을 "nameKey" key에서 꺼내온 값으로 바꾼다 */

        }else {
            Toast.makeText(this, "there isn't transferred name", Toast.LENGTH_SHORT).show()
        }

        if(english=="EnglishData"){
            binding.btnRegister2.text = "Registration completed"
        }
        else if(korea=="KoreaData"){
            binding.btnRegister2.text = "가입완료"

        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.33:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val registerService: RegisterService = retrofit.create(RegisterService::class.java)

        // 가입완료 버튼
        // 클릭시 가입성공과 동시에 MainActivity 로 이동
        // 실패시 이동X
        binding.btnRegister2.setOnClickListener {

            // editText로부터 유저가 입력한 값들을 받아온다
            val registerId = binding.editId.text.toString()
            val registerPw = binding.editPw.text.toString()
            val registerPw2 = binding.editPw2.text.toString()

            registerService.requestRegister(registerId, registerPw, registerPw2).enqueue(object:
                Callback<Register> {
                override fun onFailure(call: Call<Register>, t: Throwable) {
                    Log.e("REGISTER", t.message.toString())


                    // 웹 통신에 실패했을때 실행되는 코드
                    if(english!="EnglishData") {
                        val dialog = AlertDialog.Builder(this@RegisterActivity)
                        dialog.setTitle("실패!")
                        dialog.setMessage("통신에 실패했습니다.")
                        dialog.show()
                    } else {
                        val dialog = AlertDialog.Builder(this@RegisterActivity)
                        dialog.setTitle("Failure!")
                        dialog.setMessage("Communication failed.")
                        dialog.show()

                    }
                }

                override fun onResponse(call: Call<Register>, response: Response<Register>) {
                    // 통신에 성공했을때 응답값을 받아옴
                    if(english!="EnglishData") {
                        val register = response.body() // msg, code
                        Log.d("REGISTER", "msg : " + register?.msg)
                        Log.d("REGISTER", "code : " + register?.code)

                        if (response.code() == 200) {
                            Toast.makeText(this@RegisterActivity, "가입성공!!", Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                        } else if (response.code() == 400) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "아이디를 입력해주세요!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (response.code() == 401) {
                            Toast.makeText(this@RegisterActivity, "아이디 중복!!!!!", Toast.LENGTH_SHORT)
                                .show()
                        } else if (response.code() == 402) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "아이디를 올바른 이메일 형식으로 써주세요!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (response.code() == 403) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "비밀번호를 입력해주세요!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (response.code() == 404) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "비밀번호를 최소 8자리 이상, 하나의 문자, 숫자/특수 문자를 넣어 주세요!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (response.code() == 405) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "비밀번호 확인을 해주세요!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (response.code() == 406) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "비밀번호가 일치하지가 않습니다!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(this@RegisterActivity, "가입실패!!!", Toast.LENGTH_SHORT)
                                .show()
                        }

                        if (response.isSuccessful) {
                            Toast.makeText(this@RegisterActivity, "가입성공!!", Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@RegisterActivity, "가입실패!!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    else{
                        val register = response.body() // msg, code
                        Log.d("REGISTER", "msg : " + register?.msg)
                        Log.d("REGISTER", "code : " + register?.code)

                        if (response.code() == 200) {
                            Toast.makeText(this@RegisterActivity, "Successfully signed up!!", Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                        } else if (response.code() == 400) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Please enter your account!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (response.code() == 401) {
                            Toast.makeText(this@RegisterActivity, "Duplicated ID!!!!!", Toast.LENGTH_SHORT)
                                .show()
                        } else if (response.code() == 402) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Please write your ID in the correct email format!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (response.code() == 403) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Please enter your password!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (response.code() == 404) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Please put at least 8 digits of the password, one letter, number/special character!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (response.code() == 405) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Please check the password!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (response.code() == 406) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "The password doesn't match!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(this@RegisterActivity, "Failed to sign up!!!", Toast.LENGTH_SHORT)
                                .show()
                        }

                        if (response.isSuccessful) {
                            Toast.makeText(this@RegisterActivity, "Successfully signed up!!", Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@RegisterActivity, "Failed to sign up!!", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
                }
            })
        }
    }
}