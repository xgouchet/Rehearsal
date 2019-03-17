package fr.xgouchet.rehearsal.core.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import fr.xgouchet.rehearsal.core.room.converter.DateConverter
import fr.xgouchet.rehearsal.core.room.dao.CharacterDAO
import fr.xgouchet.rehearsal.core.room.dao.CueDAO
import fr.xgouchet.rehearsal.core.room.dao.PropDAO
import fr.xgouchet.rehearsal.core.room.dao.RangeDao
import fr.xgouchet.rehearsal.core.room.dao.RehearsalDAO
import fr.xgouchet.rehearsal.core.room.dao.SceneDAO
import fr.xgouchet.rehearsal.core.room.dao.ScriptDAO
import fr.xgouchet.rehearsal.core.room.model.CharacterDbModel
import fr.xgouchet.rehearsal.core.room.model.CueDbModel
import fr.xgouchet.rehearsal.core.room.model.PropCueDbModel
import fr.xgouchet.rehearsal.core.room.model.PropDbModel
import fr.xgouchet.rehearsal.core.room.model.RangeDbModel
import fr.xgouchet.rehearsal.core.room.model.RehearsalDbModel
import fr.xgouchet.rehearsal.core.room.model.SceneDbModel
import fr.xgouchet.rehearsal.core.room.model.ScriptDbModel


@Database(entities = [ScriptDbModel::class, SceneDbModel::class, CharacterDbModel::class, CueDbModel::class, RehearsalDbModel::class, RangeDbModel::class, PropDbModel::class, PropCueDbModel::class],
        version = AppDatabase.V2_PROPS,
        exportSchema = true)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scriptDao(): ScriptDAO
    abstract fun sceneDao(): SceneDAO
    abstract fun cueDao(): CueDAO
    abstract fun characterDao(): CharacterDAO
    abstract fun rehearsalDao(): RehearsalDAO
    abstract fun rangeDao(): RangeDao
    abstract fun propDao(): PropDAO


    companion object {
        private const val DB_NAME = "AppDatabase"

        private var INSTANCE: AppDatabase? = null

        const val V1_BASE = 1
        const val V2_PROPS = 2

        internal val MIGRATION_1_2 = object : Migration(V1_BASE, V2_PROPS) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE `cue` ADD `props` INTEGER NOT NULL DEFAULT 0")
                database.execSQL("CREATE TABLE IF NOT EXISTS `prop` (`propId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `scriptId` INTEGER NOT NULL, `name` TEXT NOT NULL, FOREIGN KEY(`scriptId`) REFERENCES `script`(`scriptId`) ON UPDATE NO ACTION ON DELETE CASCADE )")
                database.execSQL("CREATE UNIQUE INDEX `index_prop_name` ON `prop` (`name`)")
                database.execSQL("CREATE  INDEX `index_prop_scriptId` ON `prop` (`scriptId`)")
                database.execSQL("CREATE TABLE IF NOT EXISTS `propcue` (`linkId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `cueId` INTEGER NOT NULL, `propId` INTEGER NOT NULL, FOREIGN KEY(`cueId`) REFERENCES `cue`(`cueId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`propId`) REFERENCES `prop`(`propId`) ON UPDATE NO ACTION ON DELETE CASCADE )")
                database.execSQL("CREATE  INDEX `index_propcue_cueId` ON `propcue` (`cueId`)")
                database.execSQL("CREATE  INDEX `index_propcue_propId` ON `propcue` (`propId`)")
            }
        }

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
                ).addMigrations(MIGRATION_1_2)
                        .build()
    }
}
