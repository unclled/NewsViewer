package com.unclled.newsviewer.features.api_requests

import com.unclled.newsviewer.ui.news.model.NewsResponse
import io.reactivex.Observable

open class Requests(
    private val apiService: NewsApiService
) : Constants() {
    open fun sendRequest(category: String, keyword: String = "", pageSize: Int = 50, page: Int = 1): Observable<NewsResponse> {
        return apiService.getTopHeadlines(
            category = category,
            keyword = keyword,
            pageSize = pageSize,
            page = page,
            apiKey = apiKey)
    }

}