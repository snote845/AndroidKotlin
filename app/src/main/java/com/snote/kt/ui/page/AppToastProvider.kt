package com.snote.kt.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * @author gaor
 * @date 2025/9/11 10:47
 * @description AppToastProvider
 */
@Composable
fun AppToastProvider(content : @Composable () -> Unit) {
// 1. 创建并记住 SnackbarHostState 和 CoroutineScope
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // 2. 使用 LaunchedEffect 来在 Composable 进入/离开组合时注册/注销
    //    LaunchedEffect(true) 保证这个效应只在首次组合时运行，并在最终离开时清理。
    DisposableEffect (true) {
        AppToast.register(snackbarHostState, coroutineScope)

        onDispose {
            // 当 AppToastProvider 离开组合时，清理引用以防内存泄漏
            AppToast.unregister()
        }
    }

    // 3. 提供一个 Scaffold，它包含了用于显示 Snackbar 的宿主
    //    你可以根据需要将其替换为你自己的根布局，只要包含 SnackbarHost 即可。
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)/*{snackbarData ->
            // 创建一个完全自定义的 Toast UI
            MyCustomToast(snackbarData)
        } */}
    ) { innerPadding ->
        // 在这里渲染你的实际应用内容
        // 注意：如果你使用 Scaffold，你的内容应该使用它提供的 padding
        // 如果你的根布局是 Box，则不需要处理 padding
        Box(modifier = Modifier.padding(innerPadding)) {
            content()
        }
    }
}


// 定义你自己的 Toast Composable
@Composable
private fun MyCustomToast(data: SnackbarData) {
    Surface(
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        color = Color.Blue.copy(alpha = 0.8f),
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 左侧：图标和文字
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = "Info", tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = data.visuals.message,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            // 右侧：操作按钮
            data.visuals.actionLabel?.let { actionLabel ->
                TextButton(onClick = { data.performAction() }) {
                    Text(actionLabel, color = Color.Yellow)
                }
            }
        }
    }
}
