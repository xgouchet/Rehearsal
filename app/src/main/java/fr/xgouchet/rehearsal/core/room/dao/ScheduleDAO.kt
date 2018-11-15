package fr.xgouchet.rehearsal.core.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.xgouchet.rehearsal.core.room.model.CueModel
import fr.xgouchet.rehearsal.core.room.model.ScheduleModel


@Dao
interface ScheduleDAO {


    @Query("SELECT * FROM schedule WHERE scriptId = :scriptId ORDER BY dueDate ASC ")
    fun getAllFromScript(scriptId: Int): LiveData<List<ScheduleModel>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(schedule: ScheduleModel): Long
}
