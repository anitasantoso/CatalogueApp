package com.example.catalogueapp.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.catalogueapp.viewmodel.CategoriesViewModel
import com.example.catalogueapp.viewmodel.Resource

@Composable
fun CategoriesScreen(
    padding: PaddingValues,
    viewModel: CategoriesViewModel = hiltViewModel(),
    onCategoryClick: (String) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.fetchCategories()
    }

    when (viewModel.state) {
        Resource.Loading -> LoadingState()
        is Resource.Error -> {} // TODO handle error
        is Resource.Success -> {
            viewModel.state.let {
                val categories = (it as Resource.Success).data
                Box(modifier = Modifier.padding(padding)) {
                    LazyColumn(contentPadding = PaddingValues(10.dp)) {
                        items(categories.size) { index ->
                            val cat = categories.get(index)
                            CategoryListItem(cat, onCategoryClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryListItem(cat: String, onCategoryClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable {
                onCategoryClick(cat)
            },
        border = BorderStroke(1.dp, Color.Gray),
        colors = CardDefaults.outlinedCardColors(),
        elevation = CardDefaults.outlinedCardElevation()
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = cat,
            style = MaterialTheme.typography.titleSmall
        )
    }
}