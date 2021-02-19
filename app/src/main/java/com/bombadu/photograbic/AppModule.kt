package com.bombadu.photograbic

import android.content.Context
import androidx.room.Room
import com.bombadu.photograbic.Constants.DATABASE_NAME
import com.bombadu.photograbic.local.LocalDao
import com.bombadu.photograbic.local.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDefaultImagePostRepository(
        dao: LocalDao,
    ) = DefaultImageRepository(dao) as ImageRepository

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
}