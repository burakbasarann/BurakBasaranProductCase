package com.basaran.burakbasarancase.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductsBasket(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    var createdAt: String?,
    var name: String?,
    var image: String?,
    var price: String?,
    var description: String?,
    var model: String?,
    var brand: String?,
    var productsPiece: Int = 1
)