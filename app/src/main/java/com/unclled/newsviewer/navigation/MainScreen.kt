package com.unclled.newsviewer.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.unclled.newsviewer.ui.news.view.CategoryPickerDialog
import com.unclled.newsviewer.ui.news.view.NewsPage
import com.unclled.newsviewer.ui.profile.ProfilePage
import com.unclled.newsviewer.ui.saved_news.SavedNewsPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val showCategoryPicker = remember { mutableStateOf(false) }

    val navItemList = listOf(
        NavItem("Saved", Icons.Default.Star, 0),
        NavItem("News", Icons.Default.Info, 0),
        NavItem("Profile", Icons.Default.AccountCircle, 0),
    )

    var selectedIndex by remember { mutableIntStateOf(1) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("") },
                actions = {
                    SearchBar(
                        modifier = modifier.size(340.dp),
                        query = searchQuery,
                        onQueryChange = { newValue -> searchQuery = newValue },
                        onSearch = {
                            //запустить поиск
                            expanded = false
                        },
                        active = expanded,
                        onActiveChange = { expanded = it },
                        placeholder = {
                            Text("Find news by keyword")
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null
                            )
                        },
                    ) {}
                    IconButton (
                        onClick = { showCategoryPicker.value = true },
                        modifier.padding(start = 4.dp)
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        label = {
                            Text(text = navItem.label)
                        },
                        icon = {
                            BadgedBox(badge = {
                                if (navItem.badgeCount > 0)
                                    Badge {
                                        Text(text = navItem.badgeCount.toString())
                                    }
                            }) {
                                Icon(imageVector = navItem.icon, contentDescription = "Icon")
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            ContentScreen(modifier = Modifier.padding(innerPadding), selectedIndex)
        }
    }
    if (showCategoryPicker.value) {
        CategoryPickerDialog(
            onDismiss = { showCategoryPicker.value = false },
            showCategoryPicker = { isVisible -> showCategoryPicker.value = isVisible }
        )
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex: Int) {
    when (selectedIndex) {
        0 -> SavedNewsPage()
        1 -> NewsPage()
        2 -> ProfilePage()
    }
}