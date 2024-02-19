package com.basaran.burakbasarancase.viewmodel

import com.basaran.burakbasarancase.data.model.ProductsBasket
import com.basaran.burakbasarancase.repo.ProductDbRepo
import com.basaran.burakbasarancase.repo.ProductRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductFavViewModel @Inject constructor(dbRepo: ProductDbRepo, mRepo: ProductRepo) : BaseViewModel(dbRepo, mRepo) {
    fun isBasket(product: ProductsBasket): Boolean {
        return _basketList.value?.contains(product) ?: false
    }
}