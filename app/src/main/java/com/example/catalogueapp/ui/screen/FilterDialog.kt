package com.example.catalogueapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.catalogueapp.viewmodel.CategoriesViewModel
import com.example.catalogueapp.viewmodel.Resource
import com.google.accompanist.flowlayout.FlowRow

data class ProductFilter(var category: String?, var priceRange: String?)

@Composable
fun FilterDialog(
    filter: ProductFilter,
    viewModel: CategoriesViewModel = hiltViewModel(),
    onFilterApplied: (ProductFilter) -> Unit,
    onDialogDismissed: () -> Unit,
) {
    var currentFilter: ProductFilter by remember { mutableStateOf(filter) }
    LaunchedEffect(Unit) {
        viewModel.fetchCategories()
    }

    Dialog(onDismissRequest = {
        onDialogDismissed()
    }) {
        DialogScaffold(currentFilter, onFilterApplied, onDialogDismissed = {
            onDialogDismissed()
        }) { padding ->
            when (viewModel.state) {
                Resource.Loading -> LoadingState()

                is Resource.Error -> {} // TODO handle error
                is Resource.Success -> {
                    viewModel.state.let {

                        // categories won't change
                        val categories = rememberSaveable { (it as Resource.Success).data }
                        Column(
                            Modifier
                                .verticalScroll(rememberScrollState())
                                .fillMaxSize()
                                .padding(10.dp)
                        ) {
                            PriceRangeChips(padding, currentFilter.priceRange) { range ->
                                var filter = currentFilter.copy()
                                filter.priceRange = range
                                currentFilter = filter
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            CategoriesList(categories, currentFilter.category) { cat ->
                                var filter = currentFilter.copy()
                                filter.category = cat
                                currentFilter = filter
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogScaffold(
    currentFilter: ProductFilter,
    onFilterApplied: (ProductFilter) -> Unit,
    onDialogDismissed: () -> Unit,
    content: @Composable (padding: PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        onDialogDismissed()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close"
                        )
                    }
                },
                title = {
                    Text(
                        text = "Filter Products",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            onFilterApplied(currentFilter)
                        },
                    ) {
                        Text(
                            text = "APPLY",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            )
        }) { padding -> content(padding) }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun PriceRangeChips(
    padding: PaddingValues,
    currentRange: String?,
    onPriceRangeClicked: (String) -> Unit
) {
    val priceRange = listOf("$20-$30", "$40-$50", "$60-$70")
    Column() {
        Text("Price Range", style = MaterialTheme.typography.titleMedium)
        FlowRow() {
            priceRange.forEach { range ->
                AssistChip(onClick = {
                    onPriceRangeClicked(range)
                }, label = {
                    Text(range)
                }, colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (currentRange == range) MaterialTheme.colorScheme.primary else Color.Transparent,
                )
                )
            }
        }
    }
}

@Composable
fun CategoriesList(
    categories: List<String>,
    currentCategory: String?,
    onCategoryClicked: (String) -> Unit
) {
    Column {
        Text("Category", style = MaterialTheme.typography.titleMedium)
        categories.forEach() { cat ->
            CategoryOption(cat, selected = cat == currentCategory) {
                onCategoryClicked(cat)
            }
        }
    }
}

@Composable
fun CategoryOption(
    text: String,
    selected: Boolean,
    onClickOption: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(top = 14.dp)
            .selectable(selected) { onClickOption() }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(10.dp)
                .weight(1f)
        )
        if (selected) {
            Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}