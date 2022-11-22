package com.example.catalogueapp.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.catalogueapp.ui.FADE_ANIM_DURATION
import com.example.catalogueapp.ui.ScaleInAnimation
import com.example.catalogueapp.viewmodel.CategoriesViewModel
import com.example.catalogueapp.viewmodel.Resource
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

// use empty string "" for "all categories"
data class ProductFilter(var category: String, var priceRange: String?)

@Composable
fun FilterDialog(
    padding: PaddingValues,
    filter: ProductFilter,
    viewModel: CategoriesViewModel = hiltViewModel(),
    onFilterApplied: (ProductFilter) -> Unit,
    onDialogDismissed: () -> Unit,
) {
    Timber.d("Dialog recompose")
    val coroutineScope = rememberCoroutineScope()

    var currentFilter: ProductFilter by remember { mutableStateOf(filter) }
    var dialogVisible by remember { mutableStateOf(false) }

    val showDialog: () -> Unit = {
        coroutineScope.launch {
            delay(FADE_ANIM_DURATION) // wait for dialog to construct
            dialogVisible = true
        }
    }

    val dismissDialog: () -> Unit = {
        coroutineScope.launch {
            dialogVisible = false
            delay(FADE_ANIM_DURATION) // wait for animation to finish
            onDialogDismissed()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchCategories()
        showDialog()
    }

//    Dialog(onDismissRequest = dismissDialog) {
    ScaleInAnimation(dialogVisible) {
        DialogScaffold(
            padding,
            currentFilter,
            onFilterApplied,
            dismissDialog
        ) { padding ->
            when (viewModel.state) {
                Resource.Loading -> LoadingState()

                is Resource.Error -> {} // TODO handle error
                is Resource.Success -> {
                    viewModel.state.let {

                        val categories = remember { (it as Resource.Success).data }
                        Column(
                            Modifier
                                .verticalScroll(rememberScrollState())
                                .fillMaxSize()
                                .padding(10.dp)
                        ) {
                            PriceRangeChips(currentFilter.priceRange) { range ->
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
//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogScaffold(
    padding: PaddingValues,
    currentFilter: ProductFilter,
    onFilterApplied: (ProductFilter) -> Unit,
    onDialogDismissed: () -> Unit,
    content: @Composable (padding: PaddingValues) -> Unit
) {
    Scaffold(modifier = Modifier
        .fillMaxSize()
        .padding(padding),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceRangeChips(
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
                    labelColor = if (currentRange == range) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
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

        val catMap = categories.map {
            it to it
        }.toMap().toMutableMap()

        // add "all categories"
        catMap.put("All categories", "")

        catMap.forEach { entry ->
            CategoryOption(entry.key, selected = entry.value == currentCategory) {
                onCategoryClicked(entry.value)
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