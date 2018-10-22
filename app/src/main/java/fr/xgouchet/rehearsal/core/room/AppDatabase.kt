package fr.xgouchet.rehearsal.core.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ScriptModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scriptDao(): ScriptDAO

    companion object {
        private val DB_NAME = "AppDatabase"

        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        fun destroyInstance() {
            synchronized(this) {
                INSTANCE = null
            }
        }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, DB_NAME)
                        .build()
    }
}