package fr.xgouchet.rehearsal.core.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import fr.xgouchet.rehearsal.core.room.model.CueDbModel
import io.reactivex.Flowable

@Dao
interface CueDAO {

    @Query("SELECT * FROM cue WHERE sceneId = :sceneId ORDER BY position ASC")
    fun getAllInScene(sceneId: Long): Flowable<List<CueDbModel>>

    @Query("SELECT COUNT(cueId) FROM cue WHERE cue.sceneId = :sceneId AND cue.type = :type")
    fun countTypeInScene(sceneId: Long, type: Int): Int

    @Query("SELECT COUNT(cueId) FROM cue WHERE cue.characterId = :characterId AND cue.type = :type")
    fun countTypeWithCharacter(characterId: Long, type: Int): Int

    @Query("SELECT * FROM cue WHERE cueId = :sceneId")
    fun get(sceneId: Long): CueDbModel?

    @Insert(onConflict = REPLACE)
    fun insert(cue: CueDbModel): Long


    @Update
    fun updateAll(cue: CueDbModel): Int

    @Update
    fun updateAll(cues: List<CueDbModel>): Int

    @Query("DELETE FROM cue WHERE cue.cueId = :id")
    fun deleteById(id: Long): Int

    @Delete
    fun deleteAll(cues: List<CueDbModel>): Int
}
