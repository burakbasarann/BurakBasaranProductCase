package com.basaran.burakbasarancase.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.basaran.burakbasarancase.data.database.BasketProductsDao
import com.basaran.burakbasarancase.data.database.FavProductsDao
import com.basaran.burakbasarancase.data.model.ProductsBasket
import com.basaran.burakbasarancase.data.model.ProductsFav
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductDbRepo @Inject constructor(private val favoriteProductsDao: FavProductsDao, private val basketProductsDao: BasketProductsDao) {
    private var favoriteProductsList: MutableLiveData<MutableList<ProductsFav>> = MutableLiveData()
    private var basketProductsList: MutableLiveData<MutableList<ProductsBasket>> = MutableLiveData()
    fun getFav(): MutableLiveData<MutableList<ProductsFav>> {
        return favoriteProductsList
    }

    fun allFavData(): Boolean {
        return try {
            val favoriteProducts = favoriteProductsDao.getAllFavEntities()
            favoriteProductsList.postValue(favoriteProducts)
            Log.d("FavProductsDB", "Fav products fetched successfully.")
            true
        } catch (e: Exception) {
            Log.e("FavProductsDB", "Error fetching fav products:", e)
            false
        }
    }

    fun insertFavProducts(roomDBEntity: ProductsFav){
        return favoriteProductsDao.insertFav(roomDBEntity)
    }

    fun deleteFavProducts(roomDBEntity: ProductsFav){
        return favoriteProductsDao.deleteFav(entityDelete = roomDBEntity)
    }

    fun getBasket(): MutableLiveData<MutableList<ProductsBasket>> {
        return basketProductsList
    }

    fun allBasketData() {
        CoroutineScope(Dispatchers.Main).launch {
            basketProductsList.value = basketProductsDao.getAllBasketData()
        }
    }

    fun insertBasketProducts(roomDBEntity: ProductsBasket){
        return basketProductsDao.insertBasket(entity = roomDBEntity)
    }

    fun deleteBasketProducts(roomDBEntity: ProductsBasket){
        return basketProductsDao.deleteBasket(entityDelete = roomDBEntity)
    }

    fun updateBasketProducts(roomDBEntity: ProductsBasket){
        return basketProductsDao.updateBasketProduct(entityUpdate = roomDBEntity)
    }
}