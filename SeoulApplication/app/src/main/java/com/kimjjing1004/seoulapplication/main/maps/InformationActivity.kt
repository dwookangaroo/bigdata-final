package com.kimjjing1004.seoulapplication.main.maps

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kimjjing1004.seoulapplication.databinding.ActivityInformationBinding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class InformationActivity : AppCompatActivity() {
    private val htmlPageUrl = "http://www.naver.com"
    private var textviewHtmlDocument: TextView? = null
    private val htmlContentlnStringFormat = ""
    private lateinit var binding: ActivityInformationBinding


    var value2 = ""
    var english = ""
    var korea = ""
    var title = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        if (intent.hasExtra("EnglishKey")) {
            english = intent.getStringExtra("EnglishKey").toString()
            korea = ""
            title = intent.getStringExtra("nameKey").toString()

        } else if (intent.hasExtra("KoreaKey")) {
            korea = intent.getStringExtra("KoreaKey").toString()
            english = ""
            title = intent.getStringExtra("nameKey").toString()


        } else {
            Toast.makeText(this, "there isn't trasnfered name", Toast.LENGTH_SHORT).show()
        }
//
//        textviewHtmlDocument = findViewById<View>(R.id.textView) as TextView
//        textviewHtmlDocument!!.movementMethod = ScrollingMovementMethod()

        Thread {
            value2 = getJson("http://192.168.1.33:8000/restaurant")
            Thread.sleep(100L)
            judgeString2()


        }.start()
//

    }

    private fun judgeString2() {
        val responseJSON = JSONObject(value2)
        for (i in 0..9) {
            var message = responseJSON.getString(i.toString())
            message = message.replace("[", "")
            message = message.replace("]", "")
            val result = message.split(",")

            var name = ""

            if (korea == "KoreaData") {
                name = result[0]
                name = name.substring(1, name.length - 1)
            } else if (english == "EnglishData") {
                name = result[5]
                name = name.substring(1, name.length - 1)
            }

            if (name == title) {
                var address = ""
                var telephon = ""
                var starRate = ""
                var operationTime = ""
                var option = ""
                var realMenu = ""
                var picture = ""
                var resName = ""
                var resID = 0
                var finalMenu = ""

                if (english == "EnglishData") {
                    address = result[6]
                    address = address.replace("#", ",")
                    address = address.substring(1, address.length - 1)

                    realMenu = result[7]
                    realMenu = realMenu.replace("#", ",")
                    realMenu = realMenu.substring(1, realMenu.length - 1)
                    finalMenu = "Representative menu : $realMenu"

                    option = result[14]
                    option = option.replace("#", ",")
                    option = option.substring(1, option.length - 1)

                    operationTime = result[13]
                    operationTime = operationTime.substring(1, operationTime.length - 1)
                    operationTime = operationTime.replace("#", ",")
                    operationTime = operationTime.replace("""\n""", "\n")

                } else if (korea == "KoreaData") {
                    address = result[3]
                    address = address.substring(1, address.length - 1)

                    realMenu = result[4]
                    realMenu = realMenu.replace("#", ",")
                    realMenu = realMenu.substring(1, realMenu.length - 1)
                    finalMenu = "대표메뉴 : $realMenu"

                    option = result[10]
                    option = option.replace("#", ",")
                    option = option.substring(1, option.length - 1)

                    operationTime = result[9]
                    operationTime = operationTime.substring(1, operationTime.length - 1)
                    operationTime = operationTime.replace("#", ",")
                    operationTime = operationTime.replace("""\n""", "\n")
                }

                starRate = result[11]
                telephon = result[12]
                telephon = telephon.substring(1, telephon.length - 1)

                picture = result[8]
                picture = picture.substring(1, picture.length - 1)

                resName = "@drawable/$picture"
                resID = getResources().getIdentifier(resName, "drawable",
                    "com.kimjjing1004.seoulapplication")

                binding.root.post {
                    binding.title1.text = name
                    binding.title2.text = name
                    binding.phoneNumber.text = telephon
                    binding.address.text = address
                    binding.star.text = starRate + " / 5.0"
                    binding.service.text = option
                    binding.mainMenu.text = finalMenu
                    binding.mainPicture.setImageResource(resID)
                    binding.operation.text = operationTime
                }
                break
            }
        }
    }

    fun getJson(Url: String): String {
        val response = StringBuilder()
        val stringUrl: String = Url
        val url: URL
        try {
            url = URL(stringUrl)
            val httpconn: HttpURLConnection = url.openConnection() as HttpURLConnection
            httpconn.setRequestProperty("Accept", "application/json")
            if (httpconn.getResponseCode() === HttpURLConnection.HTTP_OK) {
                val input =
                    BufferedReader(InputStreamReader(httpconn.getInputStream()), 8192)
                var strLine: String? = null
                while (input.readLine().also { strLine = it } != null) {
                    response.append(strLine)
                }
                input.close()
            }
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        println("This is the response match API $response for url$stringUrl")
        return response.toString()
    }
}