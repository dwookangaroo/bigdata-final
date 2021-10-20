package com.kimjjing1004.seoulapplication.main.camera

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CameraService {
    @Multipart
    @POST("/upload/")
    fun uploadFile(
        // 여기가 인풋을 정의하는 곳
        @Part file: MultipartBody.Part?
    ): Call<RequestBody?>? // 아웃풋을 정의하는 곳

    companion object {
        const val DJANGO_SITE = "http://192.168.1.33:8000/"
    }
}