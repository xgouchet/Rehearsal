package fr.xgouchet.rehearsal.range

import android.content.Context
import androidx.lifecycle.LiveData
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.join.CueWithCharacter

class RangeCueDataSource(
        context: Context,
        sceneId: Int
) : RangePickerContract.CueDataSource {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)
    private val cueList: LiveData<List<CueWithCharacter>> = appDatabase.cueDao().getAllInScene(sceneId)

    override fun getData(): LiveData<List<CueWithCharacter>> {
        return cueList
    }
}
