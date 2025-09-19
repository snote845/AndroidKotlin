package com.snote.kt.network.net

import com.snote.kt.network.api.ToDayHistoryService
import com.snote.kt.network.interceptor.NetworkLoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author snote
 * @date 2025/9/17 18:43
 * @description net
 */
object NetworkClient {
    private const val BASE_URL = "https://60s.viki.moe/"
    
    private val okhttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        // 添加自定义网络日志拦截器
        .addInterceptor(NetworkLoggingInterceptor())
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
//                .addHeader("User-Agent", "AndroidKotlin/1.0")
                .build()
            chain.proceed(request)
        }
        .build()
        
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okhttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val toDayHistoryService: ToDayHistoryService by lazy {
        retrofit.create(ToDayHistoryService::class.java)
    }
}