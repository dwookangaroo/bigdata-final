package com.kimjjing1004.seoulapplication.main.camera

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.kimjjing1004.seoulapplication.databinding.ActivityCameraBinding
import com.kimjjing1004.seoulapplication.intro.SetLanguageActivity
import com.kimjjing1004.seoulapplication.main.maps.MapsActivity
import com.kimjjing1004.seoulapplication.splash.LocationSearchActivity
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var imageView: ImageView? = null
    private var selectedImage: Uri? = null
    val REQUEST_IMAGE_CAPTURE = 1 // 카메라 사진 촬영 요청코드
    private lateinit var curPhotoPath: String // 문자열 형태의 사진 경로 값(초기 값을 null로 시작하고 싶을 때)
    private val Gallery = 1
    var value = ""
    var english = ""
    var korea = ""
    var btnchocie = ""
    var landmarkValue=""
    var camera = ""
    var result = "shit"
    lateinit var pictureValue : ByteArray
    lateinit var pictureImage : ByteArray
    var rotateValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()



        if (intent.hasExtra("English") && intent.hasExtra("Camera")
            && intent.hasExtra("Picture") && intent.hasExtra("Rotation")) {
            english = intent.getStringExtra("English").toString()
            korea = ""
            camera = intent.getStringExtra("Camera").toString()
            pictureImage = intent.getByteArrayExtra("Picture")!!
            var imageBitmap = BitmapFactory.decodeByteArray(pictureImage, 0, pictureImage.size)

            rotateValue = intent.getStringExtra("Rotation").toString()
            if(rotateValue=="Rotate1"){
                imageBitmap = rotateImage(imageBitmap, 90F)
                binding.GImageView.setImageBitmap(imageBitmap)
            }
            else if(rotateValue=="Rotate2"){
                binding.GImageView.setImageBitmap(imageBitmap)
            }

            binding.moveOnMap.text = "Move on Map"

        } else if (intent.hasExtra("Korea") && intent.hasExtra("Camera")
            && intent.hasExtra("Picture") && intent.hasExtra("Rotation")) {
            korea = intent.getStringExtra("Korea").toString()
            english = ""
            camera = intent.getStringExtra("Camera").toString()
            pictureImage = intent.getByteArrayExtra("Picture")!!
            var imageBitmap = BitmapFactory.decodeByteArray(pictureImage, 0, pictureImage.size)

            rotateValue = intent.getStringExtra("Rotation").toString()
            if(rotateValue=="Rotate1"){
                imageBitmap = rotateImage(imageBitmap, 90F)
                binding.GImageView.setImageBitmap(imageBitmap)
            }
            else if(rotateValue=="Rotate2"){
                binding.GImageView.setImageBitmap(imageBitmap)
            }
            binding.moveOnMap.text = "지도로이동"

        }
        else if (intent.hasExtra("EnglishKey")) {
            english = intent.getStringExtra("EnglishKey").toString()
            korea = ""
        }
        else if (intent.hasExtra("KoreaKey")) {
            korea = intent.getStringExtra("KoreaKey").toString()
            english = ""
        }


        else {
            Toast.makeText(this, "there isn't transferred name", Toast.LENGTH_SHORT).show()
        }




        if(camera=="CameraData"){
            Thread {
                landmarkValue = getJson("http://192.168.1.33:8000/img_processing")
                value = getJson("http://192.168.1.33:8000/landmark")
                judgeString()

            }.start()

        }
        else{
            if (english == "EnglishData") {
                binding.btnCamera.text = "Photo shoot"
                binding.imgBtn.text = "Select image"
                binding.moveOnMap.text = "Move on Map"
                binding.photoResult.text = "Take a picture or upload it"
                binding.TextMultiLine.text = ""
            } else if (korea == "KoreaData") {
                binding.btnCamera.text = "카메라촬영"
                binding.imgBtn.text = "이미지선택"
                binding.moveOnMap.text = "지도로이동"
                binding.photoResult.text = "사진을 촬영하거나 업로드하세요"
                binding.TextMultiLine.text = ""
            }
            binding.btnCamera.visibility = View.VISIBLE
            binding.imgBtn.visibility = View.VISIBLE
            binding.moveOnMap.visibility = View.INVISIBLE

        }

        println(camera)
        println("카메라뭐임??")


        setPermission()

        binding.btnCamera.setOnClickListener {
            Thread {
                btnchocie = "버튼1"  // 카메라 촬영 버튼을 눌렀을 때
                takeCapture()
                var running = true
                while (running) {
                    if (result == "shit") {
                    } else {
                        uploadPhoto()
//                        Thread.sleep(2000L)
                        if (korea=="KoreaData") {
                            val koreaIntent = Intent(this@CameraActivity, LoadingActivity::class.java)
                            koreaIntent.putExtra("KoreaKey", "KoreaData")
                            koreaIntent.putExtra("PictureKey",pictureValue)
                            koreaIntent.putExtra("RotationKey","Rotate1")
                            startActivity(koreaIntent)
                        } else if (english=="EnglishData") {
                            val englishIntent = Intent(this@CameraActivity, LoadingActivity::class.java)
                            englishIntent.putExtra("EnglishKey", "EnglishData")
                            englishIntent.putExtra("PictureKey",pictureValue)
                            englishIntent.putExtra("RotationKey","Rotate1")
                            startActivity(englishIntent)
                        } else {
                            Toast.makeText(this, "application error!!", Toast.LENGTH_SHORT).show()
                        }
//                        landmarkValue = getJson( "http://192.168.1.33:8000/img_processing")
//                        value = getJson( "http://192.168.1.33:8000/landmark")
//                        judgeString()
                        result = "shit"
                        running = false
                    }
                }
            }.start()
            // 카메라 버튼을 눌렀을 때
            // 기본 카메라 앱을 실행하여 사진 촬영.
        }

        binding.imgBtn.setOnClickListener {
            Thread {
                btnchocie = "버튼2" // 이미지 업로드 버튼을 눌렀을 때
                openImage()
                var running = true
                while (running) {
                    if (result == "shit") {
                    } else {
                        uploadPhoto()
//                        Thread.sleep(2000L)

                        if (korea=="KoreaData") {
                            val koreaIntent = Intent(this@CameraActivity, LoadingActivity::class.java)
                            koreaIntent.putExtra("KoreaKey", "KoreaData")
                            koreaIntent.putExtra("PictureKey",pictureValue)
                            koreaIntent.putExtra("RotationKey","Rotate2")
                            startActivity(koreaIntent)
                        } else if (english=="EnglishData") {
                            val englishIntent = Intent(this@CameraActivity, LoadingActivity::class.java)
                            englishIntent.putExtra("EnglishKey", "EnglishData")
                            englishIntent.putExtra("PictureKey",pictureValue)
                            englishIntent.putExtra("RotationKey","Rotate2")
                            startActivity(englishIntent)
                        } else {
                            Toast.makeText(this, "application error!!", Toast.LENGTH_SHORT).show()
                        }

//                        landmarkValue = getJson( "http://192.168.1.33:8000/img_processing")
//                        value = getJson( "http://192.168.1.33:8000/landmark")
//                        judgeString()
                        result = "shit"
                        running = false
                    }
                }
            }.start()
        }

        binding.moveOnMap.setOnClickListener {
            if (korea=="KoreaData") {
                val koreaIntent = Intent(this@CameraActivity, LocationSearchActivity::class.java)
                koreaIntent.putExtra("KoreaKey", "KoreaData")
                startActivity(koreaIntent)
            } else if (english=="EnglishData") {
                val englishIntent = Intent(this@CameraActivity, LocationSearchActivity::class.java)
                englishIntent.putExtra("EnglishKey", "EnglishData")
                startActivity(englishIntent)
            } else {
                Toast.makeText(this, "application error!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
//    private fun fusion(bitmap: Bitmap){
//        val ei = ExifInterface(result)
//        val orientation: Int = ei.getAttributeInt(
//            ExifInterface.TAG_ORIENTATION,
//            ExifInterface.ORIENTATION_UNDEFINED
//        )
//
//        var rotatedBitmap: Bitmap? = null
//        when (orientation) {
//            ExifInterface.ORIENTATION_ROTATE_90 -> rotatedBitmap = rotateImage(bitmap, 90F)
//            ExifInterface.ORIENTATION_ROTATE_180 -> rotatedBitmap = rotateImage(bitmap, 180F)
//            ExifInterface.ORIENTATION_ROTATE_270 -> rotatedBitmap = rotateImage(bitmap, 270F)
//            ExifInterface.ORIENTATION_NORMAL -> rotatedBitmap = bitmap
//            else -> rotatedBitmap = bitmap
//        }
//
//    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.getWidth(), source.getHeight(),
            matrix, true
        )
    }


    private fun judgeString() {
        val responseJSON = JSONObject(value)
        var message = responseJSON.getString("landmark")
        message = message.replace("[", "")
        message = message.replace("]", "")
        val result = message.split(",")

        var landmarkName = ""
        var landmarkDesc = ""

        if (english == "EnglishData") {
            landmarkName = result[3]
            landmarkName = landmarkName.substring(1,landmarkName.length-1)

            landmarkDesc = result[5]
            landmarkDesc = landmarkDesc.replace("#",",")
            landmarkDesc = landmarkDesc.substring(1,landmarkDesc.length-1)

        } else if (korea == "KoreaData") {
            landmarkName = result[0]
            landmarkName = landmarkName.substring(1,landmarkName.length-1)

            landmarkDesc = result[4]
            landmarkDesc = landmarkDesc.replace("#",",")
            landmarkDesc = landmarkDesc.substring(1,landmarkDesc.length-1)
        }

            binding.root.post {
            binding.photoResult.text = landmarkName
            binding.TextMultiLine.text = landmarkDesc
            binding.btnCamera.visibility = View.INVISIBLE
            binding.imgBtn.visibility = View.INVISIBLE
            binding.moveOnMap.visibility = View.VISIBLE
            }

    }

    private fun openImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, Gallery)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(btnchocie=="버튼1") {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
                val bitmap: Bitmap
                val file = File(curPhotoPath)


                if (Build.VERSION.SDK_INT < 28) { // 안드로이드 9.0 (Pie) 버전보다 낮을 경우
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(file))
                    binding.GImageView.setImageBitmap(bitmap)
                } else { // 안드로이드 9.0 (Pie) 버전보다 높을 경우
                    val decode = ImageDecoder.createSource(this.contentResolver, Uri.fromFile(file))
                    bitmap = ImageDecoder.decodeBitmap(decode)
                    binding.GImageView.setImageBitmap(bitmap)
                }


                result  = curPhotoPath

                savePhoto(bitmap)
            }
        } else if(btnchocie=="버튼2") {
            if (requestCode == Gallery && resultCode == RESULT_OK && data != null) {
                selectedImage = data.getData()

                //여기서부터
                var bitmap: Bitmap? = null
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)

                } catch (e: IOException) {
                    e.printStackTrace()
                }
                binding.GImageView.setImageBitmap(bitmap)
                //여기까지는 이미지를 갤러리에서 클릭해서 그 사진을 이미지뷰에 보여주는 코드


                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                var cursor: Cursor = selectedImage?.let {
                    contentResolver?.query(
                        it,
                        filePathColumn,
                        null,
                        null,
                        null
                    )
                }!!

                if (cursor == null) {
                    selectedImage?.path.toString()
                } else {
                    cursor.moveToFirst()
                    var idx = cursor.getColumnIndex(filePathColumn[0])
                    result = cursor.getString(idx)
                    cursor.close()
                }
                Log.e("tag", "절대 " + result)

                //커서 사용해서 경로 확인

            } else {
                if (english == "EnglishData") {
                    Toast.makeText(this, "Failed to upload pictures", Toast.LENGTH_LONG).show()
                } else if (korea == "KoreaData") {
                    Toast.makeText(this, "사진 업로드 실패", Toast.LENGTH_LONG).show()

                }
            }
        }
    }

