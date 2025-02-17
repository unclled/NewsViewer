package com.unclled.newsviewer.features.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SavedNewsViewModel(application: Application) : AndroidViewModel(application) {
    val newsList: LiveData<List<NewsEntity>>
    private val repository: NewsRepository

    init {
        val newsDb = NewsDatabase.getInstance(application)
        val newsDao = newsDb.newsDao()
        repository = NewsRepository(newsDao)
        newsList = repository.newsList
    }

    fun saveNews(news: NewsEntity): Boolean {
        return try {
            viewModelScope.launch {
                repository.saveNews(news)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun isNewsSaved(title: String): Boolean {
        return repository.isNewsExist(title)
    }

    fun deleteNews(newsEntity: String) {
        repository.deleteNews(newsEntity)
    }

    fun getNewsByTitle(title: String): LiveData<List<NewsEntity>> {
        return repository.getNewsByTitle(title)
    }

}