package com.unclled.newsviewer.ui.news.model

data class News(
    val source: Source,
    val author: String?,
    var title: String,
    val description: String?,
    val url: String,
    val urlToImage: String,
    var publishedAt: String,
    var content: String,
)
