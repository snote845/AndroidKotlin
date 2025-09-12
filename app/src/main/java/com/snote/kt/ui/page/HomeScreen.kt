package com.snote.kt.ui.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.snote.kt.ui.theme.AndroidKotlinTheme

@Composable
fun HomeScreen(navController: NavHostController) {

    val snackbarHostState = remember { SnackbarHostState() }
    val content = remember { mutableStateOf("这是个例子") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { innerPadding ->
        AppToastProvider {
            Greeting(
                content,
                modifier = Modifier.padding(innerPadding),navController
            )
        }
    }


}

@Composable
fun Greeting(
    content: MutableState<String>,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = modifier) {
            Text(content.value)
            Button(onClick = {
                content.value = "状态发生了改变"
                AppToast.show("哎呀，收到了点击", "点我试试", SnackbarDuration.Short)
            }, modifier = modifier) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null, // 无障碍描述
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Text("点击我")
            }
            Button(onClick = {
                navController.navigate("details")
            }) {
                Text("跳转详情界面")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidKotlinTheme {
        //Greeting(content.value, "Android")
    }
}