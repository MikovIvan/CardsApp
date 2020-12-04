package ru.mikov.test.data.remote

import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

object NetworkManager {
    val api: RestService by lazy {
        //client
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val moshi = Moshi.Builder()
            .add(DateAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()

        val client = OkHttpClient().newBuilder()
            .readTimeout(2, TimeUnit.SECONDS) //socket timeout (GET)
            .writeTimeout(5, TimeUnit.SECONDS) //socket timeout (POST,PUT, etc)
            .addInterceptor(logging)
            .build()

        //retrofit
        val retrofit = Retrofit.Builder()
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(AppConfig.BASE_URL)
            .build()

        retrofit.create(RestService::class.java)

    }
}

class DateAdapter {
    @FromJson
    fun fromJson(timestamp: Long) = Date(timestamp)

    @ToJson
    fun toJson(date: Date) = date.time
}