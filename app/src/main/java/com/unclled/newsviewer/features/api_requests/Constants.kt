package com.unclled.newsviewer.features.api_requests

import com.unclled.newsviewer.BuildConfig

open class Constants(
    val apiKey: String = BuildConfig.NEWS_API_KEY,
    val baseUrl: String = "https://newsapi.org/",
    val category: Array<String> = arrayOf("business", "entertainment", "general", "health", "science", "sports", "technology")
)
