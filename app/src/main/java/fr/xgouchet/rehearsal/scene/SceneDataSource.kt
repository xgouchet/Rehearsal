package fr.xgouchet.rehearsal.scene

import android.content.Context
import androidx.lifecycle.LiveData
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.join.CueWithCharacter

class SceneDataSource(context: Context,
                      sceneId: Int,
                      range: Pair<Int, Int>?)
    : SceneContract.DataSource {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)


    private val cuesList: LiveData<List<CueWithCharacter>> = if (range == null) {
        appDatabase.cueDao().getAllInScene(sceneId)
    } else {
        appDatabase.cueDao().getAllInSceneInRange(sceneId, range.first, range.second)
    }

    override fun getData(): LiveData<List<CueWithCharacter>> {
        return cuesList
    }
}
