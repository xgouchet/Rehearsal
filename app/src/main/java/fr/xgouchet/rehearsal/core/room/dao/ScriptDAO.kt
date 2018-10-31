package fr.xgouchet.rehearsal.core.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import fr.xgouchet.rehearsal.core.room.model.ScriptModel

@Dao
interface ScriptDAO {

    @Query("SELECT * FROM script")
    fun getAll(): LiveData<List<ScriptModel>>

    @Insert(onConflict = REPLACE)
    fun insert(script: ScriptModel): Long

    @Query("DELETE FROM script WHERE scriptId = :id")
    fun deleteById(id: Int)
}
