package fr.xgouchet.rehearsal.core.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import fr.xgouchet.rehearsal.core.room.model.CueModel

@Dao
interface CueDAO {

    @Query("SELECT * FROM cue WHERE id = :id")
    fun get(id: String): CueModel


    @Query("SELECT * FROM cue WHERE sceneId = :sceneId ORDER BY position ASC ")
    fun getAllFromScene(sceneId: Int): LiveData<List<CueModel>>

    @Insert(onConflict = REPLACE)
    fun insert(cue: CueModel): Long

    @Delete
    fun delete(cue: CueModel)

    @Query("DELETE FROM cue")
    fun deleteAll()
}
