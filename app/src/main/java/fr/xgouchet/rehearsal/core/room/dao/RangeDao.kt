package fr.xgouchet.rehearsal.core.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import fr.xgouchet.rehearsal.core.room.model.RangeModel

@Dao
interface RangeDao {


    @Transaction
    @Query("SELECT * FROM range WHERE scheduleId = :scheduleId")
    fun getAllInSchedule(scheduleId: Int): LiveData<List<RangeModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(range: RangeModel): Long
}
