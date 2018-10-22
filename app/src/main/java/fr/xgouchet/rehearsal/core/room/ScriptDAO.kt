package fr.xgouchet.rehearsal.core.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface ScriptDAO {

    @Query("select * from script where id = :id")
    fun get(id: String): ScriptModel


    @Query("SELECT * from script")
    fun getAll(): LiveData<List<ScriptModel>>

    @Insert(onConflict = REPLACE)
    fun insert(script: ScriptModel)

    @Delete
    fun delete(script: ScriptModel)

    @Query("DELETE from script")
    fun deleteAll()
}