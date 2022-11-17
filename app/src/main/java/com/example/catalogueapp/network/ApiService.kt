package com.example.catalogueapp.network

import com.example.catalogueapp.model.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("/products")
    suspend fun getProducts(): Response<List<Product>>

    @GET("/products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): Response<List<Product>>

    @GET("/products/categories")
    suspend fun getCategories(): Response<List<String>>
}