package com.unclled.newsviewer.features.database

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val publisher: String,
    val author: String?,
    val title: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?
)