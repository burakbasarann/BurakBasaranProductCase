package com.basaran.burakbasarancase.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.basaran.burakbasarancase.data.model.ProductsBasket

@Dao
interface BasketProductsDao {
    @Insert
    fun insertBasket(entity: ProductsBasket)

    @Query("SELECT * FROM products")
    fun getAllBasketData(): MutableList<ProductsBasket>

    @Delete
    fun deleteBasket(entityDelete: ProductsBasket)

    @Update
    fun updateBasketProduct(entityUpdate: ProductsBasket)
}