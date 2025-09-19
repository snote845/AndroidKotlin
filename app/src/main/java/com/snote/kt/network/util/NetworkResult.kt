package com.snote.kt.network.util

/**
 * @author snote
 * @date 2025/9/17 18:52
 * @description network result
 */
sealed class NetworkResult<out T>{
    data class Success<out T>(val data:T):NetworkResult<T>()
    data class Error(val message: String): NetworkResult<Nothing>()
    object Loading: NetworkResult<Nothing>()
}