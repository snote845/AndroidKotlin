package com.snote.kt.util

import android.util.Log

/**
 * @author snote
 * @date 2025/9/17
 * @description 统一日志工具类
 */
object LogUtil {
    private const val TAG = "AndroidKotlin"
    // 降低单段日志长度，确保在所有设备上都能正常显示
    private const val MAX_LOG_LENGTH = 3000
    // JSON 内容的单段长度，更小一些以保证可读性
    private const val MAX_JSON_SEGMENT_LENGTH = 2000
    
    // 日志开关控制，可以通过这个属性动态控制
    private const val isLogEnabled = true
    
    /**
     * 设置日志级别阈值（可选）
     * Log.VERBOSE = 2
     * Log.DEBUG = 3  
     * Log.INFO = 4
     * Log.WARN = 5
     * Log.ERROR = 6
     */
    private const val LOG_LEVEL = Log.DEBUG
    
    /**
     * 强制输出日志（忽略 DEBUG 设置，用于测试）
     */
    fun forceLog(message: String, tag: String = TAG) {
        Log.i(tag, "[FORCE] $message")
    }
    
    /**
     * 测试日志功能是否正常
     */
    fun testLog() {
        // 测试长文本分段
        testLongLogSegmentation()
    }
    
    /**
     * 测试长日志分段功能
     */
    fun testLongLogSegmentation() {
        val longContent = StringBuilder()
        longContent.append("🧪 测试长文本分段功能:\n")
        longContent.append("{“测试数据”: {\n")
        
        // 生成一个超长的 JSON 样式的测试数据
        for (i in 1..100) {
            longContent.append("  “字段$i”: “这是一个很长的测试字符串，用于测试日志分段功能是否正常工作。这个数据包含了中文字符和英文字符，以及数字和特殊符号 - Test $i”")
            if (i < 100) longContent.append(",")
            longContent.append("\n")
        }
        
        longContent.append("}")
        longContent.append("}")
        
        Log.i(TAG, "[TEST] 开始测试长文本分段，内容长度: ${longContent.length} 字符")
        printJsonContent("Long Content Test", longContent.toString(), "Test-LongContent")
        Log.i(TAG, "[TEST] 长文本分段测试完成")
    }
    
    /**
     * 打印调试日志
     */
    fun d(message: String, tag: String = TAG) {
        if (isLogEnabled) {
            printLongLog(Log.DEBUG, tag, message)
        }
    }
    
    /**
     * 打印信息日志
     */
    fun i(message: String, tag: String = TAG) {
        if (isLogEnabled) {
            printLongLog(Log.INFO, tag, message)
        }
    }
    
    /**
     * 打印警告日志
     */
    fun w(message: String, tag: String = TAG) {
        if (isLogEnabled) {
            printLongLog(Log.WARN, tag, message)
        }
    }
    
    /**
     * 打印错误日志
     */
    fun e(message: String, throwable: Throwable? = null, tag: String = TAG) {
        if (isLogEnabled) {
            val fullMessage = if (throwable != null) {
                "$message\n${throwable.stackTraceToString()}"
            } else {
                message
            }
            printLongLog(Log.ERROR, tag, fullMessage)
        }
    }
    
    /**
     * 网络请求日志 - 请求开始
     */
    fun networkRequest(url: String, method: String, headers: Map<String, String>? = null, body: String? = null) {
        if (!isLogEnabled) return
        
        val logBuilder = StringBuilder()
        logBuilder.append("🚀 ========== 网络请求开始 ==========\n")
        logBuilder.append("📍 URL: $url\n")
        logBuilder.append("🔧 Method: $method\n")
        logBuilder.append("⏰ Time: ${getCurrentTime()}\n")
        
        headers?.let { headerMap ->
            if (headerMap.isNotEmpty()) {
                logBuilder.append("📋 Headers:\n")
                headerMap.forEach { (key, value) ->
                    logBuilder.append("   $key: $value\n")
                }
            }
        }
        
        logBuilder.append("=====================================")
        
        // 先输出头部信息
        i(logBuilder.toString(), "Network-Request")
        
        // 单独处理请求体
        body?.let {
            printJsonContent("Request Body", it, "Network-Request-Body")
        }
    }
    
