package com.kimjjing1004.seoulapplication.main.user

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kimjjing1004.seoulapplication.databinding.ActivityUserBinding
import com.kimjjing1004.seoulapplication.main.user.YouTubeAdapter
import com.kimjjing1004.seoulapplication.main.user.YouTubeContent
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    var english = ""
    var korea = ""
    var value = ""
    var landmarkValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        if (intent.hasExtra("English")) {
            english = intent.getStringExtra("English").toString()
            korea = ""

        } else if (intent.hasExtra("Korea")) {
            korea = intent.getStringExtra("Korea").toString()
            english = ""


        } else {
            Toast.makeText(this, "there isn't transferred name", Toast.LENGTH_SHORT).show()
        }

        Thread{
            val contents: MutableList<YouTubeContent> = ArrayList()
            value = getJson( "http://192.168.1.33:8000/landmark")
            Thread.sleep(100L)
                landmarkValue = judgeString()

            if(landmarkValue=="이순신동상") {
                // 이순신동상 유튜브
                contents.add(YouTubeContent("1hnBb7vqYnU"))
                contents.add(YouTubeContent("gKySq23lNj4"))
                contents.add(YouTubeContent("dg4aOHYL75w"))
                contents.add(YouTubeContent("vMXfYvRry9o"))
                contents.add(YouTubeContent("CDJDXODMQA"))
            }
            else if(landmarkValue=="63빌딩"){
                // 63빌딩 유튜브
                contents.add(YouTubeContent("nkPvbySFhQg"))
                contents.add(YouTubeContent("UDd8PqUEZzs"))
                contents.add(YouTubeContent("f8PD0FRybDY"))
                contents.add(YouTubeContent("skCYEOK8Uhw"))
                contents.add(YouTubeContent("Y-CwM5zDIt8"))
            }
            else if(landmarkValue=="코엑스"){
                // 코엑스 유튜브
                contents.add(YouTubeContent("rysnAIxWQUY"))
                contents.add(YouTubeContent("7_O7toPoBC4"))
                contents.add(YouTubeContent("4E-yUwzmB7U"))
                contents.add(YouTubeContent("jpdx-F26rPE"))
                contents.add(YouTubeContent("oWyGbwivzCo"))
            }
            else if(landmarkValue=="경복궁"){
                // 광화문 유튜브
                contents.add(YouTubeContent("jgPEeALIoVQ"))
                contents.add(YouTubeContent("yZeNfaIK7Nw"))
                contents.add(YouTubeContent("viIhOorr11I"))
                contents.add(YouTubeContent("O15-z-rBGJ8"))
                contents.add(YouTubeContent("SyNMzM4B1qA"))
            }
            else if(landmarkValue=="서대문형무소"){
                // 형무소 유튜브
                contents.add(YouTubeContent("ML3bJIZTsqA"))
                contents.add(YouTubeContent("7rJEjANoS84"))
                contents.add(YouTubeContent("BXu_hnDvOB0"))
                contents.add(YouTubeContent("dMxWekz_26Q"))
                contents.add(YouTubeContent("JF3B1aQqFjE"))
            }

            else if(landmarkValue=="독립문"){
                // 독립문 유튜브
                contents.add(YouTubeContent("P30VZ_Sp7qM"))
                contents.add(YouTubeContent("UIHbIhe7hVc"))
                contents.add(YouTubeContent("rbJGXJPt7eA"))
                contents.add(YouTubeContent("_5Jeuj4C4JA"))
                contents.add(YouTubeContent("4QxyVhoXvaA"))

            }

            else if(landmarkValue=="롯데타워"){
                // 롯데타워 유튜브
                contents.add(YouTubeContent("f8GPs4Pmlvo"))
                contents.add(YouTubeContent("OgJJ9vYCvrM"))
                contents.add(YouTubeContent("9ybHELfifx8"))
                contents.add(YouTubeContent("hHA0gUdgpSk"))
                contents.add(YouTubeContent("XjFj4mj-z9M&list=RDCMUCvFkF8voBqKQlEyig5umyPA&start_radio=1"))
            }

            else if(landmarkValue=="명동성당"){
                // 명동성당 유튜브
                contents.add(YouTubeContent("bLY_0XoFZ0g"))
                contents.add(YouTubeContent("I0hrpOiOlAI"))
                contents.add(YouTubeContent("4U0tSDvQwfE"))
                contents.add(YouTubeContent("cTebk9toEg8"))
                contents.add(YouTubeContent("yB432zUEMpY"))
            }

            else if(landmarkValue=="남산타워"){
                // 남산타워 유튜브
                contents.add(YouTubeContent("Eir8H4C5XfA"))
                contents.add(YouTubeContent("EK0wlUAbtow"))
                contents.add(YouTubeContent("i9OFJy8Ibp8"))
                contents.add(YouTubeContent("ha203dJ5LAQ"))
                contents.add(YouTubeContent("Dqk0nZMas9A"))
            }

            else if(landmarkValue=="구서울역"){
                // 구서울역 유튜브
                contents.add(YouTubeContent("JEktkt3ZmzQ"))
                contents.add(YouTubeContent("pinFqoF2tsE"))
                contents.add(YouTubeContent("sVLZoZOf0wI"))
                contents.add(YouTubeContent("NKL4ctXfj0I"))
                contents.add(YouTubeContent("dVmlIejS2t4"))
            }

            else if(landmarkValue=="탑골공원팔각정"){
                // 탑골공원 유튜브
                contents.add(YouTubeContent("S9CXEINeIjg"))
                contents.add(YouTubeContent("xajY_hfusGo"))
                contents.add(YouTubeContent("uP_YXnDQQ-g"))
                contents.add(YouTubeContent("PV9h0Re9Hr4"))
                contents.add(YouTubeContent("VeBWg0XwS0U"))
            }

            binding.root.post {
                val recyclerView = binding.mainRecyclerView
                recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                recyclerView.itemAnimator = DefaultItemAnimator()
                recyclerView.adapter = YouTubeAdapter(contents)
            }

        }.start()

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

    private fun judgeString(): String {
        val responseJSON = JSONObject(value)
        var message = responseJSON.getString("landmark")
        message = message.replace("[", "")
        message = message.replace("]", "")
        val result = message.split(",")

        var landmarkName = result[0]
        landmarkName = landmarkName.substring(1, landmarkName.length - 1)

        return landmarkName

    }

}