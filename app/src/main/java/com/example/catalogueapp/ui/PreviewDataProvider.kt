package com.example.catalogueapp.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.catalogueapp.model.Product
import com.example.catalogueapp.model.Rating

val MockProducts = listOf(
    Product(
        1L,
        "Product 1",
        5.00,
        "Description",
        "Category",
        "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_.jpg",
        Rating(4.5, 20)
    ),
    Product(
        2L,
        "Product 2",
        5.00,
        "Description",
        "Category",
        "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_.jpg",
        Rating(4.5, 20)
    ),
    Product(
        3L,
        "Product 3",
        5.00,
        "Description",
        "Category",
        "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_.jpg",
        Rating(4.5, 20)
    ),
    Product(
        4L,
        "Product 4",
        5.00,
        "Description",
        "Category",
        "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_.jpg",
        Rating(4.5, 20)
    )
)

class ProductsDataProvider : PreviewParameterProvider<List<Product>> {
    override val values = sequenceOf(
        MockProducts
    )
}


class ProductDataProvider : PreviewParameterProvider<Product> {
    override val values = sequenceOf(MockProducts.get(0))
}
