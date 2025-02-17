package com.unclled.newsviewer.features.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsDao {
    @Query("SELECT * FROM news")
    fun getSavedNews(): LiveData<List<NewsEntity>>

    @Query("SELECT EXISTS(SELECT * FROM news WHERE title = :title)")
    suspend fun isNewsExists(title: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNews(news: NewsEntity): Long

    suspend fun saveNewsIfNotExists(news: NewsEntity): Boolean {
        return if (!isNewsExists(news.title ?: "")) {
            insertNews(news) > 0
        } else {
            false
        }
    }

    @Query("DELETE FROM NEWS WHERE title = :title")
    fun deleteNews(title: String)

    @Query("SELECT * FROM news WHERE title LIKE '%' || :title || '%'")
    fun getNewsByTitle(title: String) : LiveData<List<NewsEntity>>
}