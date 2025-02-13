package com.unclled.newsviewer.features.api_requests

open class Constants(
    val apiKey: String = "f711cd4b62a14e4fa706aad3dbab1ece",
    val baseUrl: String = "https://newsapi.org/",
    val category: Array<String> = arrayOf("business", "entertainment", "general", "health", "science", "sports", "technology")
)
