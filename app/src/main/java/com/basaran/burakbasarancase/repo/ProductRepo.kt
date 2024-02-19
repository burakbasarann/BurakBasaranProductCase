package com.basaran.burakbasarancase.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.basaran.burakbasarancase.data.model.Products
import com.basaran.burakbasarancase.data.network.ProductDataAccessService
import javax.inject.Inject

class ProductRepo @Inject constructor(private val apiService: ProductDataAccessService) {
    private val productsList = MutableLiveData<List<Products>>()

    fun getProducts(): MutableLiveData<List<Products>> {
        return productsList
    }


    suspend fun fetchProducts(): Boolean {
        return try {
            val response = apiService.getAllProducts()
            if (response.isSuccessful) {
                val products = response.body() as List<Products>
                productsList.postValue(products)
                Log.d("ProductsDaoRepo", "Products fetched successfully.")
                true
            } else {
                Log.e("ProductsDaoRepo", "Failed to fetch products")
                false
            }
        } catch (e: Exception) {
            Log.e("ProductsDaoRepo", "Error fetching products:", e)
            false
        }
    }
}