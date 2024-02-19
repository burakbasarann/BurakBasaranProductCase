package com.basaran.burakbasarancase.viewmodel

import androidx.lifecycle.MutableLiveData
import com.basaran.burakbasarancase.data.model.FilterOptions
import com.basaran.burakbasarancase.data.model.Products
import com.basaran.burakbasarancase.data.model.ProductsBasket
import com.basaran.burakbasarancase.data.model.ProductsFav
import com.basaran.burakbasarancase.repo.ProductDbRepo
import com.basaran.burakbasarancase.repo.ProductRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(productRepo: ProductRepo, productDbRepo: ProductDbRepo) :
    BaseViewModel(productDbRepo, productRepo) {

    val filteredList = MutableLiveData<List<Products>?>()

    fun isFavorite(product: ProductsFav): Boolean {
        return _favProductsList.value?.contains(product) ?: false
    }

    fun isBasket(product: ProductsBasket): Boolean {
        return _basketList.value?.contains(product) ?: false
    }

    fun searchProducts(query: String) {
        val filteredProducts = _products.value?.filter {
            it.name?.contains(query, ignoreCase = true) ?: false
        }
        filteredList.value = filteredProducts
    }

    fun applyFilter(filterOptions: FilterOptions) {
        val filteredProducts = _products.value?.filter { product ->
            val priceDouble = product.price?.toDoubleOrNull() ?: 0.0
            val priceMatches = filterOptions.minPrice <= priceDouble && priceDouble <= filterOptions.maxPrice
            if (!filterOptions.brands.contains("Please Select Brand")) {
                val brandMatches = filterOptions.brands.contains(product.brand!!)
                priceMatches && brandMatches
            }else {
                priceMatches
            }
        }
        filteredList.value = filteredProducts
    }
}