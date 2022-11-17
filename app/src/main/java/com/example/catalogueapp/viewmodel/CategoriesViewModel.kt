package com.example.catalogueapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogueapp.network.NetworkResult
import com.example.catalogueapp.network.NetworkService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(private val networkService: NetworkService) : ViewModel() {

    var state by mutableStateOf<Resource<List<String>>>(Resource.Loading)
        private set

    fun fetchCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            state = Resource.Loading

            val result = networkService.getCategories()
            state = when(result) {
                is NetworkResult.Success -> {
                    Resource.Success(result.data)
                }
                is NetworkResult.Error -> {
                    Resource.Error("Error loading categories")
                }
            }
        }
    }
}