package com.snote.kt.network.repository

import com.snote.kt.network.model.HistoryItem
import com.snote.kt.network.net.NetworkClient
import com.snote.kt.network.util.NetworkResult
import com.snote.kt.util.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author snote
 * @date 2025/9/17 18:56
 * @description repo
 */
class TodayHistoryRepository {
    private val toDayHistoryService = NetworkClient.toDayHistoryService

    suspend fun fetchTodayHistory(): NetworkResult<List<HistoryItem>> {
        LogUtil.forceLog("📦 Repository: 开始获取历史数据")
        
        return try {
            withContext(Dispatchers.IO) {
                LogUtil.forceLog("🚀 Repository: 正在调用 API 接口")
                val response = toDayHistoryService.getTodayHistory()
                
                LogUtil.forceLog("📊 Repository: API 响应数据 - code: ${response.code}, message: ${response.message}")
                
                if (response.code == 200) {
                    val itemsCount = response.data.items.size
                    LogUtil.i("✅ Repository: 成功获取 $itemsCount 条历史数据", "ArticleRepository")
                    LogUtil.d("📅 Repository: 日期信息 - ${response.data.date} (月: ${response.data.month}, 日: ${response.data.day})", "ArticleRepository")
                    NetworkResult.Success(response.data.items)
                } else {
                    LogUtil.w("⚠️ Repository: API 返回错误码 ${response.code}: ${response.message}", "ArticleRepository")
                    NetworkResult.Error(response.message)
                }
            }
        } catch (e: Exception) {
            LogUtil.e("🚫 Repository: 获取历史数据失败", e, "ArticleRepository")
            NetworkResult.Error(e.message ?: "未知错误")
        }
    }
}