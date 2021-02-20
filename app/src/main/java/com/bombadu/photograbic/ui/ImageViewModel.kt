package com.bombadu.photograbic.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bombadu.photograbic.local.LocalData
import com.bombadu.photograbic.repository.ImageRepository
import kotlinx.coroutines.launch

class ImageViewModel @ViewModelInject constructor(
    private val repository: ImageRepository
) : ViewModel() {

    private val allData: LiveData<List<LocalData>>

    init {

        allData = repository.observeAllData()
    }

    val allImageData = repository.observeAllData()

    fun insertImageDataIntoDB(localData: LocalData) = viewModelScope.launch {
        repository.insertEntry(localData)
    }

    fun deleteImageItem(localData: LocalData) = viewModelScope.launch {
        repository.deleteEntry(localData)
    }

    fun getAllLocalImages(): LiveData<List<LocalData>> {
        return allData
    }

}