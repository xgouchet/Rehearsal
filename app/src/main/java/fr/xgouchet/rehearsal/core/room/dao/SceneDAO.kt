package fr.xgouchet.rehearsal.core.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import fr.xgouchet.rehearsal.core.room.model.SceneModel

@Dao
interface SceneDAO {

    @Query("SELECT * FROM scene WHERE sceneId = :id")
    fun get(id: String): SceneModel

    @Query("SELECT * FROM scene WHERE scriptId = :scriptId ORDER BY position ASC")
    fun getAllFromScript(scriptId: Int): LiveData<List<SceneModel>>

    @Insert(onConflict = REPLACE)
    fun insert(scene: SceneModel): Long

    @Delete
    fun delete(scene: SceneModel)

    @Query("DELETE FROM scene WHERE scriptId = :scriptId ")
    fun deleteAllFromScript(scriptId: Int)
}
