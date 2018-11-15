package fr.xgouchet.rehearsal.core.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import fr.xgouchet.rehearsal.core.room.join.SceneWithCount
import fr.xgouchet.rehearsal.core.room.model.SceneModel

@Dao
interface SceneDAO {

    @Query("SELECT scene.*, COUNT(cue.cueId) as cues FROM cue LEFT JOIN scene ON cue.sceneId = scene.sceneId WHERE scriptId = :scriptId GROUP BY cue.sceneId ORDER BY position ASC")
    fun getAllFromScript(scriptId: Int): LiveData<List<SceneWithCount>>

    @Insert(onConflict = REPLACE)
    fun insert(scene: SceneModel): Long

}
