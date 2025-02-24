package com.unclled.newsviewer.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.unclled.newsviewer.features.database.SavedNewsViewModel
import com.unclled.newsviewer.ui.news.view.CategoryPickerDialog
import com.unclled.newsviewer.ui.news.view.NewsPage
import com.unclled.newsviewer.ui.profile.ProfilePage
import com.unclled.newsviewer.ui.saved_news.view.SavedNewsPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    vm: SavedNewsViewModel = viewModel(),
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    onIndexChange: (Int) -> Unit,
    onSearch: (String, Int) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val showCategoryPicker = remember { mutableStateOf(false) }

    val navItemList = listOf(
        NavItem("Saved", Icons.Default.Star, 0),
        NavItem("News", Icons.Default.Info, 0),
        NavItem("Profile", Icons.Default.AccountCircle, 0),
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(16, 16, 16, 255)),
        topBar = {
            TopAppBar(
                modifier = modifier.fillMaxHeight(0.123f),
                colors = TopAppBarColors(
                    containerColor = Color(146, 178, 238, 255),
                    actionIconContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    scrolledContainerColor = Color(16, 16, 16, 255),
                    titleContentColor = Color.White
                ),
                title = { Text("") },
                actions = {
                    SearchBar(
                        modifier = Modifier
                            .fillMaxWidth(0.86f)
                            .height(52.dp)
                            .align(Alignment.CenterVertically),
                        query = searchQuery,
                        onQueryChange = { newValue -> searchQuery = newValue },
                        onSearch = {
                            onSearch(searchQuery, selectedIndex)
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
                    IconButton(
                        onClick = { showCategoryPicker.value = true },
                        modifier.padding(start = 4.dp)
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(146, 178, 238, 255)
            ) {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            onIndexChange(index)
                        },
                        label = {
                            Text(text = navItem.label, color = Color.White)
                        },
                        icon = {
                            BadgedBox(badge = {
                                if (navItem.badgeCount > 0)
                                    Badge {
                                        Text(
                                            text = navItem.badgeCount.toString(),
                                            color = Color.White
                                        )
                                    }
                            }) {
                                Icon(
                                    imageVector = navItem.icon,
                                    contentDescription = "Icon",
                                    tint = Color.White
                                )
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            ContentScreen(vm, selectedIndex, onSearch)
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
fun ContentScreen(
    vm: SavedNewsViewModel = viewModel(),
    selectedIndex: Int,
    onSearch: (String, Int) -> Unit
) {
    when (selectedIndex) {
        0 -> SavedNewsPage(vm, { query -> onSearch(query, selectedIndex) })
        1 -> NewsPage(vm, { query -> onSearch(query, selectedIndex) })
        2 -> ProfilePage()
    }
}