package com.bombadu.photograbic.repository

import androidx.lifecycle.LiveData
import com.bombadu.photograbic.local.LocalData

interface ImageRepository {

    suspend fun insertEntry(localData: LocalData)

    suspend fun deleteEntry(localData: LocalData)

    fun observeAllData(): LiveData<List<LocalData>>
}