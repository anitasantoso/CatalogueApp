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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.catalogueapp.model.Product
import com.example.catalogueapp.viewmodel.ProductsViewModel
import com.example.catalogueapp.viewmodel.Resource
import timber.log.Timber

@Composable
fun ProductsScreen(
    padding: PaddingValues,
    category: String?,
    onItemClick: (Product) -> Unit,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    Timber.d("Products Recompose")

    // must survive recomposition and screen rotation
    var showDialog by rememberSaveable { mutableStateOf(false) }

    // TODO custom saver
    var filter by remember { mutableStateOf(ProductFilter(null, null)) }

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
                val products = (it as Resource.Success).data

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(padding)
                ) {
                    LazyVerticalGrid(columns = GridCells.Fixed(3),
                        content = {
                            item(span = {
                                GridItemSpan(this.maxLineSpan)
                            }, content = {
                                OutlinedButton(onClick = {
                                    showDialog = true
                                }) {
                                    Text("Filter")
                                }
                            })
                            items(count = products.size, key = {
                                products.get(it).id
                            }) { index ->
                                val product = products.get(index)
                                CatalogueGridCell(product, onItemClick)
                            }
                        })
                }

                if (showDialog) {
                    FilterDialog(filter, onFilterApplied = { newFilter ->
                        showDialog = false // close
                        filter = newFilter // TODO update products
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
        .padding(5.dp)
        .clickable { onItemClick(product) }
        .height(150.dp),
        colors = CardDefaults.elevatedCardColors(),
        elevation = CardDefaults.elevatedCardElevation()) {

        Column(verticalArrangement = Arrangement.SpaceBetween) {
            AsyncImage(
                ImageRequest.Builder(LocalContext.current)
                    .data(product.image)
                    .size(300)
                    .crossfade(true)
                    .build(),
                product.title,
                Modifier.align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Fit
            )
            Box(
                modifier = Modifier
                    .weight(1.0f, false)
                    .height(50.dp)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.TopCenter),
                    text = product.title,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}