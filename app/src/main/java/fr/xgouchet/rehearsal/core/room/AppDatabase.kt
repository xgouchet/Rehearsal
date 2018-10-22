package fr.xgouchet.rehearsal.core.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.xgouchet.rehearsal.core.room.dao.CharacterDAO
import fr.xgouchet.rehearsal.core.room.dao.CueDAO
import fr.xgouchet.rehearsal.core.room.dao.SceneDAO
import fr.xgouchet.rehearsal.core.room.dao.ScriptDAO
import fr.xgouchet.rehearsal.core.room.model.CharacterModel
import fr.xgouchet.rehearsal.core.room.model.CueModel
import fr.xgouchet.rehearsal.core.room.model.SceneModel
import fr.xgouchet.rehearsal.core.room.model.ScriptModel

@Database(entities = [ScriptModel::class, SceneModel::class, CharacterModel::class, CueModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scriptDao(): ScriptDAO
    abstract fun sceneDao(): SceneDAO
    abstract fun cueDao(): CueDAO
    abstract fun characterDao(): CharacterDAO

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
                Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DB_NAME
                )
                        .build()
    }
}
