package fr.xgouchet.rehearsal.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import fr.xgouchet.rehearsal.core.room.model.SceneDbModel
import io.reactivex.Flowable

@Dao
interface SceneDAO {

    @Query("SELECT * FROM scene WHERE scriptId = :scriptId ORDER BY position ASC")
    fun getAllFromScript(scriptId: Long): Flowable<List<SceneDbModel>>

    @Query("SELECT * FROM scene  WHERE sceneId = :sceneId")
    fun get(sceneId: Long): SceneDbModel?

    @Insert(onConflict = REPLACE)
    fun insert(scene: SceneDbModel): Long

}
