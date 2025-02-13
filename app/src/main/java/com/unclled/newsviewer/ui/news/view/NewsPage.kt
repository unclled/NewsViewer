package com.unclled.newsviewer.ui.news.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.unclled.newsviewer.features.database.NewsEntity
import com.unclled.newsviewer.features.database.SavedNewsViewModel
import com.unclled.newsviewer.ui.news.viewmodel.NewsViewModel
import com.unclled.newsviewer.ui.web_view.WebViewScreen


@Composable
fun NewsPage(
    vm: SavedNewsViewModel = viewModel(),
    modifier: Modifier = Modifier,
    viewModel: NewsViewModel = viewModel()
) {
    val context = LocalContext.current
    val shape = RoundedCornerShape(8.dp)
    val isRefreshing = remember { mutableStateOf(false) }
    val showWebView = remember { mutableStateOf(false) }
    val webViewUrl = remember { mutableStateOf("") }
    var isLoaded = false

    val isLoading = remember { mutableStateOf(true) }
    val isSavedMap = remember { mutableStateMapOf<String, Boolean>() }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing.value),
        onRefresh = {
            isRefreshing.value = true
            viewModel.fetchNews(
                viewModel.selectedCategory.value,
                ""
            )
            isRefreshing.value = false
            isLoading.value = false
        },
    ) {
        if (showWebView.value) {
            WebViewScreen(webViewUrl.value)
        } else {
            Column(
                modifier.background(Color(16, 16, 16, 255)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LaunchedEffect(Unit) {
                    if (!isLoaded) {
                        viewModel.loadCategory(context)
                        viewModel.fetchNews(
                            viewModel.selectedCategory.value,
                            ""
                        )
                        isLoading.value = true
                        isLoaded = true
                        isLoading.value = false
                    }
                }

                if (isLoading.value) {
                    Text(
                        text = "Receiving fresh news...",
                        color = Color.White,
                        fontSize = 26.sp,
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Center,
                    )
                } else {
                    LazyColumn(modifier = modifier.padding(start = 6.dp, end = 6.dp)) {
                        items(viewModel.newsList) { newsItem ->
                            val isSaved = remember { mutableStateOf(false) }

                            LaunchedEffect(newsItem.title) {
                                isSaved.value = vm.isNewsSaved(newsItem.title)
                            }

                            Column(
                                modifier = Modifier.padding(14.dp),
                            ) {
                                Box(
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .clip(shape)
                                        .clickable {
                                            webViewUrl.value = newsItem.url
                                            showWebView.value = true
                                        },
                                    contentAlignment = Alignment.BottomCenter
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(newsItem.urlToImage),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(shape)
                                    )
                                    Box(
                                        modifier = modifier
                                            .fillMaxSize()
                                            .background(
                                                Brush.verticalGradient(
                                                    colors = listOf(
                                                        Color.Transparent,
                                                        Color.Black.copy(alpha = 0.2f),
                                                        Color.Black.copy(alpha = 0.4f),
                                                        Color.Black.copy(alpha = 0.6f),
                                                        Color.Black.copy(alpha = 0.8f)
                                                    ),
                                                    startY = 100f,
                                                    endY = 320f
                                                )
                                            )
                                    )
                                    Text(
                                        newsItem.title,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        style = MaterialTheme.typography.labelLarge,
                                        textAlign = TextAlign.Left,
                                        modifier = modifier
                                            .padding(start = 6.dp, end = 6.dp, bottom = 6.dp)
                                    )
                                    IconButton(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(end = 12.dp, top = 12.dp)
                                            .size(34.dp),
                                        onClick = {
                                            isSaved.value = vm.saveNews(
                                                NewsEntity(
                                                    publisher = newsItem.source.name,
                                                    author = newsItem.author
                                                        ?: newsItem.source.name,
                                                    title = newsItem.title,
                                                    url = newsItem.url,
                                                    urlToImage = newsItem.urlToImage ?: "",
                                                    publishedAt = newsItem.publishedAt
                                                )
                                            )
                                            isSavedMap[newsItem.title] = isSaved.value
                                        },
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Star,
                                            contentDescription = null,
                                            tint = if (isSaved.value) Color(
                                                255,
                                                215,
                                                0
                                            ) else Color.White
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 6.dp, start = 6.dp, end = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Box {
                                        Icon(
                                            Icons.Filled.AccountCircle,
                                            contentDescription = null,
                                            tint = Color(255, 255, 255, 20)
                                        )
                                        Text(
                                            newsItem.author ?: newsItem.source.name,
                                            color = Color.LightGray,
                                            fontSize = 10.sp,
                                            style = MaterialTheme.typography.labelSmall,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                    }
                                    Box {
                                        Icon(
                                            Icons.Filled.Place,
                                            contentDescription = null,
                                            tint = Color(255, 255, 255, 20)
                                        )
                                        Text(
                                            newsItem.source.name,
                                            color = Color.LightGray,
                                            fontSize = 10.sp,
                                            style = MaterialTheme.typography.labelSmall,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                    }
                                    Box {
                                        Icon(
                                            Icons.Filled.DateRange,
                                            contentDescription = null,
                                            tint = Color(255, 255, 255, 20)
                                        )
                                        Text(
                                            newsItem.publishedAt,
                                            color = Color.LightGray,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    BackHandler {
        if (showWebView.value) {
            showWebView.value = false
            webViewUrl.value = ""
        }
    }
}

