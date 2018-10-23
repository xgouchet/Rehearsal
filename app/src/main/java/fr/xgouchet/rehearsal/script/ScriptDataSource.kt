package fr.xgouchet.rehearsal.script

import android.content.Context
import androidx.lifecycle.LiveData
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.SceneModel

class ScriptDataSource(context: Context,
                       scriptId: Int)
    : ScriptContract.DataSource {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)
    private val sceneList: LiveData<List<SceneModel>> = appDatabase.sceneDao().getAllFromScript(scriptId)

    override fun getData(): LiveData<List<SceneModel>> {
        return sceneList
    }
}
