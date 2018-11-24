package fr.xgouchet.rehearsal.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.xgouchet.rehearsal.core.room.model.RehearsalDbModel
import io.reactivex.Flowable


@Dao
interface RehearsalDAO {


    @Query("SELECT * FROM rehearsal WHERE scriptId = :scriptId ORDER BY dueDate ASC ")
    fun getAllFromScript(scriptId: Long): Flowable<List<RehearsalDbModel>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(schedule: RehearsalDbModel): Long


    @Query("DELETE FROM rehearsal WHERE rehearsal.rehearsalId = :id")
    fun deleteById(id: Long): Int
}
