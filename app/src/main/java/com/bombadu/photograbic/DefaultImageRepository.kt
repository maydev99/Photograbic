package com.bombadu.photograbic

import androidx.lifecycle.LiveData
import com.bombadu.photograbic.local.LocalDao
import com.bombadu.photograbic.local.LocalData
import javax.inject.Inject

class DefaultImageRepository @Inject constructor(
    private val localDao: LocalDao
) : ImageRepository {
    override suspend fun insertEntry(localData: LocalData) {
        localDao.insertData(localData)
    }

    override suspend fun deleteEntry(localData: LocalData) {
        localDao.deleteData(localData)
    }

    override fun observeAllData(): LiveData<List<LocalData>> {
        return localDao.observeAllData()
    }


}