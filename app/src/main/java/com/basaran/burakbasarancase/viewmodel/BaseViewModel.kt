package com.basaran.burakbasarancase.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basaran.burakbasarancase.R
import com.basaran.burakbasarancase.common.UIState
import com.basaran.burakbasarancase.data.model.Products
import com.basaran.burakbasarancase.data.model.ProductsBasket
import com.basaran.burakbasarancase.data.model.ProductsFav
import com.basaran.burakbasarancase.repo.ProductDbRepo
import com.basaran.burakbasarancase.repo.ProductRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
    private val dbRepo: ProductDbRepo,
    private val mRepo: ProductRepo
) : ViewModel() {
    var _favProductsList = MutableLiveData<MutableList<ProductsFav>>()
    val favProductsList: MutableLiveData<MutableList<ProductsFav>> get() = _favProductsList

    var _basketList = MutableLiveData<MutableList<ProductsBasket>>()
    val basketList: MutableLiveData<MutableList<ProductsBasket>> get() = _basketList

    var _products = MutableLiveData<List<Products>>()
    val products: LiveData<List<Products>> get() = _products

    private val _state = MutableLiveData<UIState<Int>?>()
    val state: LiveData<UIState<Int>?> = _state

    init {
        loadFavProducts()
        loadBasketProducts()
        getProducts()
        _products = mRepo.getProducts()
        _favProductsList = dbRepo.getFav()
        _basketList = dbRepo.getBasket()
    }

    fun getProducts() {
        _state.value = UIState.Loading(R.string.loading)
        viewModelScope.launch(Dispatchers.IO) {
            val isSuccess = mRepo.fetchProducts()
            if (isSuccess) {
                _state.postValue(UIState.Success)
            } else {
                _state.postValue((UIState.Error(R.string.error)))
            }
        }
    }
    fun refreshProducts() {
        getProducts()
    }
    fun loadFavProducts() {
        dbRepo.allFavData()
    }
    fun loadBasketProducts() {
        dbRepo.allBasketData()
    }

    fun insertFavProducts(productFav: ProductsFav) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepo.insertFavProducts(productFav)
            }
        }
    }

    fun deleteFavProducts(productFav: ProductsFav) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepo.deleteFavProducts(productFav)
            }
        }
    }

    fun insertBasketItem(productsBasket: ProductsBasket) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepo.insertBasketProducts(productsBasket)
            }
        }
    }

    fun deleteBasketItem(productsBasket: ProductsBasket) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepo.deleteBasketProducts(productsBasket)
            }
        }
    }

    fun updateBasketItem(productsBasket: ProductsBasket) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepo.updateBasketProducts(productsBasket)
            }
        }
    }

    fun allDeleteBasket() {
        val cartItemsList = _basketList.value ?: emptyList()
        for (cardItem in cartItemsList) {
            dbRepo.deleteBasketProducts(cardItem)
        }
        loadBasketProducts()
    }

    fun productToFavoriteProduct(product: Products): ProductsFav {
        return ProductsFav(
            product.id!!.toInt(),
            product.createdAt,
            product.name,
            product.image,
            product.price,
            product.description,
            product.model,
            product.brand
        )
    }

    fun productToBasketProduct(product: Products): ProductsBasket {
        return ProductsBasket(
            product.id!!.toInt(),
            product.createdAt,
            product.name,
            product.image,
            product.price,
            product.description,
            product.model,
            product.brand
        )
    }

    fun productFavToBasketProduct(product: ProductsFav): ProductsBasket {
        return ProductsBasket(
            product.id!!.toInt(),
            product.createdAt,
            product.name,
            product.image,
            product.price,
            product.description,
            product.model,
            product.brand
        )
    }
}