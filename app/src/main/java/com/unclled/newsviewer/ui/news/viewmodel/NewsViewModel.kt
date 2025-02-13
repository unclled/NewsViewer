package com.unclled.newsviewer.ui.news.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.unclled.newsviewer.features.api_requests.NewsApiService
import com.unclled.newsviewer.features.api_requests.Requests
import com.unclled.newsviewer.ui.news.model.News
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class NewsViewModel : ViewModel() {
    private val _newsList = mutableStateListOf<News>()
    val newsList: List<News> = _newsList
    var selectedCategory = mutableStateOf("entertainment")

    fun updateCategory(newCategory: String, context: Context) {
        selectedCategory.value = newCategory
        saveSelectedCategory(newCategory, context)
    }

    fun loadCategory(context: Context) {
        selectedCategory.value = getSelectedCategory(
            "selected_category",
            "entertainment",
            context
        )
    }
    fun fetchNews(category: String, q: String) {
        try {
            val requests = Requests(NewsApiService.create())
            requests.sendRequest(category, q)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ newsResponse ->
                    _newsList.clear()
                    _newsList.addAll(newsResponse.articles)
                    removeRedundantInfo(_newsList)
                }, { error ->
                    if (error is HttpException) {
                        val errorBody = error.response()?.errorBody()?.string()
                        Log.e("Retrofit", "Error body = $errorBody")
                        Log.e("Retrofit", "Response code = ${error.code()}")
                    }
                    error.printStackTrace()
                })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun saveSelectedCategory(category: String, context: Context) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("selected_category", category)
        editor.apply()
    }

    fun getSelectedCategory(key: String, defaultValue: String, context: Context): String {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    private fun removeRedundantInfo(newsList: List<News>): List<News> {
        newsList.forEach { news ->
            news.title = clearTitle(news.title)
            news.publishedAt = formatDate(news.publishedAt)
        }
        return newsList
    }

    private fun formatDate(publishedAt: String): String {
        val instant = Instant.parse(publishedAt)
        val zonedDateTime = instant.atZone(ZoneId.of("UTC-3"))
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yy 'at' HH:mm")
        return zonedDateTime.format(formatter)
    }

    private fun clearTitle(title: String): String {
        var index = title.length
        for (i in index - 1 downTo 1) {
            if (title[i] == '-')
                break
            index = i
        }
        return title.substring(0, index - 1)
    }
}