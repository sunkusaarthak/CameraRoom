package com.appc.cameraroom

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageLocationDao {
    @Query("SELECT * FROM image_locations")
    fun getAll(): Flow<List<ImageLocation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(imageLocation: ImageLocation)

    @Delete
    suspend fun delete(imageLocation: ImageLocation)

    @Query("DELETE FROM image_locations")
    suspend fun deleteAll()
}