package com.appc.cameraroom

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ImageLocationApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { ImageLocationDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { ImageLocationRepository(database.imageLocationDao()) }
}