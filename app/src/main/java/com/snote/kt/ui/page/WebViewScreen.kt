package com.snote.kt.ui.page

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.snote.kt.util.LogUtil
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

/**
 * @author snote
 * @date 2025/9/17
 * @description WebView页面，用于显示网页内容
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    navController: NavHostController,
    url: String,
    title: String = "网页详情"
) {
    // 解码URL
    val decodedUrl = remember {
        try {
            URLDecoder.decode(url, StandardCharsets.UTF_8.toString())
        } catch (e: Exception) {
            LogUtil.w("URL解码失败: $url", "WebViewScreen")
            url
        }
    }
    
    var isLoading by remember { mutableStateOf(true) }
    var webView: WebView? by remember { mutableStateOf(null) }
    var canGoBack by remember { mutableStateOf(false) }
    
    LogUtil.i("🌐 WebViewScreen 打开: $decodedUrl", "WebViewScreen")
    
    // 处理返回键
    BackHandler(enabled = canGoBack) {
        webView?.goBack()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (decodedUrl.isNotEmpty()) {
                            Text(
                                text = decodedUrl,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { webView?.reload() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "刷新"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 加载进度条
            if (isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Spacer(modifier = Modifier.height(4.dp))
            }
            
            // WebView
            Box(modifier = Modifier.fillMaxSize()) {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            webView = this
                            
                            webViewClient = object : WebViewClient() {
                                override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                                    super.onPageStarted(view, url, favicon)
                                    isLoading = true
                                    LogUtil.d("🔄 WebView 开始加载: $url", "WebViewScreen")
                                }
                                
                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)
                                    isLoading = false
                                    canGoBack = view?.canGoBack() == true
                                    LogUtil.i("✅ WebView 加载完成: $url", "WebViewScreen")
                                }
                                
                                override fun onReceivedError(
                                    view: WebView?,
                                    errorCode: Int,
                                    description: String?,
                                    failingUrl: String?
                                ) {
                                    super.onReceivedError(view, errorCode, description, failingUrl)
                                    isLoading = false
                                    LogUtil.e("❌ WebView 加载错误: $description (Code: $errorCode)", tag = "WebViewScreen")
                                }
                            }
                            
                            settings.apply {
                                javaScriptEnabled = true
                                domStorageEnabled = true
                                loadWithOverviewMode = true
                                useWideViewPort = true
                                setSupportZoom(true)
                                builtInZoomControls = true
                                displayZoomControls = false
                            }
                            
                            loadUrl(decodedUrl)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
                
                // 优雅的WebView加载页面
                if (isLoading) {
                    WebViewLoadingScreen()
                }
                
                // 如果URL为空，显示提示
                if (decodedUrl.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "⚠️ 链接地址为空",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "无法加载网页内容",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * WebView专用的优雅加载屏幕组件
 */
@Composable
fun WebViewLoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                // 白色半透明背景，营造网页加载的感觉
                Color.White.copy(alpha = 0.95f)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
        ) {
            // 模拟网页标题区域
            WebPageTitleShimmer()
            
            // 模拟网页内容区域
            repeat(8) {
                WebPageContentShimmer()
            }
        }
        
        // 中央加载提示
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 加载动画圆圈
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .webViewShimmerEffect()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "🌐 正在加载网页内容...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 网页标题区域的占位组件
 */
@Composable
fun WebPageTitleShimmer() {
    Column {
        // 主标题
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(28.dp)
                .clip(RoundedCornerShape(6.dp))
                .webViewShimmerEffect()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 副标题或链接
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .webViewShimmerEffect()
        )
    }
}

/**
 * 网页内容区域的占位组件
 */
@Composable
fun WebPageContentShimmer() {
    Column {
        // 内容行，模拟段落
        repeat(3) { index ->
            val widthFraction = when (index) {
                0 -> 1f
                1 -> 0.9f
                else -> 0.6f
            }
            
            Box(
                modifier = Modifier
                    .fillMaxWidth(widthFraction)
                    .height(14.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .webViewShimmerEffect()
            )
            
            if (index < 2) {
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
    }
}

/**
 * WebView专用的Shimmer效果修饰符
 */
fun Modifier.webViewShimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "webview_shimmer")
    
    val alpha = transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    val slideAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000),
            repeatMode = RepeatMode.Restart
        ),
        label = "slide"
    )
    
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha.value),
                MaterialTheme.colorScheme.primary.copy(alpha = alpha.value * 0.3f),
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha.value)
            ),
            start = Offset(slideAnim.value - 300f, slideAnim.value - 300f),
            end = Offset(slideAnim.value, slideAnim.value)
        )
    )
}