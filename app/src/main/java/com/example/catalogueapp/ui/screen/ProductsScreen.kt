package com.example.catalogueapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.catalogueapp.R
import com.example.catalogueapp.model.Product
import com.example.catalogueapp.viewmodel.ProductsViewModel
import com.example.catalogueapp.viewmodel.Resource
import timber.log.Timber

@Composable
fun ProductsScreen(
    padding: PaddingValues,
    category: String,
    onItemClick: (Product) -> Unit,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    Timber.d("Products Recompose")

    // must survive recomposition and screen rotation
    var showDialog by rememberSaveable { mutableStateOf(false) }

    // TODO move to viewmodel to survive rotation
    var filter by remember { mutableStateOf(ProductFilter("", null)) }

    LaunchedEffect(Unit) {
        viewModel.fetchProducts(category)
    }

    when (viewModel.state) {
        Resource.Loading -> {
            LoadingState()
        }
        is Resource.Error -> {} // TODO handle error
        is Resource.Success -> {
            viewModel.state.let {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(padding)
                ) {
                    LazyVerticalGrid(columns = GridCells.Adaptive(120.dp),
                        content = {
                            item(span = {
                                GridItemSpan(this.maxLineSpan)
                            }, content = {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Spacer(modifier = Modifier.weight(1f))
                                    IconButton(onClick = {
                                        showDialog = true
                                    }) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_baseline_filter_list_24),
                                            contentDescription = "Filter"
                                        )
                                    }
                                }
                            })

                            val products = (it as Resource.Success).data
                            items(count = products.size, key = {
                                products.get(it).id
                            }) { index ->
                                val product = products.get(index)
                                CatalogueGridCell(product, onItemClick)
                            }
                        })
                }

                if (showDialog) {
                    FilterDialog(padding, filter, onFilterApplied = { newFilter ->
                        showDialog = false // close
                        filter = newFilter
                        viewModel.fetchProducts(filter.category)

                    }, onDialogDismissed = {
                        showDialog = false
                    })
                }
            }
        }
    }
}

@Composable
fun CatalogueGridCell(product: Product, onItemClick: (Product) -> Unit) {
    Card(modifier = Modifier
        .fillMaxSize()
        .clickable { onItemClick(product) }
        .padding(5.dp)
        .defaultMinSize(minHeight = 170.dp)
        .width(intrinsicSize = IntrinsicSize.Max)
        .height(intrinsicSize = IntrinsicSize.Max),
        colors = CardDefaults.outlinedCardColors(Color.White),
        elevation = CardDefaults.elevatedCardElevation()) {

        Column(
            modifier = Modifier
                .fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .weight(4.0f, true)
            ) {
                AsyncImage(
                    ImageRequest.Builder(LocalContext.current)
                        .data(product.image)
                        .crossfade(true)
                        .build(),
                    product.title
                )
            }

            Column(
                modifier = Modifier
                    .weight(1.0f, true)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = product.title.trim(),
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}