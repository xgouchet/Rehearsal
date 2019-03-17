package fr.xgouchet.rehearsal.core.room;

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import fr.xgouchet.elmyr.junit.JUnitForger
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AppDatabaseMigrationTest {

    private val TEST_DB = "migration-test"

    @Rule @JvmField val forger = JUnitForger()
    @Rule @JvmField val migrationHelper: MigrationTestHelper = MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            AppDatabase::class.java.canonicalName,
            FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun migrate1To2() {
        // create DB with old schema
        var db = migrationHelper.createDatabase(TEST_DB, AppDatabase.V1_BASE)
        db.close()

        // open DB with new version and migration
        db = migrationHelper.runMigrationsAndValidate(TEST_DB, AppDatabase.V2_PROPS, true, AppDatabase.MIGRATION_1_2)
    }

}
