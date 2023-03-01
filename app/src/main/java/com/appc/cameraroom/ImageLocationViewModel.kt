package com.appc.cameraroom

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class ImageLocationViewModel(private val repository: ImageLocationRepository) : ViewModel() {
    var imageLocation : LiveData<List<ImageLocation>> = repository.allImageLocation.asLiveData()

    fun addImageLocationItem(newTask : ImageLocation) = viewModelScope.launch{
        repository.insertImageLocation(newTask)
    }

    fun deleteImageLocationItem(imageLocation: ImageLocation) = viewModelScope.launch {
        repository.deleteImageLocation(imageLocation)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}

class TaskItemModelFactory(private val repository: ImageLocationRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ImageLocationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ImageLocationViewModel(repository) as T
        }
        throw java.lang.IllegalArgumentException("Unknown Class for View Model")
    }
}