    /**
     * 网络请求日志 - 响应成功
     */
    fun networkResponse(url: String, responseCode: Int, responseBody: String?, duration: Long) {
        if (!isLogEnabled) return
        
        val logBuilder = StringBuilder()
        logBuilder.append("✅ ========== 网络响应成功 ==========\n")
        logBuilder.append("📍 URL: $url\n")
        logBuilder.append("📊 Response Code: $responseCode\n")
        logBuilder.append("⏱️ Duration: ${duration}ms\n")
        logBuilder.append("⏰ Time: ${getCurrentTime()}\n")
        logBuilder.append("=====================================\n")
        
        // 先输出头部信息
        i(logBuilder.toString(), "Network-Response")
        
        // 单独处理响应体，使用更小的分段
        responseBody?.let { body ->
            printJsonContent("Response Body", body, "Network-Response-Body")
        }
    }
    
    /**
     * 网络请求日志 - 响应失败
     */
    fun networkError(url: String, error: Throwable, duration: Long) {
        if (!isLogEnabled) return
        
        val logBuilder = StringBuilder()
        logBuilder.append("❌ ========== 网络请求失败 ==========\n")
        logBuilder.append("📍 URL: $url\n")
        logBuilder.append("⏱️ Duration: ${duration}ms\n")
        logBuilder.append("⏰ Time: ${getCurrentTime()}\n")
        logBuilder.append("💥 Error: ${error.message}\n")
        logBuilder.append("📝 Stack Trace:\n${error.stackTraceToString()}\n")
        logBuilder.append("=====================================")
        e(logBuilder.toString(), tag = "Network-Error")
    }
    
    /**
     * 专门打印 JSON 内容，使用更小的分段确保可读性
     */
    private fun printJsonContent(title: String, content: String, tag: String) {
        if (content.length <= MAX_JSON_SEGMENT_LENGTH) {
            // 内容较短，直接输出
            Log.i(tag, "📨 $title:\n$content")
        } else {
            // 内容较长，分段输出
            val totalLength = content.length
            val totalSegments = (totalLength + MAX_JSON_SEGMENT_LENGTH - 1) / MAX_JSON_SEGMENT_LENGTH
            
            // 输出头部信息
            Log.i(tag, "📨 $title (Total: ${totalLength} chars, Split into $totalSegments parts):")
            
            var start = 0
            var segmentIndex = 1
            
            while (start < content.length) {
                val end = minOf(start + MAX_JSON_SEGMENT_LENGTH, content.length)
                val segment = content.substring(start, end)
                
                // 为每个分段添加清晰的标识
                val segmentTag = "$tag-Part$segmentIndex"
                val prefix = if (segmentIndex == 1) "┌─ " else if (segmentIndex == totalSegments) "└─ " else "├─ "
                
                Log.i(segmentTag, "$prefix Part $segmentIndex/$totalSegments:\n$segment")
                
                start = end
                segmentIndex++
            }
            
            // 输出结束标识
            Log.i(tag, "🏁 $title - End of content")
        }
    }
    
    /**
     * 分段打印长日志（Android Log 有长度限制）
     */
    private fun printLongLog(level: Int, tag: String, message: String) {
        if (message.length <= MAX_LOG_LENGTH) {
            // 短消息直接输出
            when (level) {
                Log.DEBUG -> Log.d(tag, message)
                Log.INFO -> Log.i(tag, message)
                Log.WARN -> Log.w(tag, message)
                Log.ERROR -> Log.e(tag, message)
            }
        } else {
            // 长消息分段输出，添加分段标识
            val totalSegments = (message.length + MAX_LOG_LENGTH - 1) / MAX_LOG_LENGTH
            var start = 0
            var segmentIndex = 1
            
            while (start < message.length) {
                val end = minOf(start + MAX_LOG_LENGTH, message.length)
                val segment = message.substring(start, end)
                val segmentTag = "$tag-Part$segmentIndex/$totalSegments"
                
                when (level) {
                    Log.DEBUG -> Log.d(segmentTag, segment)
                    Log.INFO -> Log.i(segmentTag, segment)
                    Log.WARN -> Log.w(segmentTag, segment)
                    Log.ERROR -> Log.e(segmentTag, segment)
                }
                
                start = end
                segmentIndex++
            }
        }
    }
    
    /**
     * 获取当前时间字符串
     */
    private fun getCurrentTime(): String {
        return java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", java.util.Locale.getDefault())
            .format(java.util.Date())
    }
}