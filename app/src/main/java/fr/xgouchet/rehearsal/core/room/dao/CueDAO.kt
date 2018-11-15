package fr.xgouchet.rehearsal.core.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import fr.xgouchet.rehearsal.core.room.join.CueWithCharacter
import fr.xgouchet.rehearsal.core.room.model.CueModel

@Dao
interface CueDAO {

    @Transaction
    @Query("SELECT * FROM cue LEFT JOIN character ON cue.characterId = character.characterId WHERE sceneId = :sceneId ORDER BY position ASC")
    fun getAllInScene(sceneId: Int): LiveData<List<CueWithCharacter>>


    @Transaction
    @Query("SELECT * FROM cue  LEFT JOIN character ON cue.characterId = character.characterId WHERE sceneId = :sceneId AND position >= :startPosition AND position <= :endPosition")
    fun getAllInSceneInRange(sceneId: Int, startPosition: Int, endPosition: Int): LiveData<List<CueWithCharacter>>

    @Insert(onConflict = REPLACE)
    fun insert(cue: CueModel): Long


    @Update
    fun update(cue: CueModel): Int

    @Query("DELETE FROM cue WHERE cue.cueId = :id")
    fun deleteById(id: Int)
}
