package com.basaran.burakbasarancase.data

import android.content.Context
import androidx.room.Room
import com.basaran.burakbasarancase.common.util.Constants.BASE_URL
import com.basaran.burakbasarancase.data.database.RoomDb
import com.basaran.burakbasarancase.data.network.ProductDataAccessService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Singleton
    @Provides
    fun provideApiService(): ProductDataAccessService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app, RoomDb::class.java, "product_db"
    ).allowMainThreadQueries().build() // The reason we can construct a database for the repo

    @Singleton
    @Provides
    fun provideDaoFav(db: RoomDb) = db.favProductsDao()

    @Singleton
    @Provides
    fun provideDaoBasket(db: RoomDb) = db.basketProductsDao()
}