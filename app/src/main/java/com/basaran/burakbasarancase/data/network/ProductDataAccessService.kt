package com.basaran.burakbasarancase.data.network

import com.basaran.burakbasarancase.data.model.Products
import retrofit2.Response
import retrofit2.http.GET

interface ProductDataAccessService {

    @GET("products")
    suspend fun getAllProducts(): Response<List<Products>>
}