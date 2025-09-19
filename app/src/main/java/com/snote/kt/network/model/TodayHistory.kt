package com.snote.kt.network.model

/**
 * @author snote
 * @date 2025/9/17 16:14
 * @description data
 */

// 单个历史事件项
data class HistoryItem(
    val title: String,
    val year: String,
    val description: String,
    val event_type: String,
    val link: String
)

// 今日历史数据
data class TodayHistoryData(
    val date: String,
    val month: Int,
    val day: Int,
    val items: List<HistoryItem>
)

// API 响应包装类
data class ApiResponse(
    val code: Int,
    val message: String,
    val data: TodayHistoryData
)