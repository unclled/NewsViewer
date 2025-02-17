package com.unclled.newsviewer

import android.app.Application
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.unclled.newsviewer.features.database.SavedNewsViewModel
import com.unclled.newsviewer.navigation.MainScreen
import com.unclled.newsviewer.ui.news.viewmodel.NewsViewModel


class NewsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SavedNewsViewModel::class.java) -> {
                SavedNewsViewModel(application) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val owner = LocalViewModelStoreOwner.current
            owner?.let {
                val viewModel: SavedNewsViewModel = viewModel(
                    it,
                    "SavedNewsViewModel",
                    NewsViewModelFactory(application) // not LocalContext.current.applicationContext
                )
                App(viewModel)
            }
        }
    }
}

@Composable
fun App(viewModel: SavedNewsViewModel) {
    val newsViewModel: NewsViewModel = viewModel()
    val savedNewsViewModel: SavedNewsViewModel = viewModel()

    var selectedIndex by remember { mutableIntStateOf(1) }

    MainScreen(
        vm = viewModel,
        selectedIndex = selectedIndex,
        onSearch = { query, index ->
            when (index) {
                0 -> {
                    savedNewsViewModel.getNewsByTitle(query)
                }
                1 -> {
                    newsViewModel.fetchNews(newsViewModel.selectedCategory.value, query)
                }
            }
        },
        onIndexChange = { index -> selectedIndex = index }
    )
}