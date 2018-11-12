package fr.xgouchet.rehearsal.scene

import android.content.Context
import androidx.lifecycle.LiveData
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.CharacterModel

class CharactersDataSource(context: Context,
                           sceneId: Int)
    : SceneContract.CharacterDataSource {

    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)


    private val characters: LiveData<List<CharacterModel>> = appDatabase.characterDao().getAllFromScene(sceneId)

    override fun getData(): LiveData<List<CharacterModel>> {
        return characters
    }
}
