package com.snote.kt.network.api

/**
 * @author snote
 * @date 2025/9/17 18:39
 * @description test
 */
import retrofit2.http.GET
import com.snote.kt.network.model.ApiResponse

interface ToDayHistoryService {
    @GET("v2/today_in_history")
    suspend fun getTodayHistory(): ApiResponse
}