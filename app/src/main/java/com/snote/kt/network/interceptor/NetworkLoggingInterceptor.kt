package com.snote.kt.network.interceptor

import com.snote.kt.util.LogUtil
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.io.IOException

/**
 * @author snote
 * @date 2025/9/17
 * @description 网络请求日志拦截器
 */
class NetworkLoggingInterceptor : Interceptor {
    
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startTime = System.currentTimeMillis()
        
        // 强制记录拦截器被调用
        LogUtil.forceLog("🔍 拦截器被调用: ${request.url}")
        
        // 记录请求信息
        logRequest(request)
        
        return try {
            val response = chain.proceed(request)
            val endTime = System.currentTimeMillis()
            val duration = endTime - startTime
            
            // 记录响应信息
            logResponse(request.url.toString(), response, duration)
            
            response
        } catch (e: Exception) {
            val endTime = System.currentTimeMillis()
            val duration = endTime - startTime
            
            // 记录错误信息
            LogUtil.networkError(request.url.toString(), e, duration)
            throw e
        }
    }
    
    /**
     * 记录请求信息
     */
    private fun logRequest(request: Request) {
        val url = request.url.toString()
        val method = request.method
        
        // 提取请求头
        val headers = mutableMapOf<String, String>()
        request.headers.forEach { pair ->
            headers[pair.first] = pair.second
        }
        
        // 提取请求体
        val requestBody = request.body?.let { body ->
            try {
                val buffer = Buffer()
                body.writeTo(buffer)
                buffer.readUtf8()
            } catch (e: Exception) {
                "无法读取请求体: ${e.message}"
            }
        }
        
        LogUtil.networkRequest(url, method, headers, requestBody)
    }
    
    /**
     * 记录响应信息
     */
    private fun logResponse(url: String, response: Response, duration: Long) {
        val responseCode = response.code
        
        // 读取响应体（注意不能重复读取）
        val responseBody = try {
            val source = response.body?.source()
            source?.request(Long.MAX_VALUE)
            val buffer = source?.buffer?.clone()
            buffer?.readUtf8()
        } catch (e: Exception) {
            "无法读取响应体: ${e.message}"
        }
        
        LogUtil.networkResponse(url, responseCode, responseBody, duration)
    }
}