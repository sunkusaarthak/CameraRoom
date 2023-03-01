package com.appc.cameraroom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Database(entities = [ImageLocation::class], version = 1, exportSchema = false)
abstract class ImageLocationDatabase : RoomDatabase() {
    abstract fun imageLocationDao(): ImageLocationDao

    private class ImageLocationDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.imageLocationDao())
                }
            }
        }

        suspend fun populateDatabase(imageLocationDao: ImageLocationDao) {
            imageLocationDao.deleteAll()
            var location = ImageLocation(location = "Hello")
            imageLocationDao.insert(location)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ImageLocationDatabase? = null
        fun getDatabase(context: Context, scope: CoroutineScope): ImageLocationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance =Room.databaseBuilder(
                    context.applicationContext,
                    ImageLocationDatabase::class.java,
                    "image_location_database"
                )
                    .addCallback(ImageLocationDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

