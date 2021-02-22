package com.bombadu.photograbic.di

import android.content.Context
import androidx.room.Room
import com.bombadu.photograbic.util.Constants.DATABASE_NAME
import com.bombadu.photograbic.repository.DefaultImageRepository
import com.bombadu.photograbic.repository.ImageRepository
import com.bombadu.photograbic.local.LocalDao
import com.bombadu.photograbic.local.LocalDatabase
import com.bombadu.photograbic.network.PixabayApi
import com.bombadu.photograbic.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDefaultImagePostRepository(
        dao: LocalDao,
        api: PixabayApi
    ) = DefaultImageRepository(dao, api) as ImageRepository

    @Singleton
    @Provides
    fun provideLocalDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, LocalDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideLocalDao(
        database: LocalDatabase
    ) = database.localDao()

    @Singleton
    @Provides
    fun providePixayApi(): PixabayApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PixabayApi::class.java)
    }

}

