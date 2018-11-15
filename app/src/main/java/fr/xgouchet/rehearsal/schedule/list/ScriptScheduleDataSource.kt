package fr.xgouchet.rehearsal.schedule.list

import android.content.Context
import androidx.lifecycle.LiveData
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.ScheduleModel

class ScriptScheduleDataSource(context: Context,
                               scriptId: Int)
    : ScriptScheduleContract.DataSource {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)
    private val sceneList: LiveData<List<ScheduleModel>> = appDatabase.scheduleDao().getAllFromScript(scriptId)

    override fun getData(): LiveData<List<ScheduleModel>> {
        return sceneList
    }
}
