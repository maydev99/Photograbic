package com.bombadu.photograbic.repository

import androidx.lifecycle.LiveData
import com.bombadu.photograbic.local.LocalDao
import com.bombadu.photograbic.local.LocalData
import com.bombadu.photograbic.network.ImageResponse
import com.bombadu.photograbic.network.PixabayApi
import javax.inject.Inject

class DefaultImageRepository @Inject constructor(
    private val localDao: LocalDao,
    private val pixabayApi: PixabayApi
) : ImageRepository {


    override suspend fun insertEntry(localData: LocalData) {
        localDao.insertData(localData)
    }

    override suspend fun deleteEntry(localData: LocalData) {
        localDao.deleteData(localData)
    }

    override suspend fun searchForImage(imageQuery: String): ImageResponse? {
        val response = pixabayApi.searchForImage(imageQuery)
        return response.body()
    }



    override fun observeAllData(): LiveData<List<LocalData>> {
        return localDao.observeAllData()
    }


}