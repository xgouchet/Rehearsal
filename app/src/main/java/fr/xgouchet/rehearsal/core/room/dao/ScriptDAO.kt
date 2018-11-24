package fr.xgouchet.rehearsal.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import fr.xgouchet.rehearsal.core.room.model.ScriptDbModel
import io.reactivex.Flowable

@Dao
interface ScriptDAO {

    @Query("SELECT * FROM script")
    fun getAll(): Flowable<List<ScriptDbModel>>

    @Insert(onConflict = REPLACE)
    fun insert(script: ScriptDbModel): Long

    @Query("DELETE FROM script WHERE scriptId = :id")
    fun deleteById(id: Long): Int
}
