package com.yumik.chickenneckblog.logic.network

import com.yumik.chickenneckblog.logic.response.BaseResponse
import com.yumik.chickenneckblog.logic.response.CheckUpload
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface DownLoadNetwork {
    @GET("/file/getApk")
    fun checkUpdate(): Call<BaseResponse<CheckUpload>>

    @Streaming
    @GET
    fun download(@Url url: String): Call<ResponseBody>
}