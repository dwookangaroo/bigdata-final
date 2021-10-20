package com.kimjjing1004.seoulapplication.register

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RegisterService {

    @FormUrlEncoded
    @POST("/app_register/")
    fun requestRegister(
        // 여기가 인풋을 정의하는 곳
        @Field("userid") userid:String,
        @Field("userpw") userpw:String,
        @Field("userpw2") userpw2:String
    ) : Call<Register> // 아웃풋을 정의하는 곳
}