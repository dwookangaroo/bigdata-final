package com.kimjjing1004.seoulapplication.main.maps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.*
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.kimjjing1004.seoulapplication.R
import com.kimjjing1004.seoulapplication.databinding.ActivityMapsBinding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Thread.sleep
import java.net.HttpURLConnection
import java.net.URL

class MapsActivity : FragmentActivity(), OnMyLocationButtonClickListener, OnMapClickListener,
    OnMapReadyCallback, OnMyLocationClickListener, OnInfoWindowClickListener, OnMarkerClickListener {
    private var mMap: GoogleMap? = null
    private var binding: ActivityMapsBinding? = null
    private val MY_LOCATION_REQUEST_CODE = 1

    var value1 = ""
    var value2 = ""
    var value3 = ""
    var value4 = ""
    var english = ""
    var korea = ""
    var hashMap = mutableMapOf("one" to "what")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            binding = ActivityMapsBinding.inflate(layoutInflater)
            setContentView(binding!!.root)

            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment!!.getMapAsync(this)

            if (intent.hasExtra("EnglishKey")) {
                english = intent.getStringExtra("EnglishKey").toString()
                korea = ""
                println("아놔")

            } else if (intent.hasExtra("KoreaKey")) {
                korea = intent.getStringExtra("KoreaKey").toString()
                english = ""
                println("아놔")


            } else {
                Toast.makeText(this, "there isn't transferred name", Toast.LENGTH_SHORT).show()
            }

            Thread {
                value1 = getJson("http://192.168.1.33:8000/hotel")
                value2 = getJson("http://192.168.1.33:8000/restaurant")
//                value3 = getJson("http://192.168.1.33:8000/seoul_landmarks")
                value4 = getJson("http://192.168.1.33:8000/landmark")
                Thread.sleep(100L)

                binding!!.root.post {
                    judgeString1()
                    judgeString2()
//                    judgeString3()
                    judgeString4()
                }
            }.start()
        }

    private fun judgeString1() {
        val responseJSON = JSONObject(value1)
        for (i in 0..4) {
            var message = responseJSON.getString(i.toString())
            message = message.replace("[", "")
            message = message.replace("]", "")
            val result = message.split(",")

            var name = ""

            if (english == "EnglishData") {
                name = result[5]
                name = name.substring(1, name.length - 1)

            } else if (korea == "KoreaData") {
                name = result[0]
                name = name.substring(1, name.length - 1)

            }
            var lat = result[1].toDouble()
            var lng = result[2].toDouble()

            val baseTour = LatLng(lat, lng)
            mMap!!.addMarker(
                MarkerOptions().position(baseTour).title(name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )
            hashMap[name] = "hotel"


        }
    }

    private fun judgeString2() {
        val responseJSON = JSONObject(value2)
        for (i in 0..9) {
            var message = responseJSON.getString(i.toString())
            message = message.replace("[", "")
            message = message.replace("]", "")
            val result = message.split(",")

            var name = ""

            if (english == "EnglishData") {
                name = result[5]
                name = name.substring(1, name.length - 1)

            } else if (korea == "KoreaData") {
                name = result[0]
                name = name.substring(1, name.length - 1)

            }
            var lat = result[1].toDouble()
            var lng = result[2].toDouble()

            val baseTour = LatLng(lat, lng)
            mMap!!.addMarker(
                MarkerOptions().position(baseTour).title(name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            )
            hashMap[name] = "matzip"
        }
    }

//    private fun judgeString3() {
//        val responseJSON = JSONObject(value3)
//        for (i in 0..10) {
//            var message = responseJSON.getString(i.toString())
//            message = message.replace("[", "")
//            message = message.replace("]", "")
//            val result = message.split(",")
//
//            var landmarkName = ""
//
//            if (english == "EnglishData") {
//                landmarkName = result[3]
//                landmarkName = landmarkName.substring(1, landmarkName.length - 1)
//
//            } else if (korea == "KoreaData") {
//                landmarkName = result[0]
//                landmarkName = landmarkName.substring(1, landmarkName.length - 1)
//
//            }
//
//            var lat = result[1].toDouble()
//            var lng = result[2].toDouble()
//
//
//            var baseTour = LatLng(lat, lng)
//
//            mMap!!.addMarker(
//                MarkerOptions().position(baseTour).title(landmarkName)
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//            )
//            hashMap[landmarkName] = "landmark"
//
//
//        }
//    }

    private fun judgeString4() {
        val responseJSON = JSONObject(value4)
        var message = responseJSON.getString("landmark")
        message = message.replace("[", "")
        message = message.replace("]", "")
        val result = message.split(",")

        var landmarkName = ""

        if (english == "EnglishData") {
            landmarkName = result[3]
            landmarkName = landmarkName.substring(1, landmarkName.length - 1)

        } else if (korea == "KoreaData") {
            landmarkName = result[0]
            landmarkName = landmarkName.substring(1, landmarkName.length - 1)

        }

        var lat = result[1].toDouble()
        var lng = result[2].toDouble()


        var baseTour = LatLng(lat, lng)

        mMap!!.addMarker(
            MarkerOptions().position(baseTour).title(landmarkName)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        )
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(baseTour, 15f))
        hashMap[landmarkName] = "landmark"


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
        println("This si the response march API $response for url$stringUrl")
        return response.toString()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1
            )
        }

        mMap!!.isMyLocationEnabled = true
        mMap!!.setOnMyLocationButtonClickListener(this)
        mMap!!.setOnMyLocationClickListener(this)
        mMap!!.setOnMarkerClickListener(this)


