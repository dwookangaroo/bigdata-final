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
    var exifOrientation = 0

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
            binding.GImageView.setImageBitmap(imageBitmap)


            // 회전되는 것을 야메로 막기 위해서 사용했을 뿐
            // 실제로 어플을 출시하는 경우 이런 코드는 사용해선 안된다.
            rotateValue = intent.getStringExtra("Rotation").toString()
            if(rotateValue=="Rotate1"){
//                imageBitmap = rotateImage(imageBitmap, 90F)
                binding.GImageView.setImageBitmap(imageBitmap)
            }
            else if(rotateValue=="Rotate2"){
                binding.GImageView.setImageBitmap(imageBitmap)
            }


            binding.btnCamera.text = "Photo shoot"
            binding.imgBtn.text = "Select image"
            binding.moveOnMap.text = "Move on Map"
            binding.photoResult.text = "Take a picture or upload it"
            binding.TextMultiLine.text = ""

            // XML 글자들을 숨기는 데 쓰이는 방법이다.
            binding.btnCamera.visibility = View.INVISIBLE
            binding.imgBtn.visibility = View.INVISIBLE
            binding.GImageView.visibility = View.INVISIBLE
            binding.photoResult.visibility = View.INVISIBLE

            
            // ByteArray형태를 bitmap 형태로 바꾼 후 ImageView에 띄워주는 코드
        } else if (intent.hasExtra("Korea") && intent.hasExtra("Camera")
            && intent.hasExtra("Picture")  && intent.hasExtra("Rotation")) {
            korea = intent.getStringExtra("Korea").toString()
            english = ""
            camera = intent.getStringExtra("Camera").toString()
            pictureImage = intent.getByteArrayExtra("Picture")!!
            var imageBitmap = BitmapFactory.decodeByteArray(pictureImage, 0, pictureImage.size)
            binding.GImageView.setImageBitmap(imageBitmap)
            

            rotateValue = intent.getStringExtra("Rotation").toString()
            if(rotateValue=="Rotate1"){
//                imageBitmap = rotateImage(imageBitmap, 90F)
                binding.GImageView.setImageBitmap(imageBitmap)
            }
            else if(rotateValue=="Rotate2"){
                binding.GImageView.setImageBitmap(imageBitmap)
            }


            binding.btnCamera.text = "카메라촬영"
            binding.imgBtn.text = "이미지선택"
            binding.moveOnMap.text = "지도로이동"
            binding.photoResult.text = "사진을 촬영하거나 업로드하세요"
            binding.TextMultiLine.text = ""

            binding.btnCamera.visibility = View.INVISIBLE
            binding.imgBtn.visibility = View.INVISIBLE
            binding.photoResult.visibility = View.INVISIBLE
            binding.GImageView.visibility = View.INVISIBLE



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
                landmarkValue = getJson("http://15.165.104.248:8000/img_processing")
                value = getJson("http://15.165.104.248:8000/landmark")
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


        // 한번에 동작시키기 위해서 무한 루프를 준것이다.
        // 저장되어 있는 사진을 업로드 하기 위해 위치로 들어가고 사진을 선택하기 까지
        // 시간은 계속 흐르고 있고 ImageView에 사진이 나와야만 다음 조건으로 넘어간다.
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


        // 한번에 동작시키기 위해서 무한 루프를 준것이다.
        // 저장되어 있는 사진을 업로드 하기 위해 위치로 들어가고 사진을 선택하기 까지
        // 시간은 계속 흐르고 있고 ImageView에 사진이 나와야만 다음 조건으로 넘어간다.
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


    // String 형태롤 다시 JSON형태로 바꾼 후 배열 자체가 문자열로 되어 있으므로
    // 양 옆의 []를 제거한 후 split 조건으로 다시 배열로 만들어 필요한 값들을 받아온다.
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
            binding.moveOnMap.visibility = View.VISIBLE
            binding.photoResult.visibility = View.VISIBLE
            binding.GImageView.visibility = View.VISIBLE

            }

    }

    // 미디어 파일에서 이미지를 열 수 있는 상당히 간단한 코드
    private fun openImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, Gallery)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(btnchocie=="버튼1") {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
                var bitmap: Bitmap
                // 사진의 경로가 있어야만 파일 형태로 저장이 된다.
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

                val ei = ExifInterface(curPhotoPath)
                val orientation: Int = ei.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED
                )

                var rotatedBitmap: Bitmap? = null

                when(orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 ->
                    rotatedBitmap = rotateImage(bitmap, 0F)

                    ExifInterface.ORIENTATION_ROTATE_180 ->
                    rotatedBitmap = rotateImage(bitmap, 90F)


                    ExifInterface.ORIENTATION_ROTATE_270 ->
                    rotatedBitmap = rotateImage(bitmap, 180F)


                    ExifInterface.ORIENTATION_NORMAL ->
                    rotatedBitmap = rotateImage(bitmap, 270F)

                    else -> println("what???")
                }

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


                
                // 나중에 해당 코드를 다시 공부해본다.
                // 현재로서는 완전하게 이해가 되지는 않음
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



    private fun uploadPhoto() {
        //커서 사용해서 경로 확인
        val imagePath = result
        println(imagePath)
        println("이미지 경로 알려주셈")


        // 이미지를 다시 줄이고 퀄리트를 높여 Stream 형태에서 BiteArray 형태로 만들어준다.
        var src: Bitmap = BitmapFactory.decodeFile(imagePath)
        var resized: Bitmap = Bitmap.createScaledBitmap(src, 1000, 1000, true)
        val stream = ByteArrayOutputStream()
        resized.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        val byteArray = stream.toByteArray()
        pictureValue = byteArray


        // 비트맵 파일을 JPG 파일로 바꿔주는 방법
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

        println(imageFile.toString())
        println("이미지 파일 알려줘라")

        // 장고 서버를 위한 retorit을 생성한다.

        val retrofit = Retrofit.Builder()
            .baseUrl(CameraService.DJANGO_SITE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // 서버 통신을 위해 필요한 RequestBody, Retrofit, multiPartBody, Call
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

    // 장고에서 웹서버로 띄운 것을 JSON형태로 받아와 STRING형태로 Return 해주는 코드
    // 장고에서 한글로 띄어주는 코드를 짤경우 이중으로 변환하는 과정에서 오류가 생기므로
    // 이상하게 나와도 해당 funcion을 써서 받아오면 전혀 문제 없다.
    // 차후에 String형태로 Return 안해줘도 되는지 확인할 것
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


    // 촬영할 때 이미지 회전되는 것을 방지하기 위해 사용
    fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }


}