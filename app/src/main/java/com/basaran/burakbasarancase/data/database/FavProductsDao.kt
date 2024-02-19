package com.basaran.burakbasarancase.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.basaran.burakbasarancase.data.model.ProductsFav

@Dao
interface FavProductsDao {
    @Insert
    fun insertFav(entity: ProductsFav)

    @Query("SELECT * FROM fav")
    fun getAllFavEntities(): MutableList<ProductsFav>

    @Delete
    fun deleteFav(entityDelete: ProductsFav)
}