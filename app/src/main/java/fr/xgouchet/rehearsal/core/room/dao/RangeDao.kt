package fr.xgouchet.rehearsal.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import fr.xgouchet.rehearsal.core.room.model.RangeModel

@Dao
interface RangeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(range: RangeModel): Long
}
