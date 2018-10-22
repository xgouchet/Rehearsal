package fr.xgouchet.rehearsal.core.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import fr.xgouchet.rehearsal.core.room.model.ScriptModel

@Dao
interface ScriptDAO {

    @Query("SELECT * FROM script WHERE id = :id")
    fun get(id: String): ScriptModel


    @Query("SELECT * FROM script")
    fun getAll(): LiveData<List<ScriptModel>>

    @Insert(onConflict = REPLACE)
    fun insert(script: ScriptModel): Long

    @Delete
    fun delete(script: ScriptModel)

    @Query("DELETE FROM script")
    fun deleteAll()
}
