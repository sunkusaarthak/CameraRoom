package com.appc.cameraroom

import android.app.Application
import android.content.Context
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ImageLocationRepository(private val imageLocationDao: ImageLocationDao) {
    val allImageLocation: Flow<List<ImageLocation>> = imageLocationDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertImageLocation(imageLocation: ImageLocation) {
        imageLocationDao.insert(imageLocation)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteImageLocation(imageLocation: ImageLocation) {
        imageLocationDao.delete(imageLocation)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        imageLocationDao.deleteAll()
    }
}