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

    @Query("SELECT * FROM script WHERE scriptId = :id")
    fun get(id: Int): ScriptModel


    @Query("SELECT * FROM script")
    fun getAll(): LiveData<List<ScriptModel>>

    @Insert(onConflict = REPLACE)
    fun insert(script: ScriptModel): Long

    @Delete
    fun delete(script: ScriptModel)

    @Query("DELETE FROM script WHERE scriptId = :id")
    fun deleteById(id: Int)

    @Query("DELETE FROM script")
    fun deleteAll()
}
