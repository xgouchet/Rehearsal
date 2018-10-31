package fr.xgouchet.rehearsal.core.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import fr.xgouchet.rehearsal.core.room.model.CueModel
import fr.xgouchet.rehearsal.core.room.model.CueWithCharacter

@Dao
interface CueDAO {

    @Transaction
    @Query("SELECT * FROM cue LEFT JOIN character ON cue.characterId = character.characterId WHERE sceneId = :sceneId ORDER BY position ASC")
    fun getAllInScene(sceneId: Int): LiveData<List<CueWithCharacter>>

    @Insert(onConflict = REPLACE)
    fun insert(cue: CueModel): Long
}
