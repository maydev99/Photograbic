package com.bombadu.photograbic.repository

import androidx.lifecycle.LiveData
import com.bombadu.photograbic.local.LocalData
import com.bombadu.photograbic.network.ImageResponse

interface ImageRepository {

    suspend fun insertEntry(localData: LocalData)

    suspend fun deleteEntry(localData: LocalData)

    suspend fun searchForImage(imageQuery: String): ImageResponse?

    fun observeAllData(): LiveData<List<LocalData>>
}