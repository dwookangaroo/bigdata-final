package com.kimjjing1004.seoulapplication.main.maps

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kimjjing1004.seoulapplication.databinding.ActivityAnotherinformationBinding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class AnotherInformationActivity : AppCompatActivity() {
    private val htmlPageUrl = "http://www.naver.com"
    private var textviewHtmlDocument: TextView? = null
    private val htmlContentlnStringFormat = ""
    private lateinit var binding: ActivityAnotherinformationBinding

    var value1 = ""
    var english = ""
    var korea = ""
    var title = ""
    var cnt = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnotherinformationBinding.inflate(layoutInflater)
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
            Toast.makeText(this, "there isn't transferred name", Toast.LENGTH_SHORT).show()
        }
//
//        textviewHtmlDocument = findViewById<View>(R.id.textView) as TextView
//        textviewHtmlDocument!!.movementMethod = ScrollingMovementMethod()

        Thread {
            value1 = getJson("http://192.168.1.33:8000/hotel")
            Thread.sleep(100L)
            judgeString1()


        }.start()
//

    }

    private fun judgeString1() {
        val responseJSON = JSONObject(value1)
        for (i in 0..4) {
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
                var picture = ""
                var hotelRate = ""
                var resName = ""
                var resID = 0

                if (english == "EnglishData") {
                    address = result[6]
                    address = address.replace("#", ",")
                    address = address.substring(1, address.length - 1)

                    hotelRate = result[7]
                    hotelRate = hotelRate.substring(1, hotelRate.length - 1)


                } else if (korea == "KoreaData") {
                    address = result[3]
                    address = address.substring(1, address.length - 1)

                    hotelRate = result[4]
                    hotelRate = hotelRate.substring(1, hotelRate.length - 1)


                }

                starRate = result[9]



                telephon = result[10]
                telephon = telephon.substring(1, telephon.length - 1)
                telephon = telephon.replace("연락처: ","")

                picture = result[8]
                picture = picture.substring(1, picture.length - 1)

                resName = "@drawable/$picture"


                resID = getResources().getIdentifier(
                    resName, "drawable",
                    "com.kimjjing1004.seoulapplication"
                )


                binding.root.post {
                    binding.hotelTitle1.text = name
                    binding.hotelTitle2.text = name
                    binding.hotelPhone.text = telephon
                    binding.hotelAddress.text = address
                    binding.hotelStar.text = starRate + " / 5.0"
                    binding.hotelRating.text = hotelRate
                    binding.hotelPicture.setImageResource(resID)

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
                httpconn.setRequestProperty("Accept", "application/json");
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