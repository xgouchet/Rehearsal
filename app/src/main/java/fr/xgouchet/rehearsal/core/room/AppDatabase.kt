package fr.xgouchet.rehearsal.core.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import fr.xgouchet.rehearsal.core.room.dao.CharacterDAO
import fr.xgouchet.rehearsal.core.room.dao.CueDAO
import fr.xgouchet.rehearsal.core.room.dao.RangeDao
import fr.xgouchet.rehearsal.core.room.dao.SceneDAO
import fr.xgouchet.rehearsal.core.room.dao.ScheduleDAO
import fr.xgouchet.rehearsal.core.room.dao.ScriptDAO
import fr.xgouchet.rehearsal.core.room.model.CharacterModel
import fr.xgouchet.rehearsal.core.room.model.CueModel
import fr.xgouchet.rehearsal.core.room.model.DateConverter
import fr.xgouchet.rehearsal.core.room.model.RangeModel
import fr.xgouchet.rehearsal.core.room.model.SceneModel
import fr.xgouchet.rehearsal.core.room.model.ScheduleModel
import fr.xgouchet.rehearsal.core.room.model.ScriptModel


@Database(entities = [ScriptModel::class, SceneModel::class, CharacterModel::class, CueModel::class, ScheduleModel::class, RangeModel::class],
        version = 3,
        exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scriptDao(): ScriptDAO
    abstract fun sceneDao(): SceneDAO
    abstract fun cueDao(): CueDAO
    abstract fun characterDao(): CharacterDAO
    abstract fun scheduleDao(): ScheduleDAO
    abstract fun rangeDao() : RangeDao

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
                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                        .build()

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `schedule` (`scheduleId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `scriptId` INTEGER, `dueDate` INTEGER NOT NULL, `note` TEXT, FOREIGN KEY(`scriptId`) REFERENCES `script`(`scriptId`) ON UPDATE NO ACTION ON DELETE CASCADE )")
                database.execSQL("CREATE  INDEX `index_schedule_scriptId` ON `schedule` (`scriptId`)")
                database.execSQL("CREATE TABLE IF NOT EXISTS `range` (`rangeId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `startCueId` INTEGER, `endCueId` INTEGER, `scheduleId` INTEGER, FOREIGN KEY(`startCueId`) REFERENCES `cue`(`cueId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`endCueId`) REFERENCES `cue`(`cueId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`scheduleId`) REFERENCES `schedule`(`scheduleId`) ON UPDATE NO ACTION ON DELETE CASCADE )")
                database.execSQL("CREATE  INDEX `index_range_startCueId` ON `range` (`startCueId`)")
                database.execSQL("CREATE  INDEX `index_range_endCueId` ON `range` (`endCueId`)")
                database.execSQL("CREATE  INDEX `index_range_scheduleId` ON `range` (`scheduleId`)")

            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL("DROP TABLE IF EXISTS `range`")

                database.execSQL("CREATE TABLE IF NOT EXISTS `range` (`rangeId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `scheduleId` INTEGER, `start_cueId` INTEGER, `start_sceneId` INTEGER, `start_characterId` INTEGER, `start_position` INTEGER, `start_type` INTEGER, `start_characterExtension` TEXT, `start_content` TEXT, `start_isBookmarked` INTEGER, `start_note` TEXT, `end_cueId` INTEGER, `end_sceneId` INTEGER, `end_characterId` INTEGER, `end_position` INTEGER, `end_type` INTEGER, `end_characterExtension` TEXT, `end_content` TEXT, `end_isBookmarked` INTEGER, `end_note` TEXT, FOREIGN KEY(`scheduleId`) REFERENCES `schedule`(`scheduleId`) ON UPDATE NO ACTION ON DELETE CASCADE )")
                database.execSQL("CREATE  INDEX `index_range_scheduleId` ON `range` (`scheduleId`)")
            }
        }
    }
}
