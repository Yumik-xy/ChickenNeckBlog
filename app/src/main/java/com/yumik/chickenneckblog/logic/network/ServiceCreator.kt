package com.yumik.chickenneckblog.logic.network

import com.yumik.chickenneckblog.ProjectApplication
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {

    private val retrofit = Retrofit.Builder()
        .baseUrl(ProjectApplication.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)

}