//        for(int i=1; i<resultArray.length; i++){
//            String[] newArray = resultArray[i].split(",");
//            float lat = Float.parseFloat(newArray[1]);
//            float lng = Float.parseFloat(newArray[2]);
//            LatLng tour = new LatLng(lat, lng);
//            mMap.addMarker(new MarkerOptions().position(tour).title(newArray[0]));
//            System.out.println(Arrays.toString(newArray));
//        }

        // 마커 표시(예제)
//        val baseTour = LatLng(37.579417, 126.977341)
//        mMap!!.addMarker(MarkerOptions().position(baseTour).title("불국사"))
//        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(baseTour, 12f))


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.size == 1 && permissions[0] === Manifest.permission.ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this, arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ), 1
                    )
                }
                mMap!!.isMyLocationEnabled = true
            }
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(this, "Current Location:\n$location", Toast.LENGTH_LONG).show()
    }

    override fun onMapClick(latLng: LatLng) {

    }

    companion object {
        fun addToPoint(context: Context?): Location {
            val location = Location("")
            val geocoder = Geocoder(context)
            var addresses: List<Address>? = null
            try {
                addresses = geocoder.getFromLocationName("포천시청", 3)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (addresses != null) {
                for (i in addresses.indices) {
                    val lating = addresses[i]
                    location.latitude = lating.latitude
                    location.longitude = lating.longitude
                }
            }
            return location
        }
    }

    override fun onInfoWindowClick(p0: Marker) {
        TODO("Not yet implemented")

    }

    override fun onMarkerClick(p0: Marker): Boolean {
        if (korea == "KoreaData") {
            if (hashMap[p0.title] == "matzip") {
                sleep(500L)
                val koreaIntent = Intent(this, InformationActivity::class.java)
                koreaIntent.putExtra("KoreaKey", "KoreaData")
                koreaIntent.putExtra("nameKey", p0.title)
                startActivity(koreaIntent)
            } else if (hashMap[p0.title] == "hotel") {
                sleep(500L)
                val koreaIntent = Intent(this, AnotherInformationActivity::class.java)
                koreaIntent.putExtra("KoreaKey", "KoreaData")
                koreaIntent.putExtra("nameKey", p0.title)
                startActivity(koreaIntent)
            } else if (hashMap[p0.title] == "landmark") {

            }

        } else if (english == "EnglishData") {
            if (hashMap[p0.title] == "matzip") {
                sleep(500L)
                val koreaIntent = Intent(this, InformationActivity::class.java)
                koreaIntent.putExtra("EnglishKey", "EnglishData")
                koreaIntent.putExtra("nameKey", p0.title)

                startActivity(koreaIntent)
            } else if (hashMap[p0.title] == "hotel") {
                sleep(500L)
                val koreaIntent = Intent(this, AnotherInformationActivity::class.java)
                koreaIntent.putExtra("EnglishKey", "EnglishData")
                koreaIntent.putExtra("nameKey", p0.title)
                startActivity(koreaIntent)
            } else if (hashMap[p0.title] == "landmark") {
            }

        }
        return false
    }
}



