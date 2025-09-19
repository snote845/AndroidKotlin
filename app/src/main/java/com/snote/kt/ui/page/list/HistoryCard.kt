package com.snote.kt.ui.page.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.snote.kt.network.model.HistoryItem

/**
 * @author snote
 * @date 2025/9/17 17:38
 * @description 图文混排的组件
 */
@Composable
fun ArticleCard(article: HistoryItem, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .displayCutoutPadding()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "${article.title} (${article.year})",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.heightIn(8.dp))
                Text(text = article.description, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.heightIn(4.dp))
                Text(
                    text = "类型: ${
                        when (article.event_type) {
                            "birth" -> "出生"
                            "death" -> "逝世"
                            "event" -> "事件"
                            else -> article.event_type
                        }
                    }",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

    }

}

@Composable
@Preview
fun TestPreview() {
    ArticleCard(
        article = HistoryItem(
            title = "英国首相罗伯特·沃波尔出生",
            year = "1676",
            description = "罗伯特·沃波尔，第一代奥福德伯爵，KG，KB，PC（Robert Walpole, 1st Earl of Orford，1676年8月26日－1745年3月18日，又译罗伯特·华尔波尔），英国辉格党政治家，罗伯特·沃波尔爵士（Sir Robert Walpole）是他在1742年以前更为人所知的名称。",
            event_type = "birth",
            link = "https://baike.baidu.com/item/%E7%BD%97%E4%BC%AF%E7%89%B9%C2%B7%E6%B2%83%E6%B3%A2%E5%B0%94"
        ), onClick = {
            println("click")
        }
    )
}