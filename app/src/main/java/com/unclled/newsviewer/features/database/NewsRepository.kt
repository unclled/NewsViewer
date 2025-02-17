package com.unclled.newsviewer.features.database

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsRepository(private val newsDao: NewsDao) {
    private val coroutine = CoroutineScope(Dispatchers.Main)

    val newsList: LiveData<List<NewsEntity>> = newsDao.getSavedNews()

    suspend fun saveNews(news: NewsEntity): Boolean {
        return newsDao.saveNewsIfNotExists(news)
    }

    suspend fun isNewsExist(title: String): Boolean {
        return newsDao.isNewsExists(title)
    }

    fun deleteNews(title: String) {
        coroutine.launch(Dispatchers.IO) {
            newsDao.deleteNews(title)
        }
    }

    fun getNewsByTitle(title: String): LiveData<List<NewsEntity>> {
        return newsDao.getNewsByTitle(title)
    }
}