//    private fun exifOrientationToDefrees(exifOrientation: Int): Int {
//        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
//            return 90
//        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
//            return 180
//        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
//            return 270
//        }
//        return 0
//    }
//
//    private fun rotate(bitmap: Bitmap?, degrees: Int): Bitmap? {
//        var bitmap = bitmap
//        if (degrees != 0 && bitmap != null) {
//            val m = Matrix()
//            m.setRotate(degrees.toFloat(), bitmap.width.toFloat() / 2, bitmap.height.toFloat() / 2)
//            try {
//                val converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
//                if (bitmap != converted) {
//                    bitmap.recycle()
//                    bitmap = converted
//                }
//            } catch (ex: OutOfMemoryError) {
//
//            }
//        }
//        return bitmap
//    }

    private fun uploadPhoto() {
        //커서 사용해서 경로 확인
        val imagePath = result

        var src: Bitmap = BitmapFactory.decodeFile(imagePath)
        var resized: Bitmap = Bitmap.createScaledBitmap(src, 224, 224, true)
        val stream = ByteArrayOutputStream()
        resized.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        val byteArray = stream.toByteArray()
        pictureValue = byteArray

        fun saveBitmapToJpg(bitmap: Bitmap, name: String): String {
            val storage: File = getCacheDir() //  path = /data/user/0/YOUR_PACKAGE_NAME/cache
            val fileName = "$name.jpg"
            val imgFile = File(storage, fileName)
            try {
                imgFile.createNewFile()
                val out = FileOutputStream(imgFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out) //퀄리티를 최대로
                out.close()
            } catch (e: FileNotFoundException) {
                Log.e("saveBitmapToJpg", "FileNotFoundException : " + e.message)
            } catch (e: IOException) {
                Log.e("saveBitmapToJpg", "IOException : " + e.message)
            }
            Log.d("imgPath", getCacheDir().toString() + "/" + fileName)
            return getCacheDir().toString() + "/" + fileName
        }

        val imageFile = File(saveBitmapToJpg(resized, "testFile"))
//        val imageFile = File(imagePath)

        val retrofit = Retrofit.Builder()
            .baseUrl(CameraService.DJANGO_SITE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val postApi: CameraService = retrofit.create(CameraService::class.java)
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("multipart/data"), imageFile)
        val multiPartBody: MultipartBody.Part = MultipartBody.Part
            .createFormData("model_pic", imageFile.name, requestBody)
        val call: Call<RequestBody?>? = postApi.uploadFile(multiPartBody)
        call?.enqueue(object : Callback<RequestBody?> {
            override fun onResponse(call: Call<RequestBody?>?, response: Response<RequestBody?>?) {
                Log.d("good", "good")
                println(requestBody)
            }

            override fun onFailure(call: Call<RequestBody?>?, t: Throwable?) {
                Log.d("fail", "fail")
            }
        })
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

    private fun takeCapture() {
        // 기본 카메라 앱 실행
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.kimjjing1004.seoulapplication.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    // 이미지 파일 생성
    private fun createImageFile(): File {
        val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
            .apply {
                curPhotoPath = absolutePath
            }
    }

    // 테드 퍼미션 설정
    private fun setPermission() {
        if (english != "EnglishData") {
            val permission = object : PermissionListener {
                override fun onPermissionGranted() { // 설정해놓은 위험권한들이 허용 되었을 경우 이 곳을 수행함.
//                    Toast.makeText(this@CameraActivity, "권한이 허용 되었습니다.", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) { // 설정해놓은 위험권한들 중 거부를 한 경우 이곳을 수행함.
//                    Toast.makeText(this@CameraActivity, "권한이 거부 되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            TedPermission.with(this)
                .setPermissionListener(permission)
                .setRationaleMessage("카메라 앱을 사용하시려면 권한을 허용해주세요.")
                .setDeniedMessage("권한을 거부하셨습니다. [앱 설정] -> [권한] 항목에서 허용해주세요/")
                .setPermissions(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA
                )
                .check()
        } else {
            val permission = object : PermissionListener {
                override fun onPermissionGranted() { // 설정해놓은 위험권한들이 허용 되었을 경우 이 곳을 수행함.
//                    Toast.makeText(
//                        this@CameraActivity,
//                        "Authorization has been granted",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) { // 설정해놓은 위험권한들 중 거부를 한 경우 이곳을 수행함.
//                    Toast.makeText(
//                        this@CameraActivity,
//                        "Authorization has been denied.",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            }
            TedPermission.with(this)
                .setPermissionListener(permission)
                .setRationaleMessage("Please allow Authorization to use the camera app")
                .setDeniedMessage(
                    "You have denied Authorization. Please allow it in " +
                            "[App Settings] -> [Permissions] category/"
                )
                .setPermissions(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA
                )
                .check()
        }
    }


    // 갤러리에 저장
    private fun savePhoto(bitmap: Bitmap) {
//        val absolutePath = "/storage/emulated/0/"
//        val folderPath = "$absolutePath/Pictures/"
        val folderPath =
            Environment.getExternalStorageDirectory().absolutePath + "/Pictures/" // 사진폴더로 저장하기 위한 경로 선언

        val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val filename = "${timestamp}.jpeg"
        val folder = File(folderPath)
        if (!folder.isDirectory) { // 현재 해당 경로에 폴더가 존재하는지 검사
            folder.mkdirs() // make directory 줄임말로 해당 경로에 폴더 자동으로 새로 만들기.
        }

//        val ei = ExifInterface(folderPath)
//        var orientation: Int = ei.getAttributeInt(
//            ExifInterface.TAG_ORIENTATION,
//            ExifInterface.ORIENTATION_UNDEFINED
//        )
//        val angle = when (orientation) {
//            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
//            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
//            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
//            else -> 0f
//        }
//
//        val matrix = Matrix()
//        matrix.postRotate(angle)


        // 실제적인 저장처리
        val out = FileOutputStream(folderPath + filename)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
            Uri.parse("file://$folderPath$filename")))



        if (english != "EnglishData") {
            Toast.makeText(this, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "The picture has been saved in the album.", Toast.LENGTH_SHORT)
                .show()
        }
    }

//    private fun rotatedBitmap(bitmap: Bitmap?): Bitmap? {
//        val matrix = Matrix()
//        var resultBitmap: Bitmap? = null
//
//        when (getOrientationOfImage(curPhotoPath)) {
//            0 -> matrix.setRotate(0F)
//            90 -> matrix.setRotate(90F)
//            180 -> matrix.setRotate(180F)
//            270 -> matrix.setRotate(270F)
//        }
//
//        resultBitmap = try {
//            bitmap?.let {
//                Bitmap.createBitmap(it, 0, 0, bitmap.width, bitmap.height, matrix, true)
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//        return resultBitmap
//    }

//    private fun getOrientationOfImage(filepath: String): Int? {
//        var exif: ExifInterface? = null
//        var result: Int? = null
//
//        try {
//            exif = ExifInterface(filepath)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return -1
//        }
//
//        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
//        if (orientation != -1) {
//            result = when (orientation) {
//                ExifInterface.ORIENTATION_ROTATE_90 -> 90
//                ExifInterface.ORIENTATION_ROTATE_180 -> 180
//                ExifInterface.ORIENTATION_ROTATE_270 -> 270
//                else -> 0
//            }
//        }
//        return result
//    }
}