package com.snote.kt.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun DetailScreen(navController: NavHostController) {
    /*Column(
        modifier = Modifier
            .fillMaxSize()
            .displayCutoutPadding()
    ) {
        Button(onClick = {
            navController.navigate("home")
        }) {
            Text("返回上一页")
        }
    }*/
    HybridList()
}

data class ListItemData(val icon: ImageVector, val text: String)

val sampleData = listOf(
    ListItemData(Icons.Filled.Home, "首页"),
    ListItemData(Icons.Filled.Search, "搜索"),
    ListItemData(Icons.Filled.Person, "个人中心"),
    ListItemData(Icons.Filled.Settings, "设置")
)

@Composable
fun HybridList() {
    LazyColumn(modifier = Modifier.fillMaxSize().displayCutoutPadding()) {
        items(sampleData) { item ->
            ListItemWithIcon(item)
        }
    }
}

@Composable
fun ListItemWithIcon(itemData: ListItemData) {

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Icon(
            imageVector = itemData.icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp), tint = LocalContentColor.current
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = itemData.text, style = MaterialTheme.typography.bodyMedium)
    }
}
