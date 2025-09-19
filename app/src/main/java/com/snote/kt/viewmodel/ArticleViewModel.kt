package com.snote.kt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snote.kt.network.model.HistoryItem
import com.snote.kt.network.util.NetworkResult
import com.snote.kt.util.LogUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @author snote
 * @date 2025/9/17 16:16
 * @description ViewModel
 */
class ArticleViewModel(
    private val repository: com.snote.kt.network.repository.TodayHistoryRepository = com.snote.kt.network.repository.TodayHistoryRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow<NetworkResult<List<HistoryItem>>>(NetworkResult.Loading)
    val uiState: StateFlow<NetworkResult<List<HistoryItem>>> = _uiState

    private var fetchJob: Job? = null

    fun loadArticles() {
        LogUtil.i("📡 ViewModel: 用户请求加载文章列表", "ArticleViewModel")
        
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            LogUtil.d("🔄 ViewModel: 设置加载状态", "ArticleViewModel")
            _uiState.value = NetworkResult.Loading
            
            val result = repository.fetchTodayHistory()
            
            when (result) {
                is NetworkResult.Success -> {
                    LogUtil.i("✅ ViewModel: 成功获取 ${result.data.size} 条数据", "ArticleViewModel")
                }
                is NetworkResult.Error -> {
                    LogUtil.w("⚠️ ViewModel: 获取数据失败 - ${result.message}", "ArticleViewModel")
                }
                is NetworkResult.Loading -> {
                    LogUtil.d("🔄 ViewModel: 数据加载中", "ArticleViewModel")
                }
            }
            
            _uiState.value = result
        }
    }

    fun retry() {
        LogUtil.i("🔄 ViewModel: 用户点击重试按钮", "ArticleViewModel")
        loadArticles()
    }
}