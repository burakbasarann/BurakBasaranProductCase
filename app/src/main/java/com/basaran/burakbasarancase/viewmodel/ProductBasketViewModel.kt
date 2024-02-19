package com.basaran.burakbasarancase.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.basaran.burakbasarancase.repo.ProductDbRepo
import com.basaran.burakbasarancase.repo.ProductRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductBasketViewModel @Inject constructor(dbRepo: ProductDbRepo, mRepo: ProductRepo) : BaseViewModel(dbRepo, mRepo) {

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double> get() = _totalPrice

    fun updateTotalValues() {
        val cartItemsList = _basketList.value ?: emptyList()
        var total = 0.0

        for (cartItem in cartItemsList) {
            total += cartItem.productsPiece * cartItem.price!!.toDouble()
        }
        _totalPrice.value = total
    }
}
