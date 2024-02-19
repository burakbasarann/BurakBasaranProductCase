package com.basaran.burakbasarancase.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.basaran.burakbasarancase.data.model.ProductsBasket
import com.basaran.burakbasarancase.data.model.ProductsFav

@Database(entities = [ProductsFav::class, ProductsBasket::class], version = 1)
abstract class RoomDb : RoomDatabase() {

    abstract fun favProductsDao(): FavProductsDao

    abstract fun basketProductsDao(): BasketProductsDao

}
