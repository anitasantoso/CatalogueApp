package com.example.catalogueapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.catalogueapp.R
import com.example.catalogueapp.model.Product
import com.example.catalogueapp.ui.BackButton

@Composable
fun DetailScreen(padding: PaddingValues, product: Product?) {
    product?.let {
        Surface(Modifier.padding(padding)) {
            var state = rememberScrollState(0)
            Column(
                modifier = Modifier
                    .verticalScroll(state)
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                BackButton()
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.image)
                        .crossfade(true)
                        .size(300)
                        .placeholder(R.drawable.placeholder)
                        .build(),
                    "",
                    Modifier
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.FillBounds
                )
                Text(
                    product.title,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    product.price.toString(),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    product.description,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

//@Composable
//@Preview(
//    name = "detail_phone",
//    showSystemUi = true,
//    showBackground = true,
//    device = Devices.PHONE
//)
//fun DetailPhonePreview(@PreviewParameter(ProductDataProvider::class) product: Product) {
//    DetailScreen(PaddingValues(0.dp), product)
//}
//
//@Composable
//@Preview(
//    name = "detail_tablet",
//    showSystemUi = true,
//    showBackground = true,
//    device = Devices.TABLET
//)
//fun DetailTabletPreview(@PreviewParameter(ProductDataProvider::class) product: Product) {
//    DetailScreen(PaddingValues(0.dp), product)
//}