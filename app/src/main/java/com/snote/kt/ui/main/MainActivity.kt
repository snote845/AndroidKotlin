package com.snote.kt.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.snote.kt.ui.page.DetailScreen
import com.snote.kt.ui.page.HomeScreen
import com.snote.kt.ui.theme.AndroidKotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets -> insets }

        setContent {
            AndroidKotlinTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "home") {
                    composable("home") { HomeScreen(navController) }
                    composable("details") { DetailScreen(navController) }
                }
            }
        }
    }
}
