package com.elian.computeit.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.elian.computeit.data.database.dao.TipDAO
import com.elian.computeit.data.model.Tip
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(version = 1, entities = [Tip::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase()
{
    abstract val tipDAO: TipDAO

    companion object
    {
        const val databaseName = "app_database"
        val instance get() = INSTANCE as AppDatabase

        private const val NUMBER_OF_THREADS = 4;

        val executorService: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase
        {
            synchronized(this)
            {
                return INSTANCE
                    ?: Room.databaseBuilder(context, AppDatabase::class.java, databaseName)
                        .fallbackToDestructiveMigration()
                        .build()
                        .also { INSTANCE = it }
            }
        }

        fun create(context: Context)
        {
            INSTANCE = getDatabase(context)
        }
    }
}