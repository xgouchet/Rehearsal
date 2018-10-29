package fr.xgouchet.rehearsal.core.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import fr.xgouchet.rehearsal.core.room.model.CueModel
import fr.xgouchet.rehearsal.core.room.model.CueWithCharacter

@Dao
interface CueDAO {

    @Query("SELECT * FROM cue WHERE cueId = :id")
    fun get(id: String): CueModel


    @Query("SELECT * FROM cue WHERE sceneId = :sceneId ORDER BY position ASC ")
    fun getAllFromScene(sceneId: Int): LiveData<List<CueModel>>

    @Transaction
    @Query("SELECT * FROM cue LEFT JOIN character ON cue.characterId = character.characterId WHERE sceneId = :sceneId ORDER BY position ASC")
    fun getUsers(sceneId: Int): LiveData<List<CueWithCharacter>>

    @Insert(onConflict = REPLACE)
    fun insert(cue: CueModel): Long

    @Delete
    fun delete(cue: CueModel)

    @Query("DELETE FROM cue")
    fun deleteAll()
}
