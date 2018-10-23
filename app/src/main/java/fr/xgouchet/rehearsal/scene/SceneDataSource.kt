package fr.xgouchet.rehearsal.scene

import android.content.Context
import androidx.lifecycle.LiveData
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.CueWithCharacter
import fr.xgouchet.rehearsal.core.room.model.SceneModel

class SceneDataSource(context: Context,
                      sceneId: Int)
    : SceneContract.DataSource {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)


    private val sceneList: LiveData<List<CueWithCharacter>> = appDatabase.cueDao().getUsers(sceneId)

    override fun getData(): LiveData<List<CueWithCharacter>> {
        return sceneList
    }
}
