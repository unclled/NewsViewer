package com.unclled.newsviewer

import android.app.Application
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.unclled.newsviewer.features.database.SavedNewsViewModel
import com.unclled.newsviewer.navigation.MainScreen


class NewsViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SavedNewsViewModel(application) as T
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
                    NewsViewModelFactory(LocalContext.current.applicationContext as Application)

                )
                MainScreen(viewModel)
            }
        }

    }
}