package fr.xgouchet.rehearsal.schedule.details

import android.content.Context
import androidx.lifecycle.LiveData
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.RangeModel

class ScheduleRangesDataSource(
        context: Context,
        scheduleId: Int
) : ScheduleRangesContract.DataSource {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)
    private val rangesList: LiveData<List<RangeModel>> = appDatabase.rangeDao().getAllInSchedule(scheduleId)

    override fun getData(): LiveData<List<RangeModel>> {
        return rangesList
    }
}
