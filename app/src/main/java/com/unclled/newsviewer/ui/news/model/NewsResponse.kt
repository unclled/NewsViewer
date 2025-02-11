package com.unclled.newsviewer.ui.news.model

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<News>
)