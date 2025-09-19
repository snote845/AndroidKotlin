package com.snote.kt.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.snote.kt.ui.page.DetailScreen
import com.snote.kt.ui.page.HomeScreen
import com.snote.kt.ui.page.WebViewScreen
import com.snote.kt.ui.page.list.ListScreen
import com.snote.kt.ui.theme.AndroidKotlinTheme
import com.snote.kt.util.LogUtil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 测试日志功能
        LogUtil.i("🚀 MainActivity 启动了")
        
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets -> insets }

        setContent {
            AndroidKotlinTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "home") {
                    composable("home") { HomeScreen(navController) }
                    composable("details") { DetailScreen(navController) }
                    composable("list") { ListScreen(navController) }
                    composable(
                        "webview/{url}/{title}",
                        arguments = listOf(
                            navArgument("url") { type = NavType.StringType },
                            navArgument("title") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val url = backStackEntry.arguments?.getString("url") ?: ""
                        val title = backStackEntry.arguments?.getString("title") ?: "网页详情"
                        WebViewScreen(navController, url, title)
                    }
                }
            }
        }
    }
}
