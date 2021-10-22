package com.kimjjing1004.seoulapplication.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
//import com.facebook.AccessToken
//import com.facebook.FacebookCallback
//import com.facebook.FacebookException
//import com.facebook.login.LoginManager
//import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.kimjjing1004.seoulapplication.R
import com.kimjjing1004.seoulapplication.databinding.ActivityLoginBinding
import com.kimjjing1004.seoulapplication.main.ui.MainActivity
import com.kimjjing1004.seoulapplication.register.RegisterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class LoginActivity : AppCompatActivity() {

    val login: Login? = null

    private lateinit var binding: ActivityLoginBinding

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    var english = ""
    var korea = ""

    // constants
    private companion object {
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    // 구글로그인 버튼 텍스트 변환
    private fun setGoogleButtonText(loginButton: SignInButton, buttonText: String) {
        var i = 0
        while (i < loginButton.childCount) {
            val v = loginButton.getChildAt(i)
            if (v is TextView) {
                val tv = v
                tv.text = buttonText
                tv.gravity = Gravity.CENTER
                return
            }
            i++
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 구글로그인 버튼 텍스트 변환
        setGoogleButtonText(binding.googleSignInBtn, "구글 계정으로 로그인")

        // 상단 액션바 숨김
        supportActionBar?.hide()

        // 한국어, 영어 변환
        if (intent.hasExtra("EnglishKey")) {
            english = intent.getStringExtra("EnglishKey").toString()
            korea=""
            /* "nameKey"라는 이름의 key에 저장된 값이 있다면
               textView의 내용을 "nameKey" key에서 꺼내온 값으로 바꾼다 */

        } else if (intent.hasExtra("KoreaKey")) {
            korea = intent.getStringExtra("KoreaKey").toString()
            english=""
            /* "nameKey"라는 이름의 key에 저장된 값이 있다면
               textView의 내용을 "nameKey" key에서 꺼내온 값으로 바꾼다 */

        } else {
            Toast.makeText(this, "there isn't transferred name", Toast.LENGTH_SHORT).show()
        }

        if (english=="EnglishData") {
            binding.btnLogin.text = "Login"
            binding.btnRegister.text = "Join"
//            setGoogleButtonText(binding.googleSignInBtn, "Sign in with Google")
        } else if (korea=="KoreaData") {
            binding.btnLogin.text = "로그인"
            binding.btnRegister.text = "회원가입"
//            setGoogleButtonText(binding.googleSignInBtn, "구글 계정으로 로그인")
        }

        // (firebase)구글 로그인 구성
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail() // we only need email from google account
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //        binding.facebookSignInBtn.setReadPermissions("email")
//        binding.facebookSignInBtn.setOnClickListener {
//            // begin Facebook SignIn
//            btnchocie = "Facebook"
//            facebookLogin()
//            startActivity(Intent(this@LoginActivity, FirebaseActivity::class.java))
//            finish()
//        }

        checkUser()
        // Google SignIn Button, Click to begin SignIn
        binding.googleSignInBtn.setOnClickListener {
            // begin Google SignIn
            Log.d(TAG, "onCreate: begin Google SignIn")
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.33:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val loginService: LoginService = retrofit.create(LoginService::class.java)

        // 로그인 버튼
        binding.btnLogin.setOnClickListener {

            // editText로부터 입력된 값을 받아온다
            val loginId = binding.editId.text.toString()
            val loginPw = binding.editPw.text.toString()

            loginService.requestLogin(loginId, loginPw).enqueue(object: Callback<Login> {
                override fun onFailure(call: Call<Login>, t: Throwable) {


                    // 웹 통신에 실패했을때 실행되는 코드
                    if (english=="EnglishData") {
                        Log.e("LOGIN", t.message.toString())
                        val dialog = AlertDialog.Builder(this@LoginActivity)
                        dialog.setTitle("Failure!")
                        dialog.setMessage("Communication failed.")
                        dialog.show()
                    } else {
                        Log.e("LOGIN", t.message.toString())
                        val dialog = AlertDialog.Builder(this@LoginActivity)
                        dialog.setTitle("실패!")
                        dialog.setMessage("통신에 실패했습니다.")
                        dialog.show()
                    }
                }

                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    // 통신에 성공했을때 응답값을 받아옴
                    if (english!="EnglishData") {
                        val login = response.body() // msg, code
                        Log.d("LOGIN", "msg : " + login?.msg)
                        Log.d("LOGIN", "code : " + login?.code)

                        if (response.code()==200) {
                            Toast.makeText(this@LoginActivity, "로그인 성공!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("KoreaKey", "KoreaData")
                            startActivity(intent)
                        } else if (response.code()==400) {
                            Toast.makeText(this@LoginActivity, "아이디를 입력해주세요!", Toast.LENGTH_SHORT).show()
                        } else if (response.code()==402) {
                            Toast.makeText(this@LoginActivity, "아이디 또는 비밀번호가 일치하는 정보가 없습니다!", Toast.LENGTH_SHORT).show()
                        } else if (response.code()==403) {
                            Toast.makeText(this@LoginActivity, "비밀번호를 입력해주세요!", Toast.LENGTH_SHORT).show()
                        } else if (response.code()==404) {
                            Toast.makeText(this@LoginActivity, "아이디 또는 비밀번호가 일치하는 정보가 없습니다!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@LoginActivity, "로그인 실패!!!", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        val login = response.body() // msg, code
                        Log.d("LOGIN", "msg : " + login?.msg)
                        Log.d("LOGIN", "code : " + login?.code)

                        if (response.code()==200) {
                            Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("EnglishKey", "EnglishData")
                            startActivity(intent)
                        } else if (response.code()==400) {
                            Toast.makeText(this@LoginActivity, "Please enter your account!", Toast.LENGTH_SHORT).show()
                        } else if (response.code()==402) {
                            Toast.makeText(this@LoginActivity, "There's no information that matches the ID or password!", Toast.LENGTH_SHORT).show()
                        } else if (response.code()==403) {
                            Toast.makeText(this@LoginActivity, "Please enter your password!", Toast.LENGTH_SHORT).show()
                        } else if (response.code()==404) {
                            Toast.makeText(this@LoginActivity, "There's no information that matches the ID or password!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@LoginActivity, "Login failed!!!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }

        // 회원가입 버튼
        // 클릭시 RegisterActivity 로 이동
        binding.btnRegister.setOnClickListener {
            if (korea=="KoreaData") {
                val intent = Intent(this, RegisterActivity::class.java)
                intent.putExtra("KoreaKey", "KoreaData")
                startActivity(intent)
            } else if (english=="EnglishData") {
                val intent = Intent(this, RegisterActivity::class.java)
                intent.putExtra("EnglishKey", "EnglishData")
                startActivity(intent)
            } else {
                Toast.makeText(this, "application error!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    private fun facebookLogin() {
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"))
//        LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("public_profile"))
//        LoginManager.getInstance().registerCallback(callbackManager, object :
//            FacebookCallback<LoginResult> {
//            override fun onSuccess(result: LoginResult?) {
//                // 로그인 성공시 파이어베이스로 로그인 데이터를 넘겨줌
//                handleFacebookAccessToken(result?.accessToken)
//            }
//
//            override fun onCancel() {
//                TODO("Not yet implemented")
//            }
//
//            override fun onError(error: FacebookException?) {
//                TODO("Not yet implemented")
//            }
//        })
//    }
//
//    private fun handleFacebookAccessToken(token: AccessToken?) {
//        val credential = FacebookAuthProvider.getCredential(token?.token!!)
//        firebaseAuth.signInWithCredential(credential)
//            .addOnCompleteListener {
//                    task ->
//                if (task.isSuccessful) {
//                    // 아이디, 비밀번호 맞을 때
//                    moveMainPage(task.result?.user)
//                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
//                } else {
//                    // 틀렸을 때
//                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
//
//    // 유저정보 넘겨주고 메인 액티비티 호출
//    private fun moveMainPage(user: FirebaseUser?) {
//        if (user != null) {
//            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
//            Handler(Looper.getMainLooper()).postDelayed({
//                val intent = Intent(this@LoginActivity, FirebaseActivity::class.java)
//                startActivity(intent)
//                finish()
//            }, 3000)
//        }
//    }


    // (firebase)구글 로그인 구성2
    private fun checkUser() {
        // check if user is logged in or not
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            // user is already logged in
            // start profile activity
            if(korea=="KoreaData"){
                val intent = Intent(this@LoginActivity, FirebaseActivity::class.java)
                intent.putExtra("KoreaKey", "KoreaData")
                startActivity(intent)
            }
            else if(english=="EnglishData") {
                val intent = Intent(this@LoginActivity, FirebaseActivity::class.java)
                intent.putExtra("EnglishKey", "EnglishData")
                startActivity(intent)
            }

            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Internet from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult: Google SignIn intent result")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google SignIn success, now auth with firebase
                val account = accountTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(account)
            } catch (e: Exception) {
                // failed Google SignIn
                Log.d(TAG, "onActivityResult: ${e.message}")
            }
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account")

        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                // login success
                Log.d(TAG, "firebaseAuthWithGoogleAccount: LoggedIn")

                // get loggedIn user
                val firebaseUser = firebaseAuth.currentUser
                // get user info
                val uid = firebaseUser!!.uid
                val email = firebaseUser.email

                Log.d(TAG, "firebaseAuthWithGoogleAccount: Uid: $uid")
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Email: $email")

                // check if user is new or existing
                if (authResult.additionalUserInfo!!.isNewUser) {
                    // user is new - Account created
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Account created... \n$email")
                    Toast.makeText(this@LoginActivity, "Account created... \n$email", Toast.LENGTH_SHORT).show()
                } else {
                    // existing user = LoggedIn
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Existing user... \n$email")
                    Toast.makeText(this@LoginActivity, "LoggedIn... \n$email", Toast.LENGTH_SHORT).show()
                }

                // start profile activity
                startActivity(Intent(this@LoginActivity, FirebaseActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                // login failed
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Loggin Failed due to ${e.message}")
                Toast.makeText(this@LoginActivity, "Loggin Failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
} // place google-service.json file in app folder}