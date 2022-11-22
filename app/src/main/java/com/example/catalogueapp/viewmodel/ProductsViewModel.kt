package com.example.catalogueapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogueapp.model.Product
import com.example.catalogueapp.network.NetworkResult
import com.example.catalogueapp.network.NetworkService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val networkService: NetworkService) :
    ViewModel() {

    var state by mutableStateOf<Resource<List<Product>>>(Resource.Loading)
        private set

    fun fetchProducts(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            state = Resource.Loading

            val result = if (category.isNotEmpty()) {
                networkService.getProductsByCategory(category)
            } else {
                networkService.getProducts()
            }

            when (result) {
                is NetworkResult.Success -> {
                    state = Resource.Success(result.data.sortedByDescending { it.rating.rate })
                }
                is NetworkResult.Error -> {
                    state = Resource.Error("Error loading products")
                }
            }
        }
    }
}