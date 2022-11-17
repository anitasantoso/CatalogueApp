package com.example.catalogueapp.network

import com.example.catalogueapp.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class NetworkService @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getProducts(): NetworkResult<List<Product>> {
        return withContext(Dispatchers.IO) {
            doApiCall { apiService.getProducts() }
        }
    }

    suspend fun getProductsByCategory(category: String): NetworkResult<List<Product>> {
        return withContext(Dispatchers.IO) {
            doApiCall { apiService.getProductsByCategory(category) }
        }
    }

    suspend fun getCategories() : NetworkResult<List<String>> {
        return withContext(Dispatchers.IO) {
            doApiCall { apiService.getCategories() }
        }
    }

    private suspend fun <T> doApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body)
                }
            }
            return NetworkResult.Error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: e.toString())
        }
    }
}

sealed class NetworkResult<T>() {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error<T>(val message: String, val data: T? = null) : NetworkResult<T>()
}