package fr.xgouchet.rehearsal.core.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.xgouchet.rehearsal.core.room.dao.CharacterDAO
import fr.xgouchet.rehearsal.core.room.dao.CueDAO
import fr.xgouchet.rehearsal.core.room.dao.RangeDao
import fr.xgouchet.rehearsal.core.room.dao.SceneDAO
import fr.xgouchet.rehearsal.core.room.dao.RehearsalDAO
import fr.xgouchet.rehearsal.core.room.dao.ScriptDAO
import fr.xgouchet.rehearsal.core.room.model.CharacterDbModel
import fr.xgouchet.rehearsal.core.room.model.CueDbModel
import fr.xgouchet.rehearsal.core.room.converter.DateConverter
import fr.xgouchet.rehearsal.core.room.model.RangeDbModel
import fr.xgouchet.rehearsal.core.room.model.SceneDbModel
import fr.xgouchet.rehearsal.core.room.model.RehearsalDbModel
import fr.xgouchet.rehearsal.core.room.model.ScriptDbModel


@Database(entities = [ScriptDbModel::class, SceneDbModel::class, CharacterDbModel::class, CueDbModel::class, RehearsalDbModel::class, RangeDbModel::class],
        version = 1,
        exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scriptDao(): ScriptDAO
    abstract fun sceneDao(): SceneDAO
    abstract fun cueDao(): CueDAO
    abstract fun characterDao(): CharacterDAO
    abstract fun rehearsalDao(): RehearsalDAO
    abstract fun rangeDao(): RangeDao


    companion object {
        private const val DB_NAME = "AppDatabase"

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
                Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DB_NAME
                )
                        .build()
    }
}
