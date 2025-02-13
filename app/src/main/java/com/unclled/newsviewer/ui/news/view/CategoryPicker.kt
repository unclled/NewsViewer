package com.unclled.newsviewer.ui.news.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.unclled.newsviewer.features.api_requests.Constants
import com.unclled.newsviewer.ui.news.viewmodel.NewsViewModel

@Composable
fun CategoryPicker(
    modifier: Modifier = Modifier,
    showCategoryPicker: (Boolean) -> Unit,
    viewModel: NewsViewModel = viewModel()
) {
    val context = LocalContext.current
    val categories = Constants().category
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(
            viewModel.getSelectedCategory(
                "selected_category",
                "entertainment",
                context
            )
        )
    }

    Column(
        modifier.fillMaxWidth()
    ) {
        categories.forEach { text ->
            Row(
                modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = {
                            onOptionSelected(text)
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = { onOptionSelected(text) }
                )
                Text(
                    text = text
                )
            }
        }
        Row(modifier.align(Alignment.CenterHorizontally)) {
            Button(
                onClick = {
                    viewModel.updateCategory(selectedOption, context)
                    viewModel.fetchNews(selectedOption, "")
                    showCategoryPicker(false)
                }
            ) {
                Text("Save")
            }
        }
    }
}

@Composable
fun CategoryPickerDialog(onDismiss: () -> Unit, showCategoryPicker: (Boolean) -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
        ) {
            Column {
                Text(
                    text = "Select category",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                )
                CategoryPicker(
                    modifier = Modifier.padding(2.dp),
                    showCategoryPicker = showCategoryPicker
                )
            }
        }
    }
}