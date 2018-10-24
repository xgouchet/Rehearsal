package fr.xgouchet.rehearsal.cast

import android.content.Context
import androidx.lifecycle.LiveData
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.CharacterModel

class CastDataSource(
        context: Context,
        scriptId: Int
) : CastContract.DataSource {


    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)
    private val characterList: LiveData<List<CharacterModel>> = appDatabase.characterDao().getAllFromScript(scriptId)

    override fun getData(): LiveData<List<CharacterModel>> {
        return characterList
    }

}
