package com.kimjjing1004.seoulapplication.main.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kimjjing1004.seoulapplication.R
import com.kimjjing1004.seoulapplication.databinding.ActivityHomeBinding


private lateinit var binding: ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    lateinit var imageAdapter: ImageAdapter
    private val imageList = mutableListOf<Image>()
    var english=""
    var korea=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
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

        imageAdapter = ImageAdapter(this)
        binding.imagesRecycler.adapter = imageAdapter

        imageList.apply {
            add(Image("63building", R.drawable.building63))
            add(Image("Coex", R.drawable.coex))
            add(Image("Gyeongbokgung", R.drawable.gyeongbokgungpalace))
            add(Image("Independence Door", R.drawable.independentdoor))
            add(Image("Myeongdong Church", R.drawable.myeongdongcathedral))
            add(Image("Namsan Tower", R.drawable.namsantower))
            add(Image("Seodaemoon", R.drawable.seodaemunprison))
            add(Image("General Lee", R.drawable.statue1))
            add(Image("Lotte Tower", R.drawable.tower))
            add(Image("Seoul Station", R.drawable.seoulstation))
            add(Image("Tapgol", R.drawable.tapgolpark))

//            Image("63building", R.drawable.building63),
//            Image("Conex", R.drawable.coex),
//            Image("Gyeongbokgung", R.drawable.gyeongbokgungpalace),
//            Image("Independence Door", R.drawable.independentdoor),
//            Image("Myeongdong Church", R.drawable.myeongdongcathedral),
//            Image("Namsan Tower", R.drawable.namsantower),
//            Image("Seodaemoon", R.drawable.seodaemunprison),
//            Image("General Lee", R.drawable.statue1),
//            Image("Tapgol Park", R.drawable.tapgolpark),
//            Image("Lotte Tower", R.drawable.tower),
//            Image("Seoul Station", R.drawable.seoulstation)

//        val recyclerView = findViewById<RecyclerView>(R.id.imagesRecycler)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.setHasFixedSize(true)
//        recyclerView.adapter = ImageAdapter(this, images as ArrayList<Image>)

            imageAdapter.images = imageList
            imageAdapter.notifyDataSetChanged()

        }

    